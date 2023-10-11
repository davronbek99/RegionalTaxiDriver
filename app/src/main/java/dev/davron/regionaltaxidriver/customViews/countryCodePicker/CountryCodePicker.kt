package dev.davron.regionaltaxidriver.customViews.countryCodePicker

import android.content.Context
import android.view.View
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.CountryCodePickBinding
import dev.davron.regionaltaxidriver.models.login.CountryCode

data class CountryCodePicker(
    private val countryView: View,
    private val context: Context,
    private val countryChangeListener: OnCountryChangeListener
) {

    init {
        val binding = CountryCodePickBinding.bind(countryView)
        val countriesList = loadCountries()
        binding.codeCard.setOnClickListener {
            val countryCodePickerDialog = CountryCodePickerDialog(
                context,
                loadCountries(),
                object : RvAdapter.OnItemClickListener {
                    override fun onItemClicked(item: CountryCode) {
                        countryChangeListener.onCountryChange(item)
                        binding.flag.setImageResource(item.flagResource)
                        binding.codeTv.text = item.code
                    }
                })

            countryCodePickerDialog.show()

        }


        countryChangeListener.onCountryChange(countriesList.first())
    }

    private fun loadCountries(): ArrayList<CountryCode> {
        val list = ArrayList<CountryCode>()

//        998 (93) 944-43-21
        list.add(
            CountryCode(
                R.drawable.ic_uzbekistan_flag,
                "+998",
                "(__) ___-__-__",
                context.getString(R.string.uzbekistan)
            )
        )

//        7 (920) 111-22-33
        list.add(
            CountryCode(
                R.drawable.ic_russian_flag,
                "+7",
                "(___) ___-__-__",
                context.getString(R.string.russia)
            )
        )


        //        7 000 000 0000 kazakhstan
        list.add(
            CountryCode(
                R.drawable.ic_kazakhstan_flag,
                "+7",
                "(___) ___-____",
                context.getString(R.string.kazakhstan)
            )
        )


        list.add(
            CountryCode(
                R.drawable.ic_afghanistan_flag,
                "+793",
                "(___) ___-___",
                context.getString(R.string.afghanistan)
            )
        )

        list.add(
            CountryCode(
                R.drawable.ic_kyrgyzstan_flag,
                "+996",
                "(___) ______",
                context.getString(R.string.kyrgyzstan)
            )
        )

        list.add(
            CountryCode(
                R.drawable.ic_tajikistan_flag,
                "+992",
                "(__) ___ ____",
                context.getString(R.string.tajikistan)
            )
        )


        return list
    }


    interface OnCountryChangeListener {
        fun onCountryChange(countryCode: CountryCode)
    }
}
