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
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import dev.davron.regionaltaxidriver.dialogs.registration.ChooseAddPhotoTypeDialog
import dev.davron.regionaltaxidriver.models.login.fullName.RequestFullName
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class AddProfilePhotoFragment : Fragment(), ChooseAddPhotoTypeDialog.OnItemClickListener {

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
        uiClickListener()
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


}