package com.zgame.zgame.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zgame.zgame.R
import com.zgame.zgame.databinding.RowCustomerContactBinding
import com.zgame.zgame.model.CircleData

class CustomerContactAdapter(private val context: Context, private var callBack : StoriesCallBack) :
    RecyclerView.Adapter<CustomerContactAdapter.CustomerContactViewHolder>() {

    private var images: ArrayList<CircleData> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerContactViewHolder {
        return CustomerContactViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_customer_contact,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: CustomerContactViewHolder, position: Int) {
        holder.setContactList()
    }


    inner class CustomerContactViewHolder(private val mBinding: RowCustomerContactBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun setContactList() {
            Glide.with(context).load(images[adapterPosition].listOfImages!![0])
                .apply(RequestOptions().circleCrop().placeholder(R.drawable.profile_enable))
                .into(mBinding.imgContact)

            mBinding.imgContact.setOnClickListener { callBack.openStoriesDialog(images[adapterPosition]) }
        }
    }

    fun addItem(images: ArrayList<CircleData>?) {
        this.images = images!!
    }

    interface StoriesCallBack{
        fun openStoriesDialog(circleProfileData: CircleData)
    }
}

