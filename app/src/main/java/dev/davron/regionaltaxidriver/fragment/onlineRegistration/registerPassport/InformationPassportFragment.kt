package dev.davron.regionaltaxidriver.fragment.onlineRegistration.registerPassport

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.customViews.UploadingProgressBar.UploadingProgressBar
import dev.davron.regionaltaxidriver.databinding.FragmentInformationPassportBinding
import dev.davron.regionaltaxidriver.dialogs.exitDialog.ReallyYouWantToExit
import dev.davron.regionaltaxidriver.enums.Passport
import dev.davron.regionaltaxidriver.fragment.onlineRegistration.onlineRegistrationViewModel.OnlineRegistrationViewModel
import dev.davron.regionaltaxidriver.models.attachUpload.AttachUpload
import dev.davron.regionaltaxidriver.responseApis.ResApis
import dev.davron.regionaltaxidriver.utils.Common
import dev.davron.regionaltaxidriver.utils.MySharedPreferences
import dev.davron.regionaltaxidriver.utils.getBackStackData
import dev.davron.regionaltaxidriver.utils.getExtension
import dev.davron.regionaltaxidriver.utils.imageFileToBase64
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@AndroidEntryPoint
class InformationPassportFragment : Fragment() {

    private lateinit var binding: FragmentInformationPassportBinding
    private lateinit var viewModel: OnlineRegistrationViewModel
    private var documentType = "passport"
    private var photoFrontSide: File? = null
    private var photoResidenceSide: File? = null
    private var photoWithPerson: File? = null
    private var currentClickedPosition = ""

    private var isSerialNumberCompleted = false
    private var isFrontSide = false
    private var isResidenceSide = false
    private var isWithPersonSide = false

    private var isFrontSideCancelled = false
    private var isResidenceSideCancelled = false
    private var isWithPersonSideCancelled = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            documentType = it.getString("type", "Passport")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInformationPassportBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setViewModelListener()

        setOnBackPressed()
        setClickListeners()
        setUpEditText()
    }

    private fun setUpEditText() {

    }

    private fun setClickListeners() {
        binding.backButton.setOnClickListener {
            val reallyYouWantToExit = ReallyYouWantToExit(
                requireContext(),
                object : ReallyYouWantToExit.OnItemClickListener {
                    override fun onCancelButtonClicked() {

                    }

                    override fun onExitButtonClicked() {
                        Common.isPassportDone = false
                        findNavController().popBackStack(
                            R.id.registrationMainFragment, false
                        )
                    }
                })

            reallyYouWantToExit.show()
        }

        binding.readyButton.setOnClickListener {
            val token = MySharedPreferences.getToken(requireContext())
            viewModel.setPassportSerial(
                token, documentType, binding.seriNumberEt.unMasked
            )

            binding.layer.visibility = View.VISIBLE
        }

        binding.firstPositionFrontSide.setOnClickListener {
            currentClickedPosition = Passport.FRONT_SIDE.name

            val bundle = Bundle()
            bundle.putString("type", Passport.PASSPORT_TYPE.name)
            bundle.putString("side", Passport.FRONT_SIDE.name)
            findNavController().navigate(
                R.id.to_check_camera_photo, bundle
            )
        }

        binding.firstPositionResidenceSide.setOnClickListener {
            currentClickedPosition = Passport.RESIDENCE_SIDE.name

            val bundle = Bundle()
            bundle.putString("type", Passport.PASSPORT_TYPE.name)
            bundle.putString("side", Passport.RESIDENCE_SIDE.name)
            findNavController().navigate(
                R.id.to_check_camera_photo, bundle
            )
        }

    }


    private var frontSideUploadingProgressBar: UploadingProgressBar? = null
    private var residenceSideUploadingProgressBar: UploadingProgressBar? = null
    private var withPersonUploadingProgressBar: UploadingProgressBar? = null

    private fun init() {
        viewModel = ViewModelProvider(this)[OnlineRegistrationViewModel::class.java]

        if (documentType == "passport") {
            binding.titlePage.text = getString(R.string.passport)
        } else {
            binding.titlePage.text = getString(R.string.id_card)
        }

        if (Common.me != null) {
            binding.seriNumberEt.setText(Common.me?.content?.passport_serial ?: "")
            isSerialNumberCompleted = true
        }

        if (photoFrontSide != null) {
            if (isFrontSide) {
                binding.firstPositionFrontSide.visibility = View.GONE
                binding.readyFrontSide.visibility = View.VISIBLE
                binding.cancelImageFrontSide.visibility = View.VISIBLE
                binding.progressFrontSide.root.visibility = View.VISIBLE
                Glide.with(this).load(photoFrontSide).into(binding.imageFrontSide)
            } else {
                binding.firstPositionFrontSide.visibility = View.VISIBLE
                binding.cancelImageFrontSide.visibility = View.GONE
                binding.imageFrontSide.visibility = View.VISIBLE
                binding.imageFrontSide.alpha = 0.6f
                Glide.with(this).load(photoFrontSide).into(binding.imageFrontSide)
                frontSideUploadingProgressBar?.setLoadingDone()
            }
        } else {
            if (Common.me != null && Common.me?.content?.passport_copy1?.isNotEmpty() == true) {
                binding.firstPositionFrontSide.visibility = View.GONE
                binding.readyFrontSide.visibility = View.VISIBLE
                binding.cancelImageFrontSide.visibility = View.VISIBLE
                binding.progressFrontSide.root.visibility = View.GONE
                binding.imageFrontSide.visibility = View.VISIBLE
                Glide.with(this).load(Common.me?.content?.passport_copy1)
                    .placeholder(R.drawable.loading_image).centerCrop().into(binding.imageFrontSide)
                isFrontSide = true
            }
        }

        if (photoResidenceSide != null) {
            if (isResidenceSide) {
                binding.firstPositionResidenceSide.visibility = View.GONE
                binding.imageResidenceSide.visibility = View.VISIBLE
                binding.readyResidenceSide.visibility = View.VISIBLE
                binding.cancelImageResidenceSide.visibility = View.VISIBLE
                binding.progressResidenceSide.root.visibility = View.GONE
                Glide.with(this).load(photoResidenceSide).into(binding.imageResidenceSide)
            } else {
                binding.imageResidenceSide.visibility = View.VISIBLE
                binding.cancelImageResidenceSide.visibility = View.GONE
                binding.firstPositionResidenceSide.visibility = View.GONE
                binding.imageResidenceSide.alpha = 0.6f
                Glide.with(this).load(photoResidenceSide).into(binding.imageResidenceSide)
                residenceSideUploadingProgressBar?.setLoadingDone()
            }

        } else {
            if (Common.me != null && Common.me?.content?.passport_copy2?.isNotEmpty() == true) {
                binding.firstPositionResidenceSide.visibility = View.GONE
                binding.readyResidenceSide.visibility = View.VISIBLE
                binding.cancelImageResidenceSide.visibility = View.VISIBLE
                binding.imageResidenceSide.visibility = View.VISIBLE
                binding.progressResidenceSide.root.visibility = View.GONE
                Glide.with(this).load(Common.me?.content?.passport_copy2)
                    .placeholder(R.drawable.loading_image).centerCrop()
                    .into(binding.imageResidenceSide)
                isResidenceSide = true
            }
        }

        if (photoWithPerson != null) {
            if (isWithPersonSide) {
                binding.imageWithPersonSide.visibility = View.VISIBLE
                binding.firstPositionWithPersionSide.visibility = View.GONE
                binding.readyWithPersonSide.visibility = View.VISIBLE
                binding.cancelImageWithPersonSide.visibility = View.VISIBLE
                binding.progressWithPersonSide.root.visibility = View.GONE
                Glide.with(this).load(photoWithPerson).into(binding.imageWithPersonSide)
            } else {
                binding.imageWithPersonSide.visibility = View.VISIBLE
                binding.cancelImageWithPersonSide.visibility = View.GONE
                binding.firstPositionWithPersionSide.visibility = View.GONE
                binding.imageWithPersonSide.alpha = 0.6f
                Glide.with(this).load(photoWithPerson).into(binding.imageWithPersonSide)
                withPersonUploadingProgressBar?.setLoadingDone()
            }
        } else {
            if (Common.me != null && Common.me?.content?.passport_copy3?.isNotEmpty() == true) {
                binding.firstPositionWithPersionSide.visibility = View.GONE
                binding.readyWithPersonSide.visibility = View.VISIBLE
                binding.cancelImageWithPersonSide.visibility = View.VISIBLE
                binding.imageWithPersonSide.visibility = View.VISIBLE
                binding.progressWithPersonSide.root.visibility = View.GONE
                Glide.with(this).load(Common.me?.content?.passport_copy3)
                    .placeholder(R.drawable.loading_image).centerCrop()
                    .into(binding.imageWithPersonSide)
                isWithPersonSide = true
            }
        }

        checkButtonType()

        getBackStackData<File?>("photo", true) {
            if (it != null) {
                when (currentClickedPosition) {
                    Passport.FRONT_SIDE.name -> {
                        photoFrontSide = it

                        isFrontSide = false
                        checkButtonType()
                        binding.readyFrontSide.visibility = View.VISIBLE
                        binding.firstPositionFrontSide.visibility = View.GONE
                        Glide.with(this).load(it).into(binding.imageFrontSide)
                        binding.imageFrontSide.alpha = 0.6f
                        binding.cancelImageFrontSide.visibility = View.GONE

                        val token = MySharedPreferences.getToken(requireContext())

                        val requestFile = RequestBody.create(
                            MimeTypeMap.getSingleton().getMimeTypeFromExtension(it.getExtension())
                                ?.toMediaType(), it
                        )

                        var requestBody: RequestBody? = null

                        val photoBody =
                            MultipartBody.Part.createFormData("file", it.name, requestFile)
                        requestBody = RequestBody.create("file".toMediaType(), it)
                        viewModel.attachUpload(
                            requestFile
                        )

                        frontSideUploadingProgressBar = UploadingProgressBar(requireContext(),
                            binding.progressFrontSide.root,
                            object : UploadingProgressBar.OnCancelButtonClickListener {
                                override fun onUploadingFinished() {
                                    binding.firstPositionFrontSide.visibility = View.GONE
                                    binding.readyFrontSide.visibility = View.VISIBLE
                                    binding.cancelImageFrontSide.visibility = View.VISIBLE
                                    binding.imageFrontSide.alpha = 1f
                                    isFrontSide = true
                                    checkButtonType()
                                }

                                override fun onCancelClicked() {
                                    photoFrontSide = null
                                    binding.firstPositionFrontSide.visibility = View.VISIBLE
                                    binding.readyFrontSide.visibility = View.GONE
                                    frontSideUploadingProgressBar = null
                                    isFrontSideCancelled = true
                                    viewModel.setPassportPhoto1(token, "")
                                }
                            })
                    }

                    Passport.RESIDENCE_SIDE.name -> {
                        photoResidenceSide = it

                        binding.readyResidenceSide.visibility = View.VISIBLE
                        binding.firstPositionResidenceSide.visibility = View.GONE
                        Glide.with(this).load(it).into(binding.imageResidenceSide)
                        binding.imageResidenceSide.alpha = 0.6f
                        binding.cancelImageResidenceSide.visibility = View.GONE

                        isResidenceSide = false
                        checkButtonType()
                        val token = MySharedPreferences.getToken(requireContext())
                        viewModel.setPassportPhoto2(
                            token, it.imageFileToBase64()
                        )

                        residenceSideUploadingProgressBar = UploadingProgressBar(requireContext(),
                            binding.progressResidenceSide.root,
                            object : UploadingProgressBar.OnCancelButtonClickListener {
                                override fun onUploadingFinished() {
                                    binding.firstPositionResidenceSide.visibility = View.GONE
                                    binding.readyResidenceSide.visibility = View.VISIBLE
                                    binding.cancelImageResidenceSide.visibility = View.VISIBLE
                                    binding.imageResidenceSide.alpha = 1f
                                    isResidenceSide = true
                                    checkButtonType()
                                }

                                override fun onCancelClicked() {
                                    photoResidenceSide = null
                                    binding.firstPositionResidenceSide.visibility = View.VISIBLE
                                    binding.readyResidenceSide.visibility = View.GONE

                                    isResidenceSideCancelled = true
                                    viewModel.setPassportPhoto2(token, "")
                                    residenceSideUploadingProgressBar = null
                                }
                            })
                    }

                    Passport.WITH_PERSON_SIDE.name -> {
                        photoWithPerson = it

                        isWithPersonSide = false
                        checkButtonType()
                        binding.readyWithPersonSide.visibility = View.VISIBLE
                        binding.firstPositionWithPersionSide.visibility = View.GONE
                        Glide.with(this).load(it).into(binding.imageWithPersonSide)
                        binding.imageWithPersonSide.alpha = 0.6f
                        binding.cancelImageWithPersonSide.visibility = View.GONE

                        val token = MySharedPreferences.getToken(requireContext())
                        viewModel.setPassportPhoto3(
                            token, it.imageFileToBase64()
                        )

                        withPersonUploadingProgressBar = UploadingProgressBar(requireContext(),
                            binding.progressWithPersonSide.root,
                            object : UploadingProgressBar.OnCancelButtonClickListener {
                                override fun onUploadingFinished() {
                                    binding.firstPositionWithPersionSide.visibility = View.GONE
                                    binding.readyWithPersonSide.visibility = View.VISIBLE
                                    binding.cancelImageWithPersonSide.visibility = View.VISIBLE
                                    binding.imageWithPersonSide.alpha = 1f

                                    isWithPersonSide = true
                                    checkButtonType()
                                }

                                override fun onCancelClicked() {
                                    photoWithPerson = null
                                    binding.firstPositionWithPersionSide.visibility = View.VISIBLE
                                    binding.readyWithPersonSide.visibility = View.GONE
                                    withPersonUploadingProgressBar = null
                                    isWithPersonSideCancelled = true
                                    viewModel.setPassportPhoto3(token, "")
                                }
                            })
                    }

                }
            } else {
                Toast.makeText(requireContext(), "Error while capturing photo", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun checkButtonType() {
        binding.readyButton.isEnabled = isSerialNumberCompleted && isFrontSide
    }

    private fun setViewModelListener() {
        viewModel.attachUploadData.observe(requireActivity()) {
            when (it) {
                is ResApis.Error -> {
                    Toast.makeText(
                        requireContext(), it.message, Toast.LENGTH_SHORT
                    ).show()
                    Log.d("STATUS", it.message)
                    binding.firstPositionFrontSide.visibility = View.VISIBLE
                    binding.readyFrontSide.visibility = View.VISIBLE
                    binding.progressFrontSide.root.visibility = View.GONE
                }

                is ResApis.Success -> {
                    Log.d("STATUS", it.data.url!!)
                    if (isFrontSideCancelled) {
                        isFrontSide = false
                        isFrontSideCancelled = false
                    } else {
                        frontSideUploadingProgressBar?.setLoadingDone()
                    }

                }

                else -> {}
            }
        }
    }

    private fun setOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback {
            val reallyYouWantToExit = ReallyYouWantToExit(
                requireContext(),
                object : ReallyYouWantToExit.OnItemClickListener {
                    override fun onCancelButtonClicked() {

                    }

                    override fun onExitButtonClicked() {
                        Common.isPassportDone = false
                        findNavController().popBackStack(
                            R.id.registrationMainFragment, false
                        )
                    }
                })

            reallyYouWantToExit.show()
        }
    }
}