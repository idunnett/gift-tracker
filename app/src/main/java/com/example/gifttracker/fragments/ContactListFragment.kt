package com.example.gifttracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gifttracker.R
import com.example.gifttracker.adapters.ContactsRecyclerAdapter
import com.example.gifttracker.datamodels.Contact
import com.example.gifttracker.viewmodels.ContactsViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_contact_list.*

class ContactListFragment : Fragment() {

    private val contactsViewModel : ContactsViewModel by activityViewModels()
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
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contactListRecyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = ContactsRecyclerAdapter { contact: Contact -> handleContactClick(contact) }
        contactListRecyclerView.adapter = adapter

        contactsViewModel.contacts.observe(viewLifecycleOwner, Observer { contacts -> contacts?.let { adapter.setContacts(it) } })

        view.findViewById<MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener {
            findNavController().navigate(R.id.action_contactListFragment_to_dashboardFragment)
        }
        view.findViewById<MaterialButton>(R.id.add_new_contact).setOnClickListener {
            findNavController().navigate(R.id.action_contactListFragment_to_createContactFragment)
        }
    }

    private fun handleContactClick(contact: Contact) {
        val contactID = contact.id
        val action = ContactListFragmentDirections.actionContactListFragmentToProfileFragment(contactID)
        findNavController().navigate(action)
    }
}