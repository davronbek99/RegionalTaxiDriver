package dev.davron.regionaltaxidriver.fragment.login.registration

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
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
import com.fenchtose.nocropper.Cropper
import com.fenchtose.nocropper.CropperCallback
import dagger.hilt.android.AndroidEntryPoint
import dev.davron.regionaltaxidriver.databinding.FragmentConfirmPhotoBinding
import dev.davron.regionaltaxidriver.models.attachUpload.AttachUpload
import dev.davron.regionaltaxidriver.models.login.fullName.RequestFullInformation
import dev.davron.regionaltaxidriver.responseApis.ResApis
import dev.davron.regionaltaxidriver.utils.Common
import dev.davron.regionaltaxidriver.utils.flipHorizontal
import dev.davron.regionaltaxidriver.utils.getBackStackData
import dev.davron.regionaltaxidriver.utils.getExtension
import dev.davron.regionaltaxidriver.utils.getRotationRightBitmap
import dev.davron.regionaltaxidriver.utils.rotatedImage
import dev.davron.regionaltaxidriver.utils.setBackStackData
import dev.davron.regionaltaxidriver.utils.setBottomAnimations
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class ConfirmPhotoFragment : Fragment() {

    private lateinit var binding: FragmentConfirmPhotoBinding

    private lateinit var viewModel: PersonalInformationViewModel
    private var photoFile: File? = null
    private var fromWhere: String = "gallery"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomAnimations()

        arguments?.let {
            photoFile = it.getSerializable("file") as File?
            fromWhere = it.getString("from", "gallery")
        }
    }

    private lateinit var cropper: Cropper
    private lateinit var bitmap: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        viewModelListener()
        setOnBackPressed()
        setUpClickListeners()
        setUpPhotoCrop()
    }

    private fun viewModelListener() {
        viewModel.attachUploadData.observe(requireActivity()) {
            when (it) {
                is ResApis.Error -> {
                    Toast.makeText(requireContext(), "Attach Upload Error", Toast.LENGTH_SHORT)
                        .show()
                }

                is ResApis.Success -> {
                    Toast.makeText(requireContext(), "Attach Upload Success", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {

                }
            }
        }
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[PersonalInformationViewModel::class.java]


    }

    private fun setUpPhotoCrop() {
        bitmap = getRotationRightBitmap(photoFile!!)

        if (fromWhere == "gallery") {
            bitmap = rotatedImage(bitmap, 0f)
        } else {
//            if (Common.currentCameraType == 0 && !Common.isPhotoChangingForFragments) {
//                bitmap = rotatedImage(bitmap, -90f)
//                bitmap.flipHorizontal()
//            } else {
//                bitmap = rotatedImage(bitmap, 0f)
//            }
        }
        binding.cropperView.setImageBitmap(bitmap)
    }

    private fun setUpClickListeners() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveButton.setOnClickListener {
            cropper = Cropper(binding.cropperView.cropInfo.cropInfo, bitmap)

            cropper.crop(object : CropperCallback() {
                override fun onCropped(bitmap: Bitmap?) {
                    // to do save new bitmap and close this fragment
                    if (photoFile != null && bitmap != null) {
                        Toast.makeText(requireContext(), "1-if", Toast.LENGTH_SHORT).show()

                        getImage.launch("image/*")
                        if (Common.isPhotoChangingForFragments) {
                            Toast.makeText(requireContext(), "2-if", Toast.LENGTH_SHORT).show()
                            Common.bitmapMutableLiveData.postValue(bitmap)
                        } else {
                            setBackStackData("bitmap", bitmap, true)

                        }
                    } else {
                        Toast.makeText(
                            requireContext(), "Something went wrong!", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
            cropper.cropBitmap()
        }

        binding.retakeButton.setOnClickListener {
            //todo retake option
            if (Common.isPhotoChangingForFragments) {
                Common.fromMutableLiveData.postValue(fromWhere)
                findNavController().popBackStack()
            } else {
                setBackStackData("from", fromWhere, true)
            }
        }
    }

    private fun saveImage(photoFile: File, bitmap: Bitmap) {
        try {
            val fileOutputStream: FileOutputStream =
                requireActivity().openFileOutput(photoFile.name, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback {
            findNavController().popBackStack()
        }
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
                Toast.makeText(requireContext(), "So'rov ketdi", Toast.LENGTH_SHORT).show()
                viewModel.attachUpload(AttachUpload(photoBody))
//                binding.loadingLayout.loadingLayout.visibility = View.VISIBLE
//
//                Glide.with(binding.image).load(it)
//                    .placeholder(R.drawable.ic_carbon_user_avatar_filled)
//                    .into(binding.image)
            }
        }
    }


    @SuppressLint("Recycle")
    private fun saveImage(imageUri: Uri?, filename: String): File {

        val dir = requireContext().getDir(
            "TaxiDriver",
            Context.MODE_PRIVATE
        ) //Creates Dir inside internal memory

        var file: File? = null

        if (imageUri != null) {
            file = File(dir, filename) //It has directory details and file name

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
            file = File(dir, "$filename.jpg") //It has directory details and file name

            if (file.exists()) file.delete()

            file.createNewFile()
        }

        return file
    }

}