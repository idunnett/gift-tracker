package com.example.gifttracker.fragments

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gifttracker.R
import com.example.gifttracker.adapters.ContactsRecyclerAdapter
import com.example.gifttracker.adapters.EventsRecyclerAdapter
import com.example.gifttracker.datamodels.Contact
import com.example.gifttracker.datamodels.ContactWithEvents
import com.example.gifttracker.datamodels.Event
import com.example.gifttracker.dialogs.AddEventDialog
import com.example.gifttracker.viewmodels.ContactsViewModel
import com.example.gifttracker.viewmodels.EventsViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_contact.*
import kotlinx.android.synthetic.main.fragment_contact_list.*

class ContactFragment : Fragment() {

    private val contactsViewModel : ContactsViewModel by activityViewModels()
    private val eventsViewModel : EventsViewModel by activityViewModels()
    private val contactIDArgument : ContactFragmentArgs by navArgs()
    private val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialToolbar>(R.id.toolbar3).setNavigationOnClickListener {
            findNavController().navigate(R.id.action_contactFragment_to_contactListFragment)
        }

        view.findViewById<MaterialButton>(R.id.addEventButton).setOnClickListener {
            showDialog()
        }

        view.findViewById<MaterialButton>(R.id.edit).setOnClickListener {
            val action = ContactFragmentDirections.actionProfileFragmentToEditProfileFragment(contactIDArgument.contactID)
            findNavController().navigate(action)
        }


        contactEventRecyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = EventsRecyclerAdapter { event: Event -> handleEventClick(event) }
        contactEventRecyclerView.adapter = adapter

        contactsViewModel.getContactWithEvents(contactIDArgument.contactID).observe(viewLifecycleOwner, Observer { contact -> contact?.let {
            setContactFieldValues(it)
            adapter.setEvents(it.events)
        }
        })
    }

    private fun setContactFieldValues(contactWithEvents: ContactWithEvents) {
        if(contactWithEvents.contact.imageUri?.isNotEmpty() == true) contactImageView?.setImageURI(Uri.parse(contactWithEvents.contact.imageUri))
        contactNameTextView.text = contactWithEvents.contact.name
        contactHobbiesTextView.text = contactWithEvents.contact.hobbies
    }

    private fun handleEventClick(event: Event){
        val eventID = event.eventId
        val action = ContactFragmentDirections.actionContactFragmentToGiftListFragment(contactIDArgument.contactID, eventID)
        findNavController().navigate(action)
    }

    private fun saveEvent(eventName : String, contactId: Int, date: String) {
        val event = Event(name = eventName, contactId = contactId, date = date)
        eventsViewModel.insertEvent(event)

    }

    private fun showDialog() {
        context?.let {
            val dialog = AddEventDialog(it)
            dialog.show()
            val editText = dialog.findViewById<EditText>(R.id.dialogEventName)
            editText.requestFocus()
            val datePicker = dialog.findViewById<DatePicker>(R.id.dialogDatePicker)
            dialog.findViewById<Button>(R.id.dialogSubmitBtn).setOnClickListener{
                val eventName = editText.text.toString()
                val date: String = months[datePicker.month] + " " + datePicker.dayOfMonth.toString() + ", " + datePicker.year.toString()
                if(eventName.isNotEmpty())
                saveEvent(eventName, contactIDArgument.contactID, date)
                dialog.dismiss()
            }
            dialog.findViewById<Button>(R.id.dialogCancelBtn).setOnClickListener{
                dialog.dismiss()
            }
        }
    }
}