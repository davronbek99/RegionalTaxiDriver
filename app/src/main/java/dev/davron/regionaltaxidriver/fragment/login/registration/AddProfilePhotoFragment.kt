package dev.davron.regionaltaxidriver.fragment.login.registration

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import dev.davron.regionaltaxidriver.OnlineRegistrationActivity
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.FragmentAddProfilePhotoBinding
import dev.davron.regionaltaxidriver.dialogs.exitDialog.ReallyYouWantToExit
import dev.davron.regionaltaxidriver.dialogs.registration.ChooseAddPhotoTypeDialog
import dev.davron.regionaltaxidriver.models.attachUpload.AttachUpload
import dev.davron.regionaltaxidriver.models.login.fullName.RequestFullInformation
import dev.davron.regionaltaxidriver.models.login.fullName.RequestFullName
import dev.davron.regionaltaxidriver.responseApis.ResApis
import dev.davron.regionaltaxidriver.utils.Common
import dev.davron.regionaltaxidriver.utils.getBackStackData
import dev.davron.regionaltaxidriver.utils.getExtension
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class AddProfilePhotoFragment : Fragment(), ChooseAddPhotoTypeDialog.OnItemClickListener,
    ReallyYouWantToExit.OnItemClickListener {

    private lateinit var binding: FragmentAddProfilePhotoBinding
    private lateinit var personalInformationViewModel: PersonalInformationViewModel

    private lateinit var requestFullName: RequestFullName

    private var photoFile: File? = null
    private var imageUri: Uri? = null
    private var isBackedFromConfirm = false
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestFullName = arguments?.getSerializable("requestFullName") as RequestFullName

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProfilePhotoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setViewModelListener()
        setOnBackPressed()
        uiClickListener()
        setBackStackPosition()
    }

    private fun setBackStackPosition() {
        getBackStackData<Boolean>("success", true) {
            if (it) {
                binding.avaImageView.setImageURI(imageUri)
                binding.addPhotoButton.text = getString(R.string.change_photo)

                val bundle = Bundle()
                bundle.putSerializable("file", photoFile)
                bundle.putString("from", "camera")
                findNavController().navigate(R.id.confirmPhotoFragment, bundle)
            } else {
                if (isBackedFromConfirm) {
                    val bundle = Bundle()
                    bundle.putSerializable("file", photoFile)
                    bundle.putString("from", "camera")
                    findNavController().navigate(R.id.confirmPhotoFragment, bundle)
                }
            }
        }

        getBackStackData<String>("from", true) {
            isBackedFromConfirm = true
            if (it == "gallery") {
                addPhotoFromGallery()
            } else {
                addPhotoFromCamera()
            }
        }

        getBackStackData<Bitmap?>("bitmap", true) {
            if (it != null) {
                binding.avaImageView.setImageBitmap(it)
                bitmap = it

                binding.addPhotoButton.alpha = 0.4f
                binding.addPhotoButton.text = ""
                binding.proggress.visibility = View.VISIBLE
                binding.skipButton.isEnabled = false

                val stream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                val byteArray: ByteArray = stream.toByteArray()
                var encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT)

                encodedString = "data:image/jpeg;base64,$encodedString"

//                personalInformationViewModel.attachUpload(AttachUpload())
//                personalInformationViewModel.requestFullInfo(
//                    RequestFullInformation(
//                        requestFullName.firstName,
//                        requestFullName.lastName,
//                        requestFullName.birthday,
//                        Common.phoneNumber ?: "",
//                        encodedString
//                    )
//                )

                getImage.launch("image/*")
            } else {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setViewModelListener() {
        personalInformationViewModel.requestFullInformationData.observe(requireActivity()) {
            when (it) {
                is ResApis.Error -> {
//                    Toast.makeText(requireContext(), "No", Toast.LENGTH_SHORT).show()
                }

                is ResApis.Success -> {
//                    Toast.makeText(requireContext(), "Yes", Toast.LENGTH_SHORT).show()
                }

                else -> {

                }
            }
        }
    }

    private fun init() {
        personalInformationViewModel =
            ViewModelProvider(this)[PersonalInformationViewModel::class.java]

    }

    private fun uiClickListener() {
        binding.skipButton.setOnClickListener {
            val intent = Intent(activity, OnlineRegistrationActivity::class.java)
            startActivity(intent)
        }

        binding.addPhotoButton.setOnClickListener {
            val addPhotoTypeDialog = ChooseAddPhotoTypeDialog(requireContext(), this)
            addPhotoTypeDialog.show()
        }
    }

    override fun onGallerySelected() {
        if (requireContext().checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            addPhotoFromGallery()
        } else {
            requestReadStoragePermission()
        }
    }

    private fun requestReadStoragePermission() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    addPhotoFromGallery()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("parts", requireActivity().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

            })
    }

    private fun addPhotoFromGallery() {
        getPhotoFromGallery.launch("image/*")
    }

    override fun onCameraSelected() {
        if (requireContext().checkCallingOrSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            addPhotoFromCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    addPhotoFromCamera()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

            }).check()
    }

    private fun addPhotoFromCamera() {
        photoFile = try {
            createImageFile()
        } catch (e: Exception) {
            null
        }
        val bundle = Bundle()
        bundle.putSerializable("file", photoFile)
        bundle.putSerializable("from", true)
        findNavController().navigate(R.id.cameraFragment, bundle)
    }

    private fun createImageFile(): File? {
        val m = System.currentTimeMillis()
        val externalFilesDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("my_images_$m", ".jpg", externalFilesDir)
    }

    private val getPhotoFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            Toast.makeText(requireContext(), "Gallery", Toast.LENGTH_SHORT).show()
            if (it != null) {
                photoFile = saveImage(it, System.currentTimeMillis().toString())
                binding.avaImageView.setImageURI(Uri.fromFile(photoFile))
                binding.addPhotoButton.text = getString(R.string.change_photo)
                binding.nextButton.visibility = View.VISIBLE

                val bundle = Bundle()
                bundle.putSerializable("file", photoFile)
                bundle.putString("from", "gallery")
                findNavController().navigate(R.id.to_confirm_photo, bundle)
            } else {
                if (isBackedFromConfirm) {
                    val bundle = Bundle()
                    bundle.putSerializable("file", photoFile)
                    bundle.putString("from", "gallery")
                    findNavController().navigate(R.id.to_confirm_photo, bundle)
                }
            }
        }

    @SuppressLint("Recycle")
    private fun saveImage(imageUri: Uri?, filename: String): File {

        val dir = requireContext().getDir(
            "TaxiDriver", Context.MODE_PRIVATE
        ) // Creates Dir inside internal memory

        val file: File?
        if (imageUri != null) {
            file = File(dir, filename) // It has directory details and file name

            if (file.exists()) file.delete()

            file.createNewFile()

            val fos = FileOutputStream(file)
            try {
                val inputStream = activity?.contentResolver?.openInputStream(imageUri)!!

                inputStream.copyTo(fos, DEFAULT_BUFFER_SIZE)

                fos.flush()
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            file = File(dir, "$filename.jpg") // It has directory details and file name

            if (file.exists()) file.delete()

            file.createNewFile()
        }
        return file
    }

    private fun setOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback {
            val dialog = ReallyYouWantToExit(requireContext(), this@AddProfilePhotoFragment)
            dialog.show()
        }
    }

    override fun onCancelButtonClicked() {

    }

    override fun onExitButtonClicked() {
        findNavController().popBackStack()
    }

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        Toast.makeText(requireContext(), "get image ga kirdi", Toast.LENGTH_SHORT).show()
        if (it != null) {
            Toast.makeText(requireContext(), "button Clicked", Toast.LENGTH_SHORT).show()
            val cR = requireContext().contentResolver
            val mime = MimeTypeMap.getSingleton()
            val type = mime.getExtensionFromMimeType(cR.getType(it))
            val cursor = cR.query(it, null, null, null, null)

            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val displayName = cursor.getString(index)
                val file = saveImage(it, displayName)

                val requestFile =
                    RequestBody.create(
                        MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.getExtension())
                            ?.toMediaType(), file
                    )

                // MultipartBody.Part is used to send also the actual file name
                val photoBody = MultipartBody.Part.createFormData("photo", file.name, requestFile)
                Toast.makeText(requireContext(), "So'rov ke`tdi", Toast.LENGTH_SHORT).show()
//                personalInformationViewModel.attachUpload(AttachUpload(photoBody))
//                binding.loadingLayout.loadingLayout.visibility = View.VISIBLE
//
//                Glide.with(binding.image).load(it)
//                    .placeholder(R.drawable.ic_carbon_user_avatar_filled)
//                    .into(binding.image)
            }
        }
    }

}