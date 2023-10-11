package dev.davron.regionaltaxidriver.fragment.onlineRegistration.registerDriverLicense

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.FragmentRegisterDriverLicenseBinding


class RegisterDriverLicenseFragment : Fragment() {

    private lateinit var binding: FragmentRegisterDriverLicenseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterDriverLicenseBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        uiClickListener()
    }

    private fun uiClickListener() {
        binding.nextButton.setOnClickListener {
            findNavController().navigate(R.id.to_driver_license_photo)
        }
    }
}