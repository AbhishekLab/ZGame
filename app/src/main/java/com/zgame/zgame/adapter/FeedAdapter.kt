package com.zgame.zgame.adapter

import android.content.Context
import android.util.Log.e
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.zgame.zgame.R
import com.zgame.zgame.databinding.RowFeedsBinding

class FeedAdapter(private val context : Context, private val callBack : Wink) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    private var data: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_feeds, parent, false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.execute()
    }

    inner class FeedViewHolder(private val mBinding: RowFeedsBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun execute() {

            Glide.with(context).load(data[adapterPosition].apply {
                RequestOptions().placeholder(R.drawable.ic_white_profile_place_holder).circleCrop()
                RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
            }).into(mBinding.imgUserPic)

           /* mBinding.llHeart.setOnClickListener {
                if(mBinding.imgHeart.visibility == View.VISIBLE){
                    mBinding.imgHeart.visibility = View.GONE
                    mBinding.imgBlackHeart.visibility = View.VISIBLE
                    Glide.with(context).load(R.drawable.black_heart).into(mBinding.imgBlackHeart)
                    callBack.addWink(adapterPosition)
                }else{
                    mBinding.imgHeart.visibility = View.VISIBLE
                    mBinding.imgBlackHeart.visibility = View.GONE
                    Glide.with(context).load(R.drawable.heart).into(mBinding.imgHeart)
                }
            }*/
        }
    }

    fun addItem(data: ArrayList<String>?) {
        e("sdfsdfsdf", data!![0])
        this.data = data!!
    }

    interface Wink{
        fun addWink(position: Int?)
    }
}