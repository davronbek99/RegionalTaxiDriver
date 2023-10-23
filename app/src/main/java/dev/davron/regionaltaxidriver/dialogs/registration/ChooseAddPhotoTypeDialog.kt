package dev.davron.regionaltaxidriver.dialogs.registration

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.AddPhotoChooseTypeDialogBinding

class ChooseAddPhotoTypeDialog(
    context: Context, private val onItemClickListener: OnItemClickListener
) : BottomSheetDialog(context, R.style.SheetDialog) {

    private val binding = AddPhotoChooseTypeDialogBinding.inflate(LayoutInflater.from(context))

    init {
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.cameraButton.setOnClickListener {
            dismiss()
            onItemClickListener.onCameraSelected()
        }

        binding.galleryButton.setOnClickListener {
            dismiss()
            onItemClickListener.onGallerySelected()
        }

    }

    interface OnItemClickListener {
        fun onGallerySelected()
        fun onCameraSelected()
    }
}