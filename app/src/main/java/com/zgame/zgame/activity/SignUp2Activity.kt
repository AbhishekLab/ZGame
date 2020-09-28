package com.zgame.zgame.activity

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zgame.zgame.R
import com.zgame.zgame.adapter.SignUp2Adapter
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.databinding.ActivitySignUp2Binding
import com.zgame.zgame.model.AgeModule
import com.zgame.zgame.model.GenderModule
import com.zgame.zgame.model.Location
import com.zgame.zgame.model.SeekingModule
import com.zgame.zgame.presenter.SignUp2Presenter
import com.zgame.zgame.utils.Constant

class SignUp2Activity : BaseActivity<ActivitySignUp2Binding>(), SignUp2Adapter.StateU {

    lateinit var mBinding: ActivitySignUp2Binding

    private var myAgeAdapter: SignUp2Adapter? = null
    private var myHeightAdapter: SignUp2Adapter? = null
    private var myLocationAdapter: SignUp2Adapter? = null

    private var age: ArrayList<GenderModule>? = null
    private var height: ArrayList<GenderModule>? = null
    private var location: ArrayList<GenderModule>? = ArrayList()
    private var locationState: ArrayList<GenderModule>? = null
    private var stateName: Array<String>? = null
    private var isState: Boolean = false

    private var selectedAge: String? = null
    private var selectedHeight: String? = null
    private var selectedCountry: String? = null
    private var selectedState: String? = null

    private var male: String? = ""
    private var female: String? = ""
    private var coupleFM: String? = ""
    private var coupleFF: String? = ""
    private var coupleMM: String? = ""

    private var ageSelectedValue: ArrayList<AgeModule>? = ArrayList()
    private var genderSelectedValue: ArrayList<GenderModule>? = ArrayList()
    private var seekingSelectedValue: ArrayList<SeekingModule>? = ArrayList()

    override fun onPermissionsGranted(requestCode: Int) {
    }

    override fun contentView() = R.layout.activity_sign_up2

    override fun initUI(binding: ActivitySignUp2Binding) {
        mBinding = binding
        age = ArrayList()
        height = ArrayList()

        genderSelectedValue = intent.getParcelableArrayListExtra("I_Am")
        seekingSelectedValue = intent.getParcelableArrayListExtra("Seeking")
        ageSelectedValue = intent.getParcelableArrayListExtra("Age_Range")

       /* male = intent.getStringExtra(Constant.male)
            female = intent.getStringExtra(Constant.female)
        coupleFF = intent.getStringExtra(Constant.coupleFF)
        coupleFM = intent.getStringExtra(Constant.coupleFM)
        coupleMM = intent.getStringExtra(Constant.coupleMM)*/



        mBinding.toolbar.imgBack.setOnClickListener { finish() }
        mBinding.edtSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)

        for (i in 18..70) {
            age?.add(GenderModule(false, i.toString()))
        }

        for (j in Location.getHeight()) {
            height?.add(GenderModule(false, j))
        }

        for (k in Location.getLocation()) {
            location?.add(GenderModule(false, k))
        }

        mBinding.rvMyAge.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        mBinding.rvHeight.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        mBinding.rvLocation.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        myAgeAdapter = SignUp2Adapter(age, this, "age")

        mBinding.llMyAge.setOnClickListener {
            mBinding.rvMyAge.adapter = myAgeAdapter

            if (mBinding.rvMyAge.visibility == View.VISIBLE) {
                mBinding.rvMyAge.visibility = View.GONE
            } else {
                mBinding.rvMyAge.visibility = View.VISIBLE
            }
        }

        myHeightAdapter = SignUp2Adapter(height, this, "height")

        mBinding.llHeight.setOnClickListener {
            mBinding.rvHeight.adapter = myHeightAdapter

            if (mBinding.rvHeight.visibility == View.VISIBLE) {
                mBinding.rvHeight.visibility = View.GONE
            } else {
                mBinding.rvHeight.visibility = View.VISIBLE
            }
        }

        myLocationAdapter = SignUp2Adapter(location, this, "country")
        mBinding.llLocation.setOnClickListener {
            mBinding.rvLocation.adapter = myLocationAdapter
            if (mBinding.rvLocation.visibility == View.VISIBLE) {
                mBinding.rvLocation.visibility = View.GONE
                mBinding.edtSearch.visibility = View.GONE
            } else {
                mBinding.rvLocation.visibility = View.VISIBLE
                mBinding.edtSearch.visibility = View.VISIBLE
            }
        }

        mBinding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {

                if (editable.toString().isNotBlank()) {
                    filterItem(editable.toString())
                }
            }
        })

        mBinding.edtSearch.setDrawableClickListener {
            if (isState) {
                isState = false
                mBinding.edtSearch.setText("")
                mBinding.edtSearch.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_search,
                    0,
                    0,
                    0
                )
                locationState?.clear()
                mBinding.edtSearch.hint = resources.getString(R.string.search_country)
                mBinding.rvLocation.adapter = myLocationAdapter
                myLocationAdapter?.setCountry(location, "country")
            }
        }

        mBinding.btnSubmit.setOnClickListener {
            if (selectedAge != null && selectedHeight != null && selectedCountry != null && selectedState != null) {
                startActivity(
                    Intent(this, SignUp3Activity::class.java)
                        .putExtra("I_Am", genderSelectedValue)
                        .putExtra("Seeking", seekingSelectedValue)
                        .putExtra("Age_Range", ageSelectedValue)
                        .putExtra("Age", selectedAge)
                        .putExtra("Height", selectedHeight)
                        .putExtra("Country", selectedCountry)
                        .putExtra("State", selectedState)
                )
            } else {
                showToast("please select AGE, HEIGHT and LOCATION for more filter option")
            }
        }
    }

    private fun filterItem(filterItem: String) {
        if (mBinding.edtSearch.hint == resources.getString(R.string.search_country)) {
            if (location!!.isEmpty()) {
                myLocationAdapter!!.filterList(location!!)
            } else {
                val arr: ArrayList<GenderModule> = ArrayList()
                for (item in location!!) {

                    if (item.name?.toLowerCase()?.contains(filterItem.toLowerCase())!!) {
                        arr.add(item)
                    }
                }
                if (arr.size > 0 || arr.isNotEmpty()) {
                    myLocationAdapter!!.filterList(arr)
                }
            }
        } else {
            if (locationState!!.isEmpty()) {
                myLocationAdapter!!.filterList(locationState!!)
            } else {
                val arr: ArrayList<GenderModule> = ArrayList()
                for (item in locationState!!) {

                    if (item.name?.toLowerCase()?.contains(filterItem.toLowerCase())!!) {
                        arr.add(item)
                    }
                }
                if (arr.size > 0 || arr.isNotEmpty()) {
                    myLocationAdapter!!.filterList(arr)
                }
            }
        }
    }

    override fun getState(position: Int) {
        isState = true
        mBinding.edtSearch.setText("")
        locationState = ArrayList()
        mBinding.edtSearch.hint = resources.getString(R.string.search_state)
        mBinding.edtSearch.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_back_black,
            0,
            0,
            0
        )
        stateName = SignUp2Presenter.getState(position)

        for (i in stateName!!) {
            locationState?.add(GenderModule(false, i))
        }
        myLocationAdapter = SignUp2Adapter(location, this, "state")
        mBinding.rvLocation.adapter = myLocationAdapter
        myLocationAdapter?.setState(locationState)

    }

    override fun selectedAge(age: String) {
        selectedAge = age
    }

    override fun selectedHeight(height: String) {
        selectedHeight = height
    }

    override fun selectedCountry(country: String) {
        selectedCountry = country
    }

    override fun selectedState(state: String) {
        selectedState = state
    }
}
