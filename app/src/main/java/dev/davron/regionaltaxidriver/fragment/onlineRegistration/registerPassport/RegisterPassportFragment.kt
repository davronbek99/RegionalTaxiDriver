package dev.davron.regionaltaxidriver.fragment.onlineRegistration.registerPassport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.FragmentRegisterPassportBinding

class RegisterPassportFragment : Fragment() {

    private lateinit var binding: FragmentRegisterPassportBinding

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

        uiClickListener()
    }

    private fun uiClickListener() {
        binding.passportButton.setOnClickListener {
            findNavController().navigate(R.id.to_information_passport)
        }

        binding.idCardButton.setOnClickListener {
            findNavController().navigate(R.id.to_information_passport)
        }
    }
}