package dev.davron.regionaltaxidriver.fragment.login.registration

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.davron.regionaltaxidriver.OnlineRegistrationActivity
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.FragmentAddProfilePhotoBinding

class AddProfilePhotoFragment : Fragment() {

    private lateinit var binding: FragmentAddProfilePhotoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProfilePhotoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        uiClickListener()
    }

    private fun uiClickListener() {
        binding.skipButton.setOnClickListener {
            val intent = Intent(activity, OnlineRegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}