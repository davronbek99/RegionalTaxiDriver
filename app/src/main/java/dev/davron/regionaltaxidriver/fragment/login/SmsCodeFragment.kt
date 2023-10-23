package dev.davron.regionaltaxidriver.fragment.login

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PowerManager
import android.provider.Telephony
import android.util.Log
import android.view.ActionMode
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import dev.davron.regionaltaxi.utils.broadcastReciever.SmsBroadcastReciever
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.SplashScreenActivity
import dev.davron.regionaltaxidriver.apiService.PhoneNumber
import dev.davron.regionaltaxidriver.databinding.FragmentSmsCodeBinding
import dev.davron.regionaltaxidriver.dialogs.exitDialog.ReallyYouWantToExit
import dev.davron.regionaltaxidriver.models.signIn.SignIn
import dev.davron.regionaltaxidriver.responseApis.ResApis
import dev.davron.regionaltaxidriver.utils.Common
import dev.davron.regionaltaxidriver.utils.Convertor.timer
import dev.davron.regionaltaxidriver.utils.MySharedPreferences
import dev.davron.regionaltaxidriver.utils.editLikePhoneNumber
import dev.davron.regionaltaxidriver.utils.setAnimations
import dev.davron.regionaltaxidriver.utils.showKeyboard
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

@SuppressLint("SimpleDateFormat")
@AndroidEntryPoint
class SmsCodeFragment : Fragment(), ReallyYouWantToExit.OnItemClickListener {
    private lateinit var binding: FragmentSmsCodeBinding

    private lateinit var sendCodeViewModel: SendCodeViewModel
    private var otpNumber = ""
    private lateinit var countDownTimer: CountDownTimer
    private var phoneNumber = ""
    private var from = "login"
    private var fToken = "-"
    private val df = SimpleDateFormat("mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        from = arguments?.getString("from_where") ?: "login"
        setAnimations()
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSmsCodeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        setViewModelListeners()

        uiClickListener()

        setOnBackPressed()

        //otp details...
        optViewDetails(view)

        setUpTimeDetails()

        setEditText()
    }

    private fun uiClickListener() {
        val token = MySharedPreferences.getToken(requireContext())
        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.signIn.setOnClickListener {
//            findNavController().navigate(R.id.to_full_info)

            Log.d("@@@", "uIOnItemClick: $otpNumber")
            if (otpNumber != "") {
                sendCodeViewModel.signIn(SignIn(phoneNumber, otpNumber.toString()))
                binding.loadingLayout.visibility = View.VISIBLE
                binding.layoutHead.alpha = 0.5f
                binding.layoutBottom.alpha = 0.5f
            } else {
                val lang = MySharedPreferences.getLang(requireContext())
                if (lang == "uz") {
                    Toast.makeText(requireContext(), "введите пароль", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Parol kiriting", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.otpView.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {}
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return false
            }
        }

        binding.tctReturn.setOnClickListener {
            binding.layoutHead.alpha = 0.4f
            binding.loadingLayout.visibility = View.VISIBLE

            if (from == "edit") {
                sendCodeViewModel.sendSmsCodeForUpdate(
                    token, phoneNumber.editLikePhoneNumber()
                )
            } else {
                sendCodeViewModel.sendCode(
                    PhoneNumber(phoneNumber.editLikePhoneNumber())
                )
            }

        }
    }

    private fun optViewDetails(view: View) {
        binding.otpView.setOtpCompletionListener {
            otpNumber = it
            view.hideKeyboard()

            sendCodeViewModel.signIn(SignIn(phoneNumber, otpNumber))
            binding.loadingLayout.visibility = View.VISIBLE
            binding.layoutHead.alpha = 0.5f
            binding.layoutBottom.alpha = 0.5f
        }
    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    @SuppressLint("SimpleDateFormat")
    private fun init() {
        sendCodeViewModel = ViewModelProvider(this)[SendCodeViewModel::class.java]

        //ui details...
        binding.tctReturn.visibility = View.GONE

        binding.errorTv.visibility = View.GONE

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                fToken = it.result
            }
        }

        binding.otpView.showKeyboard()

    }

    private fun setViewModelListeners() {
        sendCodeViewModel.verifyUpdatePhone.observe(requireActivity()) {
            when (it) {
                is ResApis.Error -> {
                    binding.errorTv.visibility = View.VISIBLE
                    binding.constraintRoot.alpha = 1f
                    binding.loadingLayout.visibility = View.GONE
                }

                is ResApis.Success -> {
                    if (this::countDownTimer.isInitialized) {
                        countDownTimer.cancel()
                    }

                    findNavController().popBackStack(R.id.to_full_info, false)
                }

                else -> {}
            }
        }

        sendCodeViewModel.sendCodeMutableData.observe(requireActivity()) {
            when (it) {
                is ResApis.Error -> {
                    binding.constraintRoot.alpha = 1f
                    binding.loadingLayout.visibility = View.GONE
                    binding.errorTv.visibility = View.VISIBLE
                }

                is ResApis.Success -> {
                    binding.constraintRoot.alpha = 1f
                    binding.loadingLayout.visibility = View.GONE
                    binding.errorTv.visibility = View.GONE
                    countDownTimer.start()
                }

                else -> {}
            }
        }


        sendCodeViewModel.signInMutableData.observe(requireActivity()) {
            when (it) {
                is ResApis.Error -> {
                    binding.errorTv.visibility = View.VISIBLE
                    binding.constraintRoot.alpha = 1f
                    binding.loadingLayout.visibility = View.GONE
                }

                is ResApis.Success -> {
                    Toast.makeText(requireContext(), "Success ga tushdi", Toast.LENGTH_SHORT).show()
                    if (this::countDownTimer.isInitialized) {
                        countDownTimer.cancel()
                    }

                    findNavController().navigate(R.id.to_full_info)

//                    Common.tempToken = "Bearer ${it.data.token}"

//                    if (it.data.registered) {
//                        MySharedPreferences.addToken(requireContext(), Common.tempToken)
//                        val intent = Intent(requireContext(), SplashScreenActivity::class.java)
//                        intent.putExtra("from_login", true)
//                        startActivity(intent)
//                        requireActivity().finishAffinity()
//                    } else {
//                        binding.constraintRoot.alpha = 1f
//                        binding.loadingLayout.visibility = View.GONE
//                        findNavController().navigate(R.id.to_full_info)
//                    }
                }

                else -> {}
            }
        }
    }

    private fun setUpTimeDetails() {
        countDownTimer = object : CountDownTimer(2 * 60 * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(p0: Long) {

                binding.timeTxt.isEnabled = false

                binding.timeTxt.text =
                    "${getString(R.string.request_new_code)} ${df.format(Date(p0))}"

                binding.timeTxt.setTextColor(
                    ResourcesCompat.getColor(
                        resources, R.color.time_color, activity?.theme
                    )
                )
            }

            override fun onFinish() {
                binding.timeTxt.text = getString(R.string.request_new_code)

                binding.timeTxt.isEnabled = true
                binding.otpView.setText("")
                binding.errorTv.visibility = View.GONE
                binding.timeTxt.setTextColor(
                    ResourcesCompat.getColor(
                        resources, R.color.main_blue, activity?.theme
                    )
                )
            }

        }
        countDownTimer.start()
    }

    @SuppressLint("SetTextI18n")
    private fun setEditText() {
        phoneNumber = arguments?.getString("phone") ?: ""

        binding.txtNumber.text = "${getString(R.string.write_sms_code_des)} $phoneNumber"


//        binding.otpView.setOnFocusChangeListener { view, b ->
//
//        }
        binding.otpView.showKeyboard()

        val token = MySharedPreferences.getToken(requireContext())

        binding.otpView.addTextChangedListener {
            val string = it.toString()
            if (string.length == 5) {

                binding.otpView.clearFocus()
                binding.otpView.hideKeyboard()

//                binding.constraintRoot.alpha = 0.4f
//                binding.loadingLayout.visibility = View.VISIBLE

                if (from == "edit") {
                    Toast.makeText(requireContext(), "A", Toast.LENGTH_SHORT).show()
                    sendCodeViewModel.verifyForUpdate(
                        token, phoneNumber.editLikePhoneNumber(), binding.otpView.text.toString()
                    )
                } else {
                    Toast.makeText(requireContext(), "B", Toast.LENGTH_SHORT).show()
                    sendCodeViewModel.signIn(
                        SignIn(phoneNumber.editLikePhoneNumber(), otpNumber))
                }
            } else {
            }
        }
    }

    override fun onCancelButtonClicked() {

    }

    override fun onExitButtonClicked() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }

    private fun setOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback {
            val dialog = ReallyYouWantToExit(requireContext(), this@SmsCodeFragment)
            dialog.show()
        }
    }
}