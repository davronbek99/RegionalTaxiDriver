package dev.davron.regionaltaxidriver.adapters.orders

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.davron.regionaltaxidriver.modelApi.activeOrder.activeTaxiOrder.Content
import dev.davron.regionaltaxidriver.utils.MainFun
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.ItemOrderBinding
import dev.davron.regionaltaxidriver.utils.OnItemClickListener
import java.lang.StringBuilder

class OrdersAdapter(
    var isTheme: Boolean,
    var context: Context,
    var ordersList: List<Content>,
    var onLastItemPosition: OnItemClickListener
) :
    RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {
    private var itemClickListener: ((Int) -> Unit)? = null
    fun setItemClickListener(f: (id: Int) -> Unit) {
        itemClickListener = f
    }

    inner class OrdersViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindData(data: Content) {

            if (data.has_overhead_luggage) {
                binding.layoutLuggage.visibility = View.VISIBLE
            } else {
                binding.layoutLuggage.visibility = View.GONE
            }

            if (data.has_conditioner) {
                binding.layoutHasConditioner.visibility = View.VISIBLE
            } else {
                binding.layoutHasConditioner.visibility = View.GONE
            }

            binding.txtFrom.text = data.from
            binding.txtTitle.text = "${data.title} № ${data.id}"

            val stringBuilder = StringBuilder()


            if (data.places.component1() != "0.00") {
                stringBuilder.append(
                    "#1 - ${
                        MainFun.sortNum(
                            data.places.component1().toDouble().toInt()
                        )
                    } ${MainFun.langPrice(context)}"
                )
                binding.viewFirst.setBackgroundResource(R.drawable.ic_place_green)
            } else {
                binding.viewFirst.setBackgroundResource(R.drawable.ic_place_darck_gray)
            }

            if (data.places.component2() != "0.00") {
                stringBuilder.append(
                    "\n#2 - ${
                        MainFun.sortNum(
                            data.places.component2().toDouble().toInt()
                        )
                    } ${MainFun.langPrice(context)}"
                )
                binding.viewSecond.setBackgroundResource(R.drawable.ic_place_green)
            } else {
                binding.viewSecond.setBackgroundResource(R.drawable.ic_place_darck_gray)
            }
            if (data.places.component3() != "0.00") {
                stringBuilder.append(
                    "\n#3 - ${
                        MainFun.sortNum(
                            data.places.component3().toDouble().toInt()
                        )
                    } ${MainFun.langPrice(context)}"
                )
                binding.viewThree.setBackgroundResource(R.drawable.ic_place_green)
            } else {
                binding.viewThree.setBackgroundResource(R.drawable.ic_place_darck_gray)
            }
            if (data.places.component4() != "0.00") {
                stringBuilder.append(
                    "\n#4 - ${
                        MainFun.sortNum(
                            data.places.component4().toDouble().toInt()
                        )
                    } ${MainFun.langPrice(context)}"
                )
                binding.viewFour.setBackgroundResource(R.drawable.ic_place_green)
            } else {
                binding.viewFour.setBackgroundResource(R.drawable.ic_place_darck_gray)
            }


            binding.txtPrice.text = stringBuilder.toString()
            if (stringBuilder.toString() != "") {
                binding.txtPrice.text = stringBuilder.toString()
            } else {
                binding.txtPrice.text = "${
                    MainFun.sortNum(
                        data.amount.toDouble().toInt()
                    )
                } ${MainFun.langPrice(context)}"
            }
            binding.root.setOnClickListener {
                itemClickListener?.invoke(layoutPosition)
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder =
        OrdersViewHolder(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        onLastItemPosition.onItemClickListener(position)
        holder.bindData(ordersList[position])
    }


    override fun getItemCount(): Int = ordersList.size

}