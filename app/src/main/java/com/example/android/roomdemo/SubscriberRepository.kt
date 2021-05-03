package com.example.android.roomdemo

import com.example.android.roomdemo.db.Subscriber
import com.example.android.roomdemo.db.SubscriberDao

class SubscriberRepository(private val dao: SubscriberDao) {

    //LiveData
    val subscriberLiveData = dao.getAllSubscribers()

    suspend fun insert(subscriber: Subscriber): Long{
        return dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber): Int{
        return  dao.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber): Int{
        return dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll(): Int{
      return  dao.deleteAll()
    }
}