package com.zgame.zgame.presenter

import com.anupcowkur.reservoir.Reservoir
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.contract.CustomerContract
import com.zgame.zgame.model.CircleData
import com.zgame.zgame.model.SeekingModule
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.utils.Constant
import com.zgame.zgame.utils.Constant.DbName
import java.lang.NullPointerException
import java.lang.reflect.Type


class CustomerPresenter (private val view: CustomerContract.CustomerView) : CustomerContract.CustomerPresenter {

    private var databaseRef: DatabaseReference? = null
    private var circleProfile : ArrayList<CircleData>? = ArrayList()
    private var userList : ArrayList<SignUpModel>? = ArrayList()

    private var db : FirebaseFirestore? = null
    private var userNameQuery : CollectionReference? = null

    private var male = ""
    private var female = ""
    private var coupleFF = ""
    private var coupleMM = ""
    private var coupleFM = ""

    private var type : Type? = null
    private var seekingSelectedValue: List<SeekingModule>? = ArrayList()
    private var currentUserData : SignUpModel? = SignUpModel()

    private var images : ArrayList<String> = ArrayList()

    override fun customerRandomList() {

    }

    override fun usersFilterList() {
        sortedUsersData()
    }

    override fun getContactRandomImages() {
       /* databaseRef = FirebaseDatabase.getInstance().getReference("RandomPic")
        databaseRef?.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
              view.getNullValue(p0.message)
            }
            override fun onDataChange(p0: DataSnapshot) {
                for(i in p0.children.iterator()){
                   images.add(i.getValue(String::class.java)!!)
                }
                view.setContactImages(images)
            }
        })*/
    }




    private fun sortedUsersData(){
        userList?.clear()
        databaseRef = FirebaseDatabase.getInstance().reference.child(DbName)

        db = FirebaseFirestore.getInstance()
        userNameQuery = db?.collection(DbName)

        try{
            currentUserData =   Reservoir.get(Constant.reservoir_key, SignUpModel::class.java)
        }catch (e:NullPointerException){}



        val res = PreferanceRepository.getString("SeekingValue")
        type = object : TypeToken<ArrayList<SeekingModule>>() {}.type
        seekingSelectedValue = Gson().fromJson(res, type)

        if(seekingSelectedValue.isNullOrEmpty()){
            currentUserData?.seeking?.forEach {
                when (it) {
                    "Male" -> {
                        male = it
                    }
                    "Female" -> {
                        female = it
                    }
                    "Couple(FF)" -> {
                        coupleFF = it
                    }
                    "Couple(FM)" -> {
                        coupleFM = it
                    }
                    "Couple(MM)" -> {
                        coupleMM = it
                    }
                }
            }
        }else{
            seekingSelectedValue?.forEach {
                when (it.name) {
                    "Male" -> {
                        male = it.name.toString()
                    }
                    "Female" -> {
                        female = it.name.toString()
                    }
                    "Couple(FF)" -> {
                        coupleFF = it.name.toString()
                    }
                    "Couple(FM)" -> {
                        coupleFM = it.name.toString()
                    }
                    "Couple(MM)" -> {
                        coupleMM = it.name.toString()
                    }
                }
            }
        }

        userNameQuery?.whereArrayContainsAny("seeking", listOf(male, female, coupleFF, coupleFM, coupleMM))?.limit(20)?.get()?.addOnCompleteListener {
            for(a in it.result!!.iterator()){
                userList?.add(a.toObject(SignUpModel::class.java))
            }
            view.getUsersFilterList(userList)
        }?.addOnFailureListener {
            view.getNullValue(it.message!!)
        }
    }

    override fun circleProfile() {
        circleProfile?.clear()
        databaseRef = FirebaseDatabase.getInstance().getReference("Circle")

        databaseRef!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                view.getNullValue(p0.message)
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (userData in p0.children.iterator()) {
                    circleProfile?.add(userData.getValue(CircleData::class.java)!!)
                }
                view.getCircleProfileData(circleProfile)
            }
        })
    }

}