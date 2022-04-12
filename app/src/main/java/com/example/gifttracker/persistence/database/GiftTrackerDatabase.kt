package com.example.gifttracker.persistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gifttracker.datamodels.*
import com.example.gifttracker.persistence.dao.CalendarEventDAO
import com.example.gifttracker.persistence.dao.ContactDAO
import com.example.gifttracker.persistence.dao.EventDAO
import com.example.gifttracker.persistence.dao.GiftDAO

@Database(entities = [Contact::class, Event::class, Gift::class, CalendarEvent::class],
    version = 11)
abstract class GiftTrackerDatabase : RoomDatabase() {

    abstract fun contactDAO() : ContactDAO
    abstract fun eventDAO() : EventDAO
    abstract fun giftDAO() : GiftDAO
    abstract fun calendarEventDAO() : CalendarEventDAO

    // Singleton design pattern - Build the database, and returns it back to the ViewModel
    companion object {
        @Volatile
        private var INSTANCE : GiftTrackerDatabase? = null

        fun getDatabase(context: Context) : GiftTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, GiftTrackerDatabase::class.java, "gifttracker_database").fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }

    }

}