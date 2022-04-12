package com.example.gifttracker.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gifttracker.R
import com.example.gifttracker.datamodels.Contact


class ContactsRecyclerAdapter(private val clickListener: (Contact) -> Unit) : RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder>() {

    private var contacts = emptyList<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.contacts_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact, position, clickListener)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
//        private val contactImage = itemView?.findViewById<TextView>(R.id.noteTitleTextView)
        private val contactName = itemView?.findViewById<TextView>(R.id.contactNameTextView)
        private val contactImage = itemView?.findViewById<ImageView>(R.id.contactListItemImageView)
        private var contactPosition = 0;

        fun bind(contact: Contact, position: Int, clickListener: (Contact) -> Unit) {
            contactName?.text = contact.name
            if(contact.imageUri?.isNotEmpty() == true) contactImage?.setImageURI(Uri.parse(contact.imageUri))
            contactPosition = position
            itemView.setOnClickListener { clickListener(contact) }
        }
    }

    fun setContacts(contacts : List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

    fun getContactAtPosition(position: Int): Contact {
        return contacts[position]
    }

}