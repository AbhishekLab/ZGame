package com.zgame.zgame.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zgame.zgame.R
import com.zgame.zgame.adapter.AgeAdapter
import com.zgame.zgame.adapter.GenderAdapter
import com.zgame.zgame.adapter.SeekingAdapter
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.databinding.ActivitySignUp1Binding
import com.zgame.zgame.model.AgeModule
import com.zgame.zgame.model.GenderModule
import com.zgame.zgame.model.SeekingModule

class SignUp1Activity : BaseActivity<ActivitySignUp1Binding>() {

    lateinit var mBinding: ActivitySignUp1Binding

    private var seekingAdapter: SeekingAdapter? = null
    private var ageAdapter: AgeAdapter? = null
    private var genderAdapter: GenderAdapter? = null

    private var seekingSelectedValue: ArrayList<SeekingModule>? = null
    private var ageSelectedValue: ArrayList<AgeModule>? = null
    private var genderSelectedValue: ArrayList<GenderModule>? = ArrayList()

    override fun onPermissionsGranted(requestCode: Int) {
    }

    override fun contentView() = R.layout.activity_sign_up1

    override fun initUI(binding: ActivitySignUp1Binding) {
        mBinding = binding

        val seeking: ArrayList<SeekingModule> = ArrayList()
        seeking.add(SeekingModule(false, resources.getString(R.string.female)))
        seeking.add(SeekingModule(false, resources.getString(R.string.male)))
        seeking.add(SeekingModule(false, resources.getString(R.string.couple_fm)))
        seeking.add(SeekingModule(false, resources.getString(R.string.couple_ff)))
        seeking.add(SeekingModule(false, resources.getString(R.string.couple_mm)))
        seekingAdapter = SeekingAdapter(this, seeking)
        mBinding.rvSeeking.adapter = seekingAdapter

        mBinding.rvSeeking.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        mBinding.llSeeking.setOnClickListener {

            if (mBinding.rvSeeking.visibility == View.VISIBLE) {
                mBinding.tvMultiSelection.visibility = View.GONE
                mBinding.rvSeeking.visibility = View.GONE
            } else {
                mBinding.tvMultiSelection.visibility = View.VISIBLE
                mBinding.rvSeeking.visibility = View.VISIBLE
            }
        }

        val age: ArrayList<AgeModule> = ArrayList()
        age.add(AgeModule(false, resources.getString(R.string.one)))
        age.add(AgeModule(false, resources.getString(R.string.two)))
        age.add(AgeModule(false, resources.getString(R.string.three)))
        age.add(AgeModule(false, resources.getString(R.string.four)))
        age.add(AgeModule(false, resources.getString(R.string.five)))
        ageAdapter = AgeAdapter(this, age)
        mBinding.rvAge.adapter = ageAdapter

        mBinding.rvAge.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        mBinding.llAge.setOnClickListener {

            if (mBinding.rvAge.visibility == View.VISIBLE) {
                mBinding.tvMultiSelectionAge.visibility = View.GONE
                mBinding.rvAge.visibility = View.GONE
            } else {
                mBinding.tvMultiSelectionAge.visibility = View.VISIBLE
                mBinding.rvAge.visibility = View.VISIBLE
            }
        }

        val gender: ArrayList<GenderModule> = ArrayList()
        gender.add(GenderModule(false, resources.getString(R.string.i_am_male)))
        gender.add(GenderModule(false, resources.getString(R.string.i_am_female)))
        gender.add(GenderModule(false, resources.getString(R.string.i_am_couple_fm)))
        gender.add(GenderModule(false, resources.getString(R.string.i_am_couple_ff)))
        gender.add(GenderModule(false, resources.getString(R.string.i_am_couple_mm)))

        genderAdapter = GenderAdapter(this,gender)
        mBinding.rvGender.adapter = genderAdapter

        mBinding.rvGender.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        mBinding.llGender.setOnClickListener {

            if (mBinding.rvGender.visibility == View.VISIBLE) {
                mBinding.rvGender.visibility = View.GONE
            } else {
                mBinding.rvGender.visibility = View.VISIBLE
            }
        }

        mBinding.btnSubmit.setOnClickListener {
            genderSelectedValue?.clear()
            seekingSelectedValue = seekingAdapter?.getSelected()
            ageSelectedValue = ageAdapter?.getSelected()
            genderSelectedValue?.add(genderAdapter?.getSelected()!!)

            if (seekingSelectedValue?.size != 0 && ageSelectedValue?.size != 0 && genderSelectedValue?.size != 0) {
                startActivity(Intent(this,SignUp2Activity::class.java)
                    .putExtra("I_Am",genderSelectedValue)
                    .putExtra("Seeking",seekingSelectedValue)
                    .putExtra("Age_Range",ageSelectedValue))
            } else {
                showToast("please select SEEKING and AGE for more filter option")
            }
        }

        mBinding.toolbar.imgBack.setOnClickListener { finish() }
    }
}