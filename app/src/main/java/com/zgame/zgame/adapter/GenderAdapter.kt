package com.zgame.zgame.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zgame.zgame.R
import com.zgame.zgame.databinding.RowGenderBinding
import com.zgame.zgame.model.GenderModule


class GenderAdapter(private val context : Context, private var data : ArrayList<GenderModule>?) : RecyclerView.Adapter<GenderAdapter.GenderViewHolder>() {

    private var checkedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderViewHolder {
        return GenderViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_gender, parent, false
            )
        )
    }

    override fun getItemCount(): Int = data!!.size

    override fun onBindViewHolder(holder: GenderViewHolder, position: Int) {
        holder.bind(data?.get(position)!!)
    }

    inner class GenderViewHolder(private  val mBinding: RowGenderBinding) : RecyclerView.ViewHolder(mBinding.root) {


        fun bind(employee: GenderModule) {
            if (checkedPosition == -1) {
                mBinding.imageView.visibility = View.GONE
            } else {
                if (checkedPosition == adapterPosition) {
                    mBinding.imageView.visibility = View.VISIBLE
                } else {
                    mBinding.imageView.visibility = View.GONE
                }
            }
           mBinding.textView.text = employee.name
            itemView.setOnClickListener {
               mBinding.imageView.visibility = View.VISIBLE
                if (checkedPosition != adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                }
            }
        }
    }

    fun getSelected(): GenderModule? {
        return if (checkedPosition != -1) {
            data?.get(checkedPosition)
        } else null
    }
    fun filterList(locationData: ArrayList<GenderModule>) {
        data = locationData
        notifyDataSetChanged()
    }

}