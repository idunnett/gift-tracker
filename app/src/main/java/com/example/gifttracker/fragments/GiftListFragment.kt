package com.example.gifttracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gifttracker.R
import com.example.gifttracker.adapters.ContactsRecyclerAdapter
import com.example.gifttracker.adapters.GiftsRecyclerAdapter
//import com.example.gifttracker.adapters.GiftsRecyclerAdapter
import com.example.gifttracker.datamodels.Contact
import com.example.gifttracker.datamodels.Event
import com.example.gifttracker.datamodels.Gift
import com.example.gifttracker.dialogs.AddEventDialog
import com.example.gifttracker.dialogs.AddGiftDialog
import com.example.gifttracker.viewmodels.ContactsViewModel
import com.example.gifttracker.viewmodels.EventsViewModel
import com.example.gifttracker.viewmodels.GiftsViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_gift_list.*

class GiftListFragment : Fragment() {

    private val eventsViewModel : EventsViewModel by activityViewModels()
    private val contactsViewModel: ContactsViewModel by activityViewModels()
    private val giftsViewModel: GiftsViewModel by activityViewModels()
    private val giftListArguments : GiftListFragmentArgs by navArgs()
    private lateinit var mContext : Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gift_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener {
            val action = GiftListFragmentDirections.actionGiftListFragmentToContactFragment(giftListArguments.contactID)
            findNavController().navigate(action)
        }

        view.findViewById<FloatingActionButton>(R.id.floating_action_button).setOnClickListener{
            showAddGiftDialog()
        }

        giftListRecyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = GiftsRecyclerAdapter { gift: Gift -> handleGiftClick(gift) }
        giftListRecyclerView.adapter = adapter

        eventsViewModel.getEventWithGifts(giftListArguments.eventID).observe(viewLifecycleOwner, Observer { gift -> gift?.let {
            adapter.setGifts(it.gifts)

            view.findViewById<TextView>(R.id.giftListEventName).text = it.event.name
        } })

        contactsViewModel.getContact(giftListArguments.contactID).observe(viewLifecycleOwner, Observer { contact -> contact?.let {
            view.findViewById<TextView>(R.id.giftListTitle).text = "Gifts for " + it.name
        } })

//        view.findViewById<MaterialButton>(R.id.add_new_contact).setOnClickListener {
//            findNavController().navigate(R.id.action_contactListFragment_to_createContactFragment)
//        }
    }

    private fun handleGiftClick(gift: Gift) {
//        val action = GiftListFragmentDirections.actionGiftListFragmentToContactFragment(giftListArguments.contactID)
//        findNavController().navigate(action)
    }

    private fun saveGift(giftName : String, eventId: Int) {
        val gift = Gift(name = giftName, eventId = eventId)
        giftsViewModel.insertGift(gift)
    }

    private fun showAddGiftDialog(){
        context?.let {
            val dialog = AddGiftDialog(it)
            dialog.show()
            val editText = dialog.findViewById<EditText>(R.id.dialogGiftName)
            editText.requestFocus()
            dialog.findViewById<Button>(R.id.dialogSubmitBtn).setOnClickListener{
                val giftName = editText.text.toString()
                println(giftName)
                if(giftName.isNotEmpty())
                    saveGift(giftName, giftListArguments.eventID)
                dialog.dismiss()
            }
            dialog.findViewById<Button>(R.id.dialogCancelBtn).setOnClickListener{
                dialog.dismiss()
            }
        }

    }
}