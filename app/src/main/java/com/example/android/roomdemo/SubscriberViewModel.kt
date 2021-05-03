package com.example.android.roomdemo

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.roomdemo.db.Subscriber
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel(), Observable {

    val subscriberLiveData = repository.subscriberLiveData

    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete: Subscriber

    @Bindable
    val inputName = MutableLiveData<String>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        if (inputName.value == null || inputEmail.value == null) {
            statusMessage.value = Event("Fill in the blank form.")
        } else if (inputName.value == null) {
            statusMessage.value = Event("Enter a name")
        }else if(inputEmail.value == null){
            statusMessage.value = Event("Enter email address")
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value = Event("Enter correct email format")
        } else {
            if (isUpdateOrDelete) {
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputEmail.value!!
                update(subscriberToUpdateOrDelete)
            } else{
                val name = inputName.value!!
                val email = inputEmail.value!!
                insert(Subscriber(0, name, email))
                inputEmail.value = null
                inputName.value = null
            }

        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(subscriberToUpdateOrDelete)
        } else {
            clearAll()

        }
    }

    private fun insert(subscriber: Subscriber) = viewModelScope.launch {
        val rowId = repository.insert(subscriber)
        if (rowId > 0) {
            statusMessage.value = Event("Subscriber added successfully $rowId ")
        } else {
            statusMessage.value = Event("Error occurred.")
        }
    }

    private fun update(subscriber: Subscriber) = viewModelScope.launch {
        val noOfRows = repository.update(subscriber)
        if (noOfRows > 0) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRows Row Updated Successfully")
        } else {
            statusMessage.value = Event("Error occurred.")
        }
    }

    private fun delete(subscriber: Subscriber) = viewModelScope.launch {
        val noOfRowsDeleted = repository.delete(subscriber)

        if (noOfRowsDeleted > 0) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRowsDeleted Row Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }

    }

    private fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAll()
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Subscribers Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }


}