package com.example.gifttracker.fragments

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gifttracker.R
import com.example.gifttracker.databinding.FragmentDashboardBinding
import com.example.gifttracker.datamodels.AttendeePersonBean
import com.example.gifttracker.datamodels.CalendarEvent
import com.example.gifttracker.datamodels.CalendarBean
import com.example.gifttracker.persistence.database.GiftTrackerDatabase
import com.example.gifttracker.persistence.repositories.CalendarRepository
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import kotlin.concurrent.thread

class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding;
    private var mCalendarBeanList = arrayListOf<CalendarBean>()
    val TAG="jcy-DashboardFragment";

    var strDateFormat = "MM-dd HH:mm"
    var sdf: SimpleDateFormat = SimpleDateFormat(strDateFormat)
    var mRepository: CalendarRepository?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        mRepository=CalendarRepository(GiftTrackerDatabase.getDatabase(requireContext()).calendarEventDAO());
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.contact_list).setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_contactListFragment)
        }

        view.findViewById<ImageButton>(R.id.settings).setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_settingsFragment)
        }

        view.findViewById<MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI();
        getContactList()

    }


    @SuppressLint("Range")
    private fun getContactList() {
        Log.d(TAG, "  getContactList   ");
        thread {
            mCalendarBeanList.clear()
            val CALANDER_EVENT_URL = "content://com.android.calendar/events"
            val uri: Uri = Uri.parse(CALANDER_EVENT_URL)

            val cursor: Cursor? =
                requireActivity().contentResolver.query(uri, null, "${CalendarContract.Events.DTSTART} >= ?",
                    arrayOf<String>(System.currentTimeMillis().toString()), CalendarContract.Events.DTSTART + " asc")
            while (cursor?.moveToNext() == true) {

                val id =
                    cursor.getLong(cursor.getColumnIndex(CalendarContract.Events._ID))


                val title = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE))

                val dtstart = cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTSTART))

                var description =
                    cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION))
                if (description != null) {

                    var indexEnter = description.indexOf("\n-::");
                    if (indexEnter > 0) {
                        description = description.substring(0, indexEnter)
                    }

                }


                val location =
                    cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION))
                var personList = arrayListOf<AttendeePersonBean>()
                val cursorPerson: Cursor? =
                    CalendarContract.Attendees.query(requireActivity().contentResolver, id, null)
                while (cursorPerson?.moveToNext() == true) {

                    val attendeeEmail =
                        cursorPerson.getString(cursorPerson.getColumnIndex("attendeeEmail"))
                    val attendeeName =
                        cursorPerson.getString(cursorPerson.getColumnIndex("attendeeName"))
                    val attendeeStatus =
                        cursorPerson.getInt(cursorPerson.getColumnIndex("attendeeStatus"))
                    Log.d(
                        TAG,
                        "attendeeEmail $attendeeEmail attendeeName $attendeeName attendeeStatus $attendeeStatus"
                    );
                    if (attendeeStatus != 1) {
                        personList.add(AttendeePersonBean(attendeeEmail, attendeeName))
                    }
                }
                var contact = CalendarBean(id, title, description, location, dtstart, personList)

                mCalendarBeanList.add(contact)
                if (mCalendarBeanList.size == 3) {
                    break
                }
            }
            requireActivity().runOnUiThread {
                updateUI()
            }
            saveToSqlite()
        }
    }

    private fun updateUI(){
        binding.llDashboardEvent1.visibility= View.INVISIBLE;
        binding.llDashboardEvent2.visibility= View.INVISIBLE;
        binding.llDashboardEvent3.visibility= View.INVISIBLE;
        if(mCalendarBeanList.size==1){
            showEvent1()
        }else if(mCalendarBeanList.size==2){
            showEvent1()
            showEvent2()
        }else if(mCalendarBeanList.size==3){
            showEvent1()
            showEvent2()
            showEvent3()
        }
    }
    private fun showEvent1(){
        binding.llDashboardEvent1.visibility= View.VISIBLE;
        binding.dashboardEvent1.setText(mCalendarBeanList[0].name)
        var list =mCalendarBeanList[0].peoples;
        if(list.size==0){
            binding.dashboardName1.setText("")
        }else  if(list.size==1){
            var name = if(!TextUtils.isEmpty(mCalendarBeanList[0].peoples[0].name)){
                mCalendarBeanList[0].peoples[0].name
            }else{
                mCalendarBeanList[0].peoples[0].email
            }
            Log.d(TAG, "name : ");
            binding.dashboardName1.setText(name)
        }else{
            var name = if(!TextUtils.isEmpty(mCalendarBeanList[0].peoples[0].name)){
                mCalendarBeanList[0].peoples[0].name
            }else{
                mCalendarBeanList[0].peoples[0].email
            }
            name=name+"..."
            binding.dashboardName1.setText(name)
        }
        binding.dashboardDate1.setText(sdf.format(Date(mCalendarBeanList[0].dtStart)))
        binding.dashboardGift1.setText(mCalendarBeanList[0].description?:"")
    }
    private fun showEvent2(){
        binding.llDashboardEvent2.visibility= View.VISIBLE;
        binding.dashboardEvent2.setText(mCalendarBeanList[1].name)
        var list =mCalendarBeanList[1].peoples;
        if(list.size==0){
            binding.dashboardName2.setText("")
        }else  if(list.size==1){
            var name = if(!TextUtils.isEmpty(mCalendarBeanList[1].peoples[0].name)){
                mCalendarBeanList[1].peoples[0].name
            }else{
                mCalendarBeanList[1].peoples[0].email
            }
            binding.dashboardName2.setText(name)
        }else{
            var name = if(!TextUtils.isEmpty(mCalendarBeanList[1].peoples[0].name)){
                mCalendarBeanList[1].peoples[0].name
            }else{
                mCalendarBeanList[1].peoples[0].email
            }
            name=name+"..."
            binding.dashboardName2.setText(name)
        }
        binding.dashboardDate2.setText(sdf.format(Date(mCalendarBeanList[1].dtStart)))
        binding.dashboardGift2.setText(mCalendarBeanList[1].description?:"")
    }
    private fun showEvent3(){
        binding.llDashboardEvent3.visibility= View.VISIBLE;
        binding.dashboardEnent3.setText(mCalendarBeanList[2].name)
        var list =mCalendarBeanList[2].peoples;
        if(list.size==0){
            binding.dashboardName3.setText("")
        }else  if(list.size==1){
            var name = if(!TextUtils.isEmpty(mCalendarBeanList[2].peoples[0].name)){
                mCalendarBeanList[2].peoples[0].name
            }else{
                mCalendarBeanList[2].peoples[0].email
            }
            binding.dashboardName3.setText(name)
        }else{
            var name = if(!TextUtils.isEmpty(mCalendarBeanList[2].peoples[0].name)){
                mCalendarBeanList[2].peoples[0].name
            }else{
                mCalendarBeanList[2].peoples[0].email
            }
            name=name+"..."
            binding.dashboardName3.setText(name)
        }
        binding.dashboardDate3.setText(sdf.format(Date(mCalendarBeanList[2].dtStart)))
        binding.dashboardGift3.setText(mCalendarBeanList[2].description?:"")
    }
    private fun saveToSqlite() {

        lifecycleScope.launch(Dispatchers.IO){
            for(collect in mCalendarBeanList){
                var prosons= "";
                for(person in collect.peoples){
                    prosons ="$prosons,${person.email},"
                }
                var event = CalendarEvent(name = collect.name, description = collect.description?:"", dtStart = collect.dtStart, peoples = prosons);
                Log.d(TAG, "saveToSqlite event "+event);
                mRepository?.insertEvent(event)
            }
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }
}
