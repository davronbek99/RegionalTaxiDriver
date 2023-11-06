package dev.davron.regionaltaxidriver.fragment.mainActivity.oddFragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dev.davron.regionaltaxidriver.BuildConfig
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.FragmentCheckCameraPhotoBinding
import dev.davron.regionaltaxidriver.enums.DriverLicense
import dev.davron.regionaltaxidriver.enums.Passport
import dev.davron.regionaltaxidriver.enums.TexPassport
import dev.davron.regionaltaxidriver.enums.Transport
import dev.davron.regionaltaxidriver.utils.Common
import dev.davron.regionaltaxidriver.utils.getBackStackData
import dev.davron.regionaltaxidriver.utils.setBackStackData
import java.io.File
import java.io.IOException

class CheckCameraPhotoFragment : Fragment() {

    private lateinit var binding: FragmentCheckCameraPhotoBinding
    private lateinit var documentType: String
    private lateinit var side: String
    private var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            documentType = it.getString("type", "")
            side = it.getString("side", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckCameraPhotoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnBackPressed()
        setClickListeners()
        init()

    }

    private fun init() {
        when (side) {
            Passport.FRONT_SIDE.name -> {
                binding.titleTv.text = getString(R.string.face_part_photo_document)
                if (Common.documentType == "passport") {
                    binding.eskizImg.setImageResource(R.drawable.passpport_front)
                } else {
                    binding.eskizImg.setImageResource(R.drawable.id_card_front)
                }
            }

            Passport.RESIDENCE_SIDE.name -> {
                binding.titleTv.text = getString(R.string.with_place_id_photo_document)
                if (Common.documentType == "passport") {
                    binding.eskizImg.setImageResource(R.drawable.passport_back)
                } else {
                    binding.eskizImg.setImageResource(R.drawable.id_card_back)
                }
            }

            Passport.WITH_PERSON_SIDE.name -> {
                binding.titleTv.text = getString(R.string.person_with_document)
            }

            DriverLicense.BACK_SIDE_LICENSE.name -> {
                binding.titleTv.text = getString(R.string.back_side)
                binding.eskizImg.setImageResource(R.drawable.driver_license_back)
            }

            DriverLicense.FRONT_SIDE_LICENSE.name -> {
                binding.titleTv.text = getString(R.string.back_side)
                binding.eskizImg.setImageResource(R.drawable.driver_license_fron)
            }

            DriverLicense.FRONT_SIDE_LICENSE.name -> {
                binding.titleTv.text = getString(R.string.face_part_photo_document)
                binding.eskizImg.setImageResource(R.drawable.driver_license_back)
            }

            TexPassport.FRONT_SIDE_TEX.name -> {
                binding.titleTv.text = getString(R.string.face_part_photo_document)
                binding.eskizImg.setImageResource(R.drawable.tex_passport_front)
            }

            TexPassport.BACK_SIDE_TEX.name -> {
                binding.titleTv.text = getString(R.string.back_side)
                binding.eskizImg.setImageResource(R.drawable.tex_passport_back)
            }

            Transport.FRONT_SIDE_CAR.name -> {
                binding.titleTv.text = getString(R.string.front_side_auto)
                binding.eskizImg.setImageResource(R.drawable.car_front)
            }

            Transport.FIRST_LINE_CAR.name -> {
                binding.titleTv.text = getString(R.string.first_line_salon)
                binding.eskizImg.setImageResource(R.drawable.car_first_line)
            }

            Transport.BACK_LINE_CAR.name -> {
                binding.titleTv.text = getString(R.string.back_line_salon)
                binding.eskizImg.setImageResource(R.drawable.car_back_line)
            }

            Transport.BAGGAGE_CAR.name -> {
                binding.titleTv.text = getString(R.string.baggage)
                binding.eskizImg.setImageResource(R.drawable.car_baggage)
            }

            Transport.RIGHT_SIDE_CAR.name -> {
                binding.titleTv.text = getString(R.string.right_auto)
                binding.eskizImg.setImageResource(R.drawable.car_right)
            }

            Transport.BACK_SIDE_CAR.name -> {
                binding.eskizImg.setImageResource(R.drawable.car_back)
                binding.titleTv.text = getString(R.string.back_side_auto)
            }

            Transport.LEFT_SIDE_CAR.name -> {
                binding.titleTv.text = getString(R.string.left_side_auto)
                binding.eskizImg.setImageResource(R.drawable.car_left)
            }

        }

        getBackStackData<Boolean>("success", true) {
            if (it) {
                binding.resultButtons.visibility = View.VISIBLE
                binding.takePhotoButton.visibility = View.GONE
                binding.imageView.visibility = View.VISIBLE
                binding.phoneEdge.visibility = View.GONE

                val uri = (if (photoFile != null) {
                    photoFile
                } else {
                    File("")
                })?.let { file ->
                    try {
                        FileProvider.getUriForFile(
                            requireContext(),
                            BuildConfig.APPLICATION_ID,
                            file
                        )
                    } catch (e: Exception) {
                        //here
                        File("")
                    }
                }

                Glide.with(this).load(uri).into(binding.imageView)
            }
        }
    }

    private fun setClickListeners() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.takePhotoButton.setOnClickListener {
            if (requireContext().checkCallingOrSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                addPhotoFromCamera()
            } else {
                requestCameraPermission()
            }
        }

        binding.retakeButton.setOnClickListener {
            binding.takePhotoButton.performClick()
        }

        binding.saveButton.setOnClickListener {
            binding.saveButton.text = ""
            binding.saveButton.alpha = 0.4f
            binding.saveProgress.visibility = View.VISIBLE
            setBackStackData("photo", photoFile, true)
        }
    }

    private fun addPhotoFromCamera() {
        photoFile = try {
            createImageFile()
        } catch (e: Exception) {
            null
        }
        photoFile?.also {
            val uri = FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID,
                it
            )

        }

        val bundle = Bundle()
        bundle.putSerializable("file", photoFile)
        findNavController().navigate(R.id.to_camera_fragment2, bundle)
    }

    private fun requestCameraPermission() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) { /* ... */
                    addPhotoFromCamera()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) { /* ... */
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) { /* ... */
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val m = System.currentTimeMillis()
        val externalFilesDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val createTempFile = File.createTempFile("$documentType$side", ".jpg", externalFilesDir)
        return createTempFile
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Common.fromCheckPhotoFragment = true
    }

    private fun setOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback {
            findNavController().popBackStack()
        }
    }
}