package dev.davron.regionaltaxidriver.fragment.login.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.FragmentFirstSecondNameBinding
import dev.davron.regionaltaxidriver.models.login.fullName.RequestFullName

@AndroidEntryPoint
class FirstSecondNameFragment : Fragment() {

    private lateinit var binding: FragmentFirstSecondNameBinding

    //    private lateinit var personalInformationViewModel: PersonalInformationViewModel
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
        setViewModelListeners()
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
            val bundle = Bundle()
            bundle.putSerializable(
                "requestFullName",
                RequestFullName(
                    binding.firstname.text.toString(),
                    binding.lastname.text.toString(),
                    binding.dateOfBirth.text.toString(),
                    ""
                )
            )
            findNavController().navigate(R.id.to_add_profile_photo, bundle)
        }
    }

    private fun init() {
//        personalInformationViewModel =
//            ViewModelProvider(this)[PersonalInformationViewModel::class.java]
    }

    private fun setViewModelListeners() {

    }
}