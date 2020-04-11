package com.zgame.zgame.adapter

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zgame.zgame.R
import com.zgame.zgame.databinding.RowGenderBinding
import com.zgame.zgame.model.GenderModule

class SignUp2Adapter(
    private var data: ArrayList<GenderModule>?,
    private val listener: StateU,
    private var flag: String
) :
    RecyclerView.Adapter<SignUp2Adapter.GenderViewHolder>() {

    private var checkedPosition = -1

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

    inner class GenderViewHolder(private val mBinding: RowGenderBinding) :
        RecyclerView.ViewHolder(mBinding.root) {


        fun bind(employee: GenderModule) {

            if (checkedPosition == adapterPosition) {
                mBinding.imageView.visibility = View.VISIBLE
            } else {
                mBinding.imageView.visibility = View.GONE
            }

            mBinding.textView.text = employee.name
            itemView.setOnClickListener {
                when (flag) {
                    "country" -> {
                        d("asdasa","country")
                        listener.selectedCountry(data!![adapterPosition].name!!)
                        listener.getState(adapterPosition)
                    }
                    "age" -> {
                        listener.selectedAge(data!![adapterPosition].name!!)
                    }
                    "height" -> {
                        listener.selectedHeight(data!![adapterPosition].name!!)
                    }
                    "state" -> {
                        listener.selectedState(data!![adapterPosition].name!!)
                    }
                }
                checkedPosition = adapterPosition
                notifyDataSetChanged()
            }
        }
    }

    fun filterList(locationData: ArrayList<GenderModule>) {
        data = locationData
        notifyDataSetChanged()
    }

    fun setState(data: ArrayList<GenderModule>?){
        this.data = data!!
    }
    fun setCountry(
        data: ArrayList<GenderModule>?,
        flag: String
    ){
        this.data?.clear()
        this.data = data!!
        this.flag = flag
        checkedPosition = -1
    }

    interface StateU {
        fun getState(position: Int)
        fun selectedAge(age: String)
        fun selectedHeight(height : String)
        fun selectedCountry(country : String)
        fun selectedState(state : String)
    }


}