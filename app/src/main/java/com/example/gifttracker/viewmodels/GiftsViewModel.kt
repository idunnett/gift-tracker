package com.example.gifttracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.gifttracker.datamodels.Gift
import com.example.gifttracker.persistence.database.GiftTrackerDatabase
import com.example.gifttracker.persistence.repositories.GiftRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GiftsViewModel(application: Application) : AndroidViewModel(application) {

    private val giftRepository : GiftRepository
    val gifts : LiveData<List<Gift>>

    // Initializes contacts by fetching them from the ContactRepository through the ContactDAO
    init {
        val giftsDAO = GiftTrackerDatabase.getDatabase(application).giftDAO()
        giftRepository = GiftRepository(giftsDAO)
        gifts = giftRepository.gifts
    }

    fun getGift(giftID: Int): LiveData<Gift> {
        return giftRepository.getGift(giftID)
    }

    fun insertGift(gift : Gift) = viewModelScope.launch(Dispatchers.IO) {
        giftRepository.insertGift(gift)
    }

    fun deleteGift(gift: Gift) = viewModelScope.launch(Dispatchers.IO) {
        giftRepository.deleteGift(gift)
    }

    fun updateGift(gift: Gift) = viewModelScope.launch(Dispatchers.IO) {
        giftRepository.updateGift(gift)
    }

}