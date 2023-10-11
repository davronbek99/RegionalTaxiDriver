package dev.davron.regionaltaxidriver.dialogs.exitDialog

import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.ReallyWantToExitBinding

class ReallyYouWantToExit(context: Context, val onItemClickListener: OnItemClickListener) :
    BottomSheetDialog(context, R.style.SheetDialog) {

    private val binding = ReallyWantToExitBinding.inflate(LayoutInflater.from(context))

    init {

        setContentView(binding.root)

        binding.cancelButton.setOnClickListener {
            dismiss()
            onItemClickListener.onCancelButtonClicked()
        }

        binding.exitButton.setOnClickListener {
            dismiss()
            onItemClickListener.onExitButtonClicked()
        }
    }

    interface OnItemClickListener {
        fun onCancelButtonClicked()
        fun onExitButtonClicked()
    }
}