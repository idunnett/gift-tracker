package com.example.gifttracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gifttracker.R
import com.example.gifttracker.datamodels.Gift

class GiftsRecyclerAdapter(private val clickListener: (Gift) -> Unit) : RecyclerView.Adapter<GiftsRecyclerAdapter.ViewHolder>() {

    private var gifts = emptyList<Gift>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.gift_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gift = gifts[position]
        holder.bind(gift, position, clickListener)
    }

    override fun getItemCount(): Int {
        return gifts.size
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val giftName = itemView?.findViewById<TextView>(R.id.giftTextView)
        private var giftPosition = 0;

        fun bind(gift: Gift, position: Int, clickListener: (Gift) -> Unit) {
            giftName?.text = gift.name
            giftPosition = position
            itemView.setOnClickListener { clickListener(gift) }
        }
    }

    fun setGifts(gifts: List<Gift>) {
        this.gifts = gifts
        notifyDataSetChanged()
    }

}
