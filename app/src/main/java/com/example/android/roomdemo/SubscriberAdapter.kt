package com.example.android.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.roomdemo.databinding.ListItemBinding
import com.example.android.roomdemo.db.Subscriber
import com.example.android.roomdemo.generated.callback.OnClickListener

class SubscriberAdapter(private val clickListener: (Subscriber) -> Unit): RecyclerView.Adapter<MyViewHolder>(){

    private  val subscribesList = ArrayList<Subscriber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subscribesList[position], clickListener)
    }

    override fun getItemCount(): Int = subscribesList.size

    fun setList(subscriber: List<Subscriber>){
        subscribesList.clear()
        subscribesList.addAll(subscriber)
    }
}

class MyViewHolder(private val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(subscriber: Subscriber, clickListener: (Subscriber) -> Unit){
        binding.nameTextView.text = subscriber.name
        binding.emailTextView.text = subscriber.email
        binding.listItemLayout.setOnClickListener {
            clickListener(subscriber)
        }
    }

}