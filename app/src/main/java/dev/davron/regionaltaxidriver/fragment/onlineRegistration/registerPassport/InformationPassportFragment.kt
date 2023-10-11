package dev.davron.regionaltaxidriver.fragment.onlineRegistration.registerPassport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.FragmentInformationPassportBinding

class InformationPassportFragment : Fragment() {

    private lateinit var binding: FragmentInformationPassportBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInformationPassportBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}