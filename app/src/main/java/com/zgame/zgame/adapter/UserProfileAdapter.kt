package com.zgame.zgame.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zgame.zgame.R
import com.zgame.zgame.databinding.RowUserProfileBinding

class UserProfileAdapter(private val context : Context) : RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder>() {

    private var listOfImages: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileViewHolder {
        return UserProfileViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_user_profile, parent, false))
    }

    override fun getItemCount(): Int = listOfImages.size

    override fun onBindViewHolder(holder: UserProfileViewHolder, position: Int) {
        holder.setImages()
    }

    inner class UserProfileViewHolder(private val mBinding: RowUserProfileBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun setImages() {
           Glide.with(context).load(listOfImages[adapterPosition]).apply{
               RequestOptions().placeholder(R.drawable.ic_white_profile_place_holder).circleCrop()
           }.into(mBinding.imgUserPic)
        }
    }

    fun addImages(listOfImages: ArrayList<String>) {
        this.listOfImages.addAll(listOfImages)
    }
}
