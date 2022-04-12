package com.example.gifttracker.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gifttracker.R
import com.example.gifttracker.datamodels.Contact
import com.example.gifttracker.viewmodels.ContactsViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_create_contact.*

class CreateContactFragment : Fragment() {

    private val contactsViewModel: ContactsViewModel by activityViewModels()

    private val CONTACT_PERMISSION_CODE = 1
    private val CONTACT_PICK_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialToolbar>(R.id.toolbar4).setNavigationOnClickListener() {
            findNavController().navigate(R.id.action_createContactFragment_to_contactListFragment)
        }

        view.findViewById<MaterialButton>(R.id.importContactButton).setOnClickListener {
            if(checkImportContactPermission()) pickContact()
            else requestContactPermission()
        }

        view.findViewById<MaterialButton>(R.id.createContactSubmitButton).setOnClickListener {
            val success = saveContact(createContactNameEditText, createContactImageView, createContactHobbiesEditText)
            if(success)
                findNavController().navigate(R.id.action_createContactFragment_to_contactListFragment)
        }

    }

    // save a contact to the database
    private fun saveContact(contactNameInputField: TextView, contactImageView: ImageView, contactHobbiesInputField: TextView):Boolean {
        val contactName : String = contactNameInputField.text.toString()
        val contactImageUri : String? = contactImageView.tag?.toString()
        val contactHobbies : String = contactHobbiesInputField.text.toString()

        // only name input is required
        if (contactName.isNotEmpty()) {
            val contact = Contact(name = contactName, imageUri = contactImageUri, hobbies = contactHobbies)
            contactsViewModel.insertContact(contact)
            return true
        }

        Toast.makeText(context, "Name must not be empty.", Toast.LENGTH_LONG).show()
        return false
    }

    // starts contacts activity of 'Contacts Contracts'
    private fun pickContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, CONTACT_PICK_CODE)
    }

    // returns true if contacts permissions are allowed, otherwise returns false
    // creates toast asking user to enable permissions if they are set to denied
    private fun checkImportContactPermission(): Boolean {
        val permission = context?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.READ_CONTACTS) }
        if(permission == PackageManager.PERMISSION_GRANTED) return true
        else if(permission == PackageManager.PERMISSION_DENIED) Toast.makeText(context, "Please enable contacts permissions in settings", Toast.LENGTH_LONG).show()
        return false
    }

    // display pop up asking to allow of deny contact permissions if not yet set
    private fun requestContactPermission() {
        val permission = arrayOf(android.Manifest.permission.READ_CONTACTS)
        activity?.let { ActivityCompat.requestPermissions(it, permission, CONTACT_PERMISSION_CODE) }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == CONTACT_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                pickContact()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK){
            if(requestCode == CONTACT_PICK_CODE){
                val cursor: Cursor
                val uri = data!!.data
                cursor = context?.contentResolver?.query(uri!!, null, null, null, null)!!
                if(cursor.moveToFirst()){
                    val contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val contactThumbnail = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))

                    createContactNameEditText.setText(contactName)
                    if(contactThumbnail != null) {
                        val imageUri = Uri.parse(contactThumbnail)
                        createContactImageView.setImageURI(imageUri)
                        createContactImageView.tag = imageUri
                    }
                }
            }
        }
    }
}