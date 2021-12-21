package com.example.cinemafilm.chat

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemafilm.R

class ChatAdapter (
    private val values: ArrayList<Chat>,
    private val activity: Activity
): RecyclerView.Adapter<ChatAdapter.ViewHolder>(){

        // обработчик клика по элементу списка (лямбда выражение), может быть не задан
        private var itemClickListener: ((Chat) -> Unit)? = null

        fun setItemClickListener(itemClickListener: (Chat) -> Unit) {
            this.itemClickListener = itemClickListener
        }

        // Метод onCreateViewHolder вызывается при создании визуального элемента
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // грузим layout, который содержит вёрстку элемента списка (нарисуйте сами)
            val itemView = LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.chat_item,
                    parent,
                    false)

            // создаем на его основе ViewHolder
            return ViewHolder(itemView)
        }

        override fun getItemCount(): Int = values.size

        // заполняет визуальный элемент данными
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.user.text = values[position].user
            holder.message.text = values[position].message
            }


        //Реализация класса ViewHolder, хранящего ссылки на виджеты.
        class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            var user: TextView = itemView.findViewById(R.id.user)
            var message: TextView = itemView.findViewById(R.id.message)
        }
}