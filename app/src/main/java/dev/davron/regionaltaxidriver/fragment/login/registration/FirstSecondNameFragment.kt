package dev.davron.regionaltaxidriver.fragment.login.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.FragmentFirstSecondNameBinding

class FirstSecondNameFragment : Fragment() {

    private lateinit var binding: FragmentFirstSecondNameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstSecondNameBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setTabLayout()
        uiClickListener()
    }

    private fun setTabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.tv_male)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.tv_female)))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
//                val tabBinding = ItemTabLayoutBinding.bind(tab?.customView!!)
//
//                tabBinding.root.background =
//                    ContextCompat.getDrawable(requireContext(), R.drawable.selected_tab_back)
//                tabBinding.secondaryNameTv.visibility = View.GONE
//                tabBinding.nameTv.visibility = View.VISIBLE
            }


            override fun onTabUnselected(tab: TabLayout.Tab?) {

//                val tabBinding = ItemTabLayoutBinding.bind(tab?.customView!!)
//                tabBinding.root.background = null
//                tabBinding.secondaryNameTv.visibility = View.VISIBLE
//                tabBinding.nameTv.visibility = View.GONE
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun uiClickListener() {
        binding.signIn.setOnClickListener {
            findNavController().navigate(R.id.to_add_profile_photo)
        }
    }

    private fun init() {

    }
}