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
import com.zgame.zgame.model.SignUpModel

class CustomerAdapter(val context: FragmentActivity?, private val listener : Profile) : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {
    private var data: ArrayList<SignUpModel>? = ArrayList()
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
            mBinding.tvCustomerLocation.text = "${"Location: "}${data?.get(adapterPosition)?.state}"
            mBinding.tvCustomerName.text = data?.get(adapterPosition)?.userName

           /* Glide.with(context!!).load(data?.get(adapterPosition)?.image).into(mBinding.ivCustomer)
            mBinding.cvDetails.setOnClickListener {
                listener.itemListener(data?.get(adapterPosition)?.id)
            }*/
        }
    }

    fun addItem(data: ArrayList<SignUpModel>?) {
        this.data?.addAll(data!!)
    }

    interface Profile{
        fun itemListener(id: String?)
    }
}