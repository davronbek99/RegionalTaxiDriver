package dev.davron.regionaltaxidriver.fragment.onlineRegistration.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.FragmentRegistrationMainBinding

class RegistrationMainFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        uiClickListener()
    }

    private fun uiClickListener() {
        binding.passportIdCard.setOnClickListener {
            findNavController().navigate(R.id.to_register_passport)
        }
        binding.driverLicenseRegister.setOnClickListener {
            findNavController().navigate(R.id.to_register_driver_license)
        }
        binding.selectTransport.setOnClickListener {
            findNavController().navigate(R.id.to_select_transport_manufactured)
        }
        binding.registerTexPassport.setOnClickListener {
            findNavController().navigate(R.id.to_number_of_transport)
        }
        binding.photoTransport.setOnClickListener {
            findNavController().navigate(R.id.to_transport_photo)
        }
    }
}