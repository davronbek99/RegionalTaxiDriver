package dev.davron.regionaltaxidriver.adapters.orders

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.ItemOrderPostBinding
import dev.davron.regionaltaxidriver.modelApi.activeOrder.activePostOrder.Content
import dev.davron.regionaltaxidriver.utils.MainFun
import java.lang.StringBuilder

class OrderPostAdapter(
    var isTheme: Boolean,
    var context: Context,
    var ordersList: List<Content>,
    var onLastItemPosition: AdapterView.OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemClickListener: ((Int) -> Unit)? = null
    fun setItemClickListener(f: (id: Int) -> Unit) {
        itemClickListener = f
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            ItemOrderPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {

            holder.binding.txtTitle.text =
                "${ordersList[position].title} № ${ordersList[position].id}"
            holder.binding.txtFrom.text = ordersList[position].from


            val builder = StringBuilder()
            val places = ordersList[position].places
            if (places.component1() != "0.00") {
                builder.append(
                    "#1 - ${
                        places.component1().toDouble().toInt()
                    }  ${MainFun.langPrice(context)}"
                )
                holder.binding.viewFirst.setBackgroundResource(R.drawable.ic_place_green)
            } else {
                holder.binding.viewFirst.setBackgroundResource(R.drawable.ic_place_darck_gray)
            }

            if (places.component2() != "0.00") {

                builder.append(
                    "\n#2 - ${
                        places.component2().toDouble().toInt()
                    }  ${MainFun.langPrice(context)}"
                )

                holder.binding.viewSecond.setBackgroundResource(R.drawable.ic_place_green)
            } else {
                holder.binding.viewSecond.setBackgroundResource(R.drawable.ic_place_darck_gray)
            }

            if (places.component3() != "0.00") {
                builder.append(
                    "\n#3 - ${
                        places.component3().toDouble().toInt()
                    }  ${MainFun.langPrice(context)}"
                )
                holder.binding.viewThree.setBackgroundResource(R.drawable.ic_place_green)
            } else {
                holder.binding.viewThree.setBackgroundResource(R.drawable.ic_place_darck_gray)
            }

            if (places.component4() != "0.00") {
                builder.append(
                    "\n#4 - ${
                        places.component4().toDouble().toInt()
                    }  ${MainFun.langPrice(context)}"
                )
                holder.binding.viewFour.setBackgroundResource(R.drawable.ic_place_green)
            } else {
                holder.binding.viewFour.setBackgroundResource(R.drawable.ic_place_darck_gray)
            }

            if (ordersList[position].has_overhead_luggage) {
                holder.binding.layoutLuggage.visibility = View.VISIBLE
            } else {
                holder.binding.layoutLuggage.visibility = View.GONE
            }


            if (builder.toString() != "") {
                holder.binding.txtNumber.text = builder.toString()
            } else {
                holder.binding.txtNumber.text = "${
                    MainFun.sortNum(
                        ordersList[position].amount.toDouble().toInt()
                    )
                } ${MainFun.langPrice(context)}"
            }

            holder.binding.layoutHead.setOnClickListener {
                itemClickListener?.invoke(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    class MyViewHolder(var binding: ItemOrderPostBinding) : RecyclerView.ViewHolder(binding.root)
}