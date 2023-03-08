package com.am.chatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter: ListAdapter<ChatModel, ChatAdapter.ChatVH>(ChatDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup,
        viewType: Int): ChatVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatVH(view)
    }

    override fun onBindViewHolder(holder: ChatVH,
        position: Int) {
        holder.onBind(getItem(position))
    }

    inner class ChatVH(private val item: View): RecyclerView.ViewHolder(item) {
        private val tvSender: TextView = item.findViewById(R.id.tv_sender)
        fun onBind(data: ChatModel) {
            tvSender.text = data.name
        }
    }

    object ChatDiffUtil: DiffUtil.ItemCallback<ChatModel>() {
        override fun areItemsTheSame(oldItem: ChatModel,
            newItem: ChatModel): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: ChatModel,
            newItem: ChatModel): Boolean {
            return false
        }
    }
}