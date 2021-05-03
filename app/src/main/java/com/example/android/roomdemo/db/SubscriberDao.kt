package com.example.android.roomdemo.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDao {

    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber): Long

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber): Int

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber): Int

    @Query("DELETE FROM subscriber_table_name")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM subscriber_table_name")
    fun getAllSubscribers(): LiveData<List<Subscriber>>
}