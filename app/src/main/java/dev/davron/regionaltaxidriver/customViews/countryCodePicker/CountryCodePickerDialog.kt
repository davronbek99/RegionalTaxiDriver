package dev.davron.regionaltaxidriver.customViews.countryCodePicker

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.CountryCodePickerDialogBinding
import dev.davron.regionaltaxidriver.models.login.CountryCode

class CountryCodePickerDialog(
    context: Context,
    val list: ArrayList<CountryCode>,
    val onItemClickListener: RvAdapter.OnItemClickListener
) :
    BottomSheetDialog(context, R.style.SheetDialog) {
    private val binding = CountryCodePickerDialogBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
        val adapter = RvAdapter(list, object : RvAdapter.OnItemClickListener {
            override fun onItemClicked(item: CountryCode) {
                dismiss()
                onItemClickListener.onItemClicked(item)
            }
        })

        binding.rv.adapter = adapter

        binding.closeButton.setOnClickListener {
            dismiss()
        }

    }
}