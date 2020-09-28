package com.zgame.zgame.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zgame.zgame.R
import com.zgame.zgame.databinding.RowUserMessageBinding

class UserMessageAdapter : RecyclerView.Adapter<UserMessageAdapter.UserMessageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMessageViewHolder {
        return UserMessageViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_user_message, parent, false
            )
        )
    }

    override fun getItemCount(): Int = 20

    override fun onBindViewHolder(holder: UserMessageViewHolder, position: Int) {
        holder.execute()
    }

    inner class UserMessageViewHolder(private val mBinding: RowUserMessageBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun execute(){

        }
    }
}