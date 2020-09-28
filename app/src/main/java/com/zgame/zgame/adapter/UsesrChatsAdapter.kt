package com.zgame.zgame.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zgame.zgame.R
import com.zgame.zgame.databinding.RowUserMessageBinding

class UsesrChatsAdapter : RecyclerView.Adapter<UsesrChatsAdapter.userChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userChatViewHolder {
        return userChatViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_users_chat, parent, false
            )
        )
    }

    override fun getItemCount(): Int = 20

    override fun onBindViewHolder(holder: userChatViewHolder, position: Int) {
        holder.execute()
    }

    inner class userChatViewHolder(private val mBinding: RowUserMessageBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun execute(){

        }
    }
}