package dev.davron.regionaltaxidriver.fragment.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.apiService.PhoneNumber
import dev.davron.regionaltaxidriver.customViews.countryCodePicker.CountryCodePicker
import dev.davron.regionaltaxidriver.databinding.FragmentPhoneBinding
import dev.davron.regionaltaxidriver.modelApi.loginActivity.ResCommon
import dev.davron.regionaltaxidriver.models.login.CountryCode
import dev.davron.regionaltaxidriver.responseApis.ResApis
import dev.davron.regionaltaxidriver.utils.MySharedPreferences
import dev.davron.regionaltaxidriver.utils.setAnimations
import dev.davron.regionaltaxidriver.utils.showKeyboard

@AndroidEntryPoint
class PhoneFragment : Fragment(), CountryCodePicker.OnCountryChangeListener {

    private lateinit var binding: FragmentPhoneBinding
    private lateinit var countryCodePicker: CountryCodePicker
    private lateinit var countryCode: CountryCode
    private lateinit var sendCodeViewModel: SendCodeViewModel
    private var from = "login"
    private var phone = ""
    var isFirst = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            from = it.getString("from_where", "login")
            phone = it.getString("phone", "")
        }
        setAnimations()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoneBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnBackPressed()
        init()
        setOnClickListener()
        setViewModelListeners()
        setUpPhoneEdittextElements()
    }

    private var lang: String = ""

    @SuppressLint("SetTextI18n")
    private fun init() {
        sendCodeViewModel = ViewModelProvider(this)[SendCodeViewModel::class.java]

        binding.txtPhone.setText("+998")
        binding.txtPhone.showKeyboard()
//        binding.errorTv.visibility = View.GONE
//        countryCodePicker = CountryCodePicker(binding.countryCodeCard.root, requireContext(), this)

//        if (from == "edit") {
//            binding.txtPhone.setText(phone)
//            binding.signIn.isEnabled = true
//        }


    }

    private fun setViewModelListeners() {
        sendCodeViewModel.sendCodeMutableData.observe(requireActivity(), observerSendCode)
    }

    private fun setOnClickListener() {
        val token = MySharedPreferences.getToken(requireContext())

        isFirst = true

        binding.signIn.setOnClickListener {
            val phone = binding.txtPhone.masked.replace(" ", "").replace("(", "").replace(")", "")
            if (phone.length == 13) {
                createUser(phone)
                binding.loader.visibility = View.VISIBLE
                binding.layoutHead.alpha = 0.5f
                binding.signIn.alpha = 0.5f
            } else {
                val text =
                    if (lang == "uz") "Telefon raqamni to'liq kiriting" else "Введите номер телефона полностью"
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
            }

        }

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.txtPhone.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! >= 19) {
                    view?.hideKeyboard()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun setUpPhoneEdittextElements() {
//        binding.txtPhone.setOnFocusChangeListener { view, b ->
//            binding.bottomLineCode.isHovered = b
//            binding.bottomLinePhoneNumber.isHovered = b
//        }
//
//        binding.phoneNumberEdt.addTextChangedListener {
//            if (it != null) {
//                val string = it.toString()
//
//                if (string.length == countryCode.mask.length) {
//                    binding.phoneNumberEdt.hideKeyboard()
//                    binding.phoneNumberEdt.clearFocus()
//                    binding.nextButton.isEnabled = true
//                } else {
//                    binding.nextButton.isEnabled = false
//                }
//            }
//        }
    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun createUser(number: String) {
        sendCodeViewModel.sendCode(PhoneNumber(number))
    }

    private fun setOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback {
            findNavController().popBackStack()
        }
    }

    override fun onCountryChange(countryCode: CountryCode) {
        this.countryCode = countryCode

//        val mask = Mask(
//            countryCode.mask,
//            '_'
//        )
//        val listener = MaskChangedListener(mask)
//        binding.phoneNumberEdt.addTextChangedListener(listener)
//        binding.phoneNumberEdt.hint = countryCode.mask.replace("_", "0")
//        binding.phoneNumberEdt.setText("")
    }

    private val observerSendCode = Observer<ResApis<ResCommon>> {
        when (it) {
            is ResApis.Error -> {
                //response is not successful
//                    if (it.message.contains("try after a while")) {
//                        binding.errorTv.text = getString(R.string.try_after_while)
//                    } else {
//                        binding.errorTv.text = getString(R.string.error_occurred)
//                    }
                binding.layer.visibility = View.GONE
//                    binding.errorTv.visibility = View.VISIBLE
            }

            is ResApis.Success -> {
                //response is success
                binding.layer.visibility = View.GONE
                Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                val bundle = Bundle()
                bundle.putString(
                    "phone",
                    binding.txtPhone.masked
                )
                findNavController().navigate(R.id.to_sms_code, bundle)

                sendCodeViewModel.sendCodeMutableData.postValue(ResApis.Loading())
            }

            else -> {
//                Toast.makeText(requireContext(), "Else", Toast.LENGTH_SHORT).show()
            }
        }
    }
}