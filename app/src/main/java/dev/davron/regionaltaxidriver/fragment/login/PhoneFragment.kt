package dev.davron.regionaltaxidriver.fragment.login

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
import androidx.navigation.fragment.findNavController
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.FragmentPhoneBinding

class PhoneFragment : Fragment() {

    private lateinit var binding: FragmentPhoneBinding

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

        init()
        setOnClickListener()
    }

    private var lang: String = ""

    private fun init() {

    }

    private fun setOnClickListener() {
        binding.signIn.setOnClickListener {
            val phone = binding.txtPhone.masked.replace(" ", "").replace("(", "").replace(")", "")
            if (phone.length == 13) {
                createUser(phone)
                binding.loader.visibility = View.VISIBLE
                binding.layoutHead.alpha = 0.5f
                binding.signIn.alpha = 0.5f
                findNavController().navigate(R.id.to_sms_code)
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

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun createUser(number: String) {
        // Toast.makeText(requireContext(), number, Toast.LENGTH_SHORT).show()
//        viewModel.createUser(number)
    }
}