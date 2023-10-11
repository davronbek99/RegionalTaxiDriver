package dev.davron.regionaltaxidriver.fragment.onlineRegistration.registerTransport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.FragmentSelectTransportModelBinding

class SelectTransportModelFragment : Fragment() {

    private lateinit var binding: FragmentSelectTransportModelBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectTransportModelBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiClickListener()
    }

    private fun uiClickListener() {
        binding.titleTv.setOnClickListener {
            findNavController().navigate(R.id.to_select_transport_color)
        }
    }
}