package dev.davron.regionaltaxidriver.fragment.onlineRegistration.registerTransport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.FragmentManaFacturedYearFragmentBinding

class ManufacturedYearFragmentFragment : Fragment() {

    private lateinit var binding: FragmentManaFacturedYearFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManaFacturedYearFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiClickListener()
    }

    private fun uiClickListener() {

    }


}