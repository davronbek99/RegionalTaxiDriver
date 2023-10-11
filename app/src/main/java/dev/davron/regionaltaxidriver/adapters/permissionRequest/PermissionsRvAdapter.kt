package dev.davron.regionaltaxidriver.adapters.permissionRequest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.ItemPermissionsRvBinding
import dev.davron.regionaltaxidriver.models.permissionRequest.PermissionModel

class PermissionsRvAdapter(
    val list: ArrayList<PermissionModel>,
    val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PermissionsRvAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(permission: PermissionModel, position: Int)
    }

    inner class MyViewHolder(val itemPermissionsRvBinding: ItemPermissionsRvBinding) :
        RecyclerView.ViewHolder(itemPermissionsRvBinding.root) {

        fun onBind(item: PermissionModel, position: Int) {
            itemPermissionsRvBinding.apply {
                permissionName.text = item.name
                permissionDescription.text = item.description

                root.setOnClickListener {
                    onItemClickListener.onItemClick(item, position)
                }


                if (item.granted) {
                    checkImageview.setImageResource(R.drawable.ic_permission_granted)
                } else {
                    checkImageview.setImageResource(R.drawable.ic_permission_not_granted)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemPermissionsRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}