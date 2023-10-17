package com.example.crudsops.ui.mainApp.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crudsops.data.Item
import com.example.crudsops.databinding.ItemViewBinding

class ItemRecyclerAdapter(
    val items: ArrayList<Item>,
    var callBackListener: CallBackListener
) :
    RecyclerView.Adapter<ItemRecyclerAdapter.ItemsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemsViewHolder {
        return ItemsViewHolder(
            ItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        if (items.size == 0) {
            return 0
        }
        return items.size
    }


    inner class ItemsViewHolder(private val binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.tvTitle.text = item.title
            binding.ivDelete.setOnClickListener {
                callBackListener.onItemRemoveClicked(item)
            }
            binding.content.setOnClickListener {
                callBackListener.onItemClicked(item)
            }
        }

    }

    interface CallBackListener {
        fun onItemClicked(item: Item)
        fun onItemRemoveClicked(item: Item)
    }
}