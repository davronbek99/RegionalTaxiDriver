package dev.davron.regionaltaxidriver.customViews.countryCodePicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.davron.regionaltaxidriver.databinding.ItemSelectCountryViewRvBinding
import dev.davron.regionaltaxidriver.models.login.CountryCode

class RvAdapter(val list: ArrayList<CountryCode>, val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<RvAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(item: CountryCode)
    }

    inner class MyViewHolder(val itemSelectCountryViewRvBinding: ItemSelectCountryViewRvBinding) :
        RecyclerView.ViewHolder(itemSelectCountryViewRvBinding.root) {
        fun onBind(item: CountryCode) {
            itemSelectCountryViewRvBinding.apply {
                codeTv.text = item.code
                codeTv.visibility = View.VISIBLE

                tv.text = item.name

                tv.setCompoundDrawablesWithIntrinsicBounds(item.flagResource, 0, 0, 0)

                root.setOnClickListener {
                    onItemClickListener.onItemClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemSelectCountryViewRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}