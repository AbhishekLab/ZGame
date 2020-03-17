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


class DemoAdapter : RecyclerView.Adapter<DemoAdapter.DemoViewHolder>() {

    private var data: ArrayList<CustomerData>? = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoViewHolder {
        return DemoViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_customer_adapter, parent, false
            )
        )
    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {

        holder.execute()
    }

    inner class DemoViewHolder(private val mBinding: RowCustomerAdapterBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun execute() {
            mBinding.tvCustomerAge.text = "27"
            mBinding.tvCustomerLocation.text = "Ranchi"
            mBinding.tvCustomerName.text = "Abhishek"
        }
    }

   /* fun addItem(data: ArrayList<CustomerData>?) {
        this.data?.addAll(data!!)
    }

    interface Profile{
        fun itemListener(adapterPosition: Int)
    }*/
}