package dev.davron.regionaltaxidriver.fragment.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import dev.davron.regionaltaxi.utils.broadcastReciever.SmsBroadcastReciever
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.ActivityOtpViewBinding
import dev.davron.regionaltaxidriver.service.SmsBroadcastReceiver

class OtpViewActivity : AppCompatActivity(), SmsBroadcastReceiver.OnSmsReceived {
    private lateinit var binding:ActivityOtpViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOtpViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    }

//    private fun optViewDetails(view: View) {
//        binding.otpView.setOtpCompletionListener {
//            otpNumber = it
//            view.hideKeyboard()
//
//            viewModel.loginUser(phone, otpNumber)
//            binding.loader.visibility = View.VISIBLE
//            binding.layoutHead.alpha = 0.5f
//            binding.layotBottom.alpha = 0.5f
//        }
//    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onSmsCodeReceived(code: String) {
        binding.otpView.setText(code)
    }


}