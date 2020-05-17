package com.zgame.zgame.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zgame.zgame.MainActivity
import com.zgame.zgame.R
import com.zgame.zgame.databinding.RowDrawerListBinding
import com.zgame.zgame.model.DataModelWithBoolean

class DrawerItemAdapter(
    private val context: MainActivity, val items: ArrayList<DataModelWithBoolean>
) : RecyclerView.Adapter<DrawerItemAdapter.DrawerListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerListViewHolder {
        return DrawerListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_drawer_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: DrawerListViewHolder, position: Int) {
        holder.setDrawerList()
    }


    inner class DrawerListViewHolder(private val mBinding: RowDrawerListBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun setDrawerList() {
            mBinding.apply {
                this.position = adapterPosition
                this.activity = context
                tvTitle.text = items[adapterPosition].title
                Glide.with(context).load(items[adapterPosition].icon).into(ivTitle)
            }
        }
    }
}

