package com.zgame.zgame.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zgame.zgame.R
import com.zgame.zgame.model.SeekingModule


class SeekingAdapter(private  val context: Context?, private  val employees: ArrayList<SeekingModule>?) : RecyclerView.Adapter<SeekingAdapter.MultiViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MultiViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.row_gender, viewGroup, false)
        return MultiViewHolder(view)
    }

    override fun onBindViewHolder(multiViewHolder: MultiViewHolder, position: Int) {
        multiViewHolder.bind(employees!![position])
    }

    override fun getItemCount(): Int {
        return employees!!.size
    }

    inner class MultiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textView)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(employee: SeekingModule) {
            imageView.visibility = if (employee.isChecked) View.VISIBLE else View.GONE
            textView.text = employee.name
            itemView.setOnClickListener {
                employee.isChecked = !employee.isChecked
                imageView.visibility = if (employee.isChecked) View.VISIBLE else View.GONE
            }
        }
    }

    fun getAll(): ArrayList<SeekingModule>? {
        return employees
    }

    fun getSelected(): ArrayList<SeekingModule>? {
        val selected: ArrayList<SeekingModule> = ArrayList()
        for (i in 0 until employees!!.size) {
            if (employees[i].isChecked) {
                selected.add(employees[i])
            }
        }
        return selected
    }

}