package com.example.gifttracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gifttracker.R
import com.example.gifttracker.datamodels.Event

class EventsRecyclerAdapter(private val clickListener: (Event) -> Unit) : RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder>() {

    private var events = emptyList<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.event_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event, position, clickListener)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val eventName = itemView?.findViewById<TextView>(R.id.eventNameTextView)
        private val eventDate = itemView?.findViewById<TextView>(R.id.eventDateTextView)
        private var eventPosition = 0;

        fun bind(event: Event, position: Int, clickListener: (Event) -> Unit) {
            eventName?.text = event.name.toString()
            eventDate?.text = event.date.toString()
            eventPosition = position
            itemView.setOnClickListener { clickListener(event) }
        }
    }

    fun setEvents(events: List<Event>) {
        this.events = events
        notifyDataSetChanged()
    }

    fun getEventAtPosition(position: Int): Event {
        return events[position]
    }

}