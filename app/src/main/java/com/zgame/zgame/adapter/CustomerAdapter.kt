package com.zgame.zgame.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zgame.zgame.R
import com.zgame.zgame.databinding.RowCustomerAdapterBinding
import com.zgame.zgame.model.CustomerData

class CustomerAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    private var data: ArrayList<CustomerData>? = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        return CustomerViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_customer_adapter, parent, false
            )
        )
    }

    override fun getItemCount(): Int = data!!.size

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {

        holder.execute()
    }

    inner class CustomerViewHolder(private val mBinding: RowCustomerAdapterBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun execute() {
            mBinding.tvCustomerAge.text = "${"Age: "}${data?.get(adapterPosition)?.age}"
            mBinding.tvCustomerLocation.text = "${"Location: "}${data?.get(adapterPosition)?.location}"
            mBinding.tvCustomerName.text = data?.get(adapterPosition)?.name
            Glide.with(context!!).load(data?.get(adapterPosition)?.image).into(mBinding.ivCustomer)
        }
    }

    fun addItem(data: ArrayList<CustomerData>?) {
        this.data?.addAll(data!!)
    }
}