package com.zgame.zgame.activity

import androidx.viewpager.widget.ViewPager
import com.zgame.zgame.R
import com.zgame.zgame.adapter.CommonSliderAdapter
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.databinding.ActivityCommonSliderBinding
import com.zgame.zgame.utils.ScalePageTransformer
import java.util.*
import kotlin.collections.ArrayList

class CommonSliderActivity : BaseActivity<ActivityCommonSliderBinding>() {

    private var imageModelArrayList: ArrayList<String>? = null
    var imgPager : ViewPager? = null
    private var listOfImages : ArrayList<String> = ArrayList()
    private var position : Int = 0


    private lateinit var mBinding: ActivityCommonSliderBinding

    override fun onPermissionsGranted(requestCode: Int) {
    }

    override fun contentView(): Int = R.layout.activity_common_slider

    override fun initUI(binding: ActivityCommonSliderBinding) {
        mBinding = binding

        listOfImages = intent.getStringArrayListExtra("user_images")!!
        position = intent.getIntExtra("position",0)

        mBinding.imgClose.setOnClickListener {
            finish()
        }

        initialise(listOfImages, position)

    }

    private fun initialise(it: ArrayList<String>?, position: Int) {
        imageModelArrayList = it
        imgPager = mBinding.imgPager
        imgPager?.adapter = CommonSliderAdapter(this, imageModelArrayList)
        imgPager?.setClipToPadding(false)
        imgPager?.setCurrentItem(position)
        imgPager?.setPageTransformer(false, ScalePageTransformer())

    }

}
