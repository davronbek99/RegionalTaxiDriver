package dev.davron.regionaltaxidriver.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.ErrorOccurredDialogBinding

@SuppressLint("SetTextI18n")
class AutoClickerDialog(context: Context) :
    BottomSheetDialog(context, R.style.SheetDialog) {

    private val binding = ErrorOccurredDialogBinding.inflate(LayoutInflater.from(context))


    init {
        setContentView(binding.root)
        binding.title.text="AutoClicker"
        binding.tariffTv.text = context.getString(R.string.autoclicker_des)
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }
}