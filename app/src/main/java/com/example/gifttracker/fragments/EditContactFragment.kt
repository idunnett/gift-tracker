package com.example.gifttracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gifttracker.R
import com.example.gifttracker.datamodels.Contact
import com.example.gifttracker.datamodels.ContactWithEvents
import com.example.gifttracker.viewmodels.ContactsViewModel
import com.example.gifttracker.viewmodels.EventsViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_edit_contact.*

class EditContactFragment : Fragment() {
    private val contactsViewModel : ContactsViewModel by activityViewModels()
    private val eventsViewModel : EventsViewModel by activityViewModels()
    private val contactIDArgument : ContactFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<MaterialToolbar>(R.id.toolbar4).setOnClickListener{
            val contactName : String = view.findViewById<TextView>(R.id.editContactNameEditText).text.toString()
            val contactImageUri : String? = view.findViewById<ImageView>(R.id.editContactImageView).tag?.toString()
            val contactHobbies : String = view.findViewById<TextView>(R.id.editContactHobbiesEditText).text.toString()
            if(contactName.isEmpty() || contactHobbies.isEmpty()){
                val snack = Snackbar.make(view.findViewById<MaterialToolbar>(R.id.toolbar4),"Create contact with empty field?",
                    Snackbar.LENGTH_LONG)
                snack.show()
                snack.setAction("Yes", {
                    updateContact(editContactNameEditText, editContactImageView ,editContactHobbiesEditText)
                    val action = EditContactFragmentDirections.actionEditProfileFragmentToProfileFragment(contactIDArgument.contactID)
                    findNavController().navigate(action)
                })
            }
            else{
                val snack = Snackbar.make(view.findViewById<MaterialToolbar>(R.id.toolbar4),"Contact saved successfully.",
                    Snackbar.LENGTH_LONG)
                snack.show()
                updateContact(editContactNameEditText, editContactImageView ,editContactHobbiesEditText)
                val action = EditContactFragmentDirections.actionEditProfileFragmentToProfileFragment(contactIDArgument.contactID)
                findNavController().navigate(action)
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                updateContact(editContactNameEditText, editContactImageView ,editContactHobbiesEditText)
                findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
            }
        })
        contactsViewModel.getContactWithEvents(contactIDArgument.contactID).observe(viewLifecycleOwner, {contact -> contact?.let{setContactFieldValues(it)}})

    }


    private fun setContactFieldValues(contactWithEvents: ContactWithEvents) {
        editContactNameEditText.setText(contactWithEvents.contact.name)
        editContactHobbiesEditText.setText(contactWithEvents.contact.hobbies)
    }

    private fun updateContact(contactNameInputField: EditText, contactImageView: ImageView, contactHobbiesInputField: EditText) {
        val contactName : String = contactNameInputField.text.toString()
        val contactImageUri : String? = contactImageView.tag?.toString()
        val contactHobbies : String = contactHobbiesInputField.text.toString()

        // only name input is required
        if (contactName.isNotEmpty()) {
            val contact = Contact(id = contactIDArgument.contactID, name = contactName, imageUri = contactImageUri, hobbies = contactHobbies)
            contactsViewModel.updateContact(contact)
        }
    }
}