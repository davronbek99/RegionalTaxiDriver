package dev.davron.regionaltaxidriver.fragment.onlineRegistration.registerPassport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.FragmentRegisterPassportBinding
import dev.davron.regionaltaxidriver.utils.Common

class RegisterPassportFragment : Fragment() {

    private lateinit var binding: FragmentRegisterPassportBinding

    private var documentType = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterPassportBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnBackPressed()
        init()
        uiClickListener()
    }

    private fun init() {
        if (documentType != "") {
            binding.nextButton.isEnabled = true

            if (documentType == "passport") {
                binding.genderGroup.selectButton(R.id.passport_button)
            } else {
                binding.genderGroup.selectButton(R.id.id_card_button)
            }
        } else {
            if (Common.me != null) {
                documentType = Common.me?.content?.document_type ?: ""
            }

            if (documentType != "") {
                binding.nextButton.isEnabled = true

                if (documentType == "passport") {
                    binding.genderGroup.selectButton(R.id.passport_button)
                } else {
                    binding.genderGroup.selectButton(R.id.id_card_button)
                }
            }
        }
    }

    private fun uiClickListener() {
        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.genderGroup.setOnSelectListener {
            binding.nextButton.isEnabled = true

            if (it.id == R.id.id_card_button) {
                documentType = "id_card"
            } else {
                documentType = "passport"
            }
        }
//        binding.passportButton.setOnClickListener {
//            findNavController().navigate(R.id.to_information_passport)
//        }
//
//        binding.idCardButton.setOnClickListener {
//            findNavController().navigate(R.id.to_information_passport)
//        }

        binding.nextButton.setOnClickListener {
            Common.fromCheckPhotoFragment = false
            // open next page
            val bundle = Bundle()
            bundle.putString("type", documentType)
            Common.documentType = documentType
            findNavController().navigate(R.id.to_information_passport, bundle)
        }
    }

    private fun setOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback {
            findNavController().popBackStack()
        }
    }
}