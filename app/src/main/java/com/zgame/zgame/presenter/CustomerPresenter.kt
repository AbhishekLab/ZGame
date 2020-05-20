package com.zgame.zgame.presenter

import com.anupcowkur.reservoir.Reservoir
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.contract.CustomerContract
import com.zgame.zgame.model.ContactRandomData
import com.zgame.zgame.model.SignUpModel
import com.zgame.zgame.utils.Constant
import com.zgame.zgame.utils.Constant.DbName
import java.lang.NullPointerException


class CustomerPresenter (private val view: CustomerContract.CustomerView) : CustomerContract.CustomerPresenter {

    private var databaseRef: DatabaseReference? = null
    private var allCustomerResponse : ArrayList<ContactRandomData>? = ArrayList()
    private var userList : ArrayList<SignUpModel>? = ArrayList()

    private var db : FirebaseFirestore? = null
    private var userNameQuery : CollectionReference? = null
    private var maleQuery : Query? = null
    private var femaleQuery : Query? = null
    private var coupleFFQuery : Query? = null
    private var coupleMMQuery : Query? = null
    private var coupleFMQuery : Query? = null

    private var male: String? = ""
    private var female: String? = ""
    private var coupleFM: String? = ""
    private var coupleFF: String? = ""
    private var coupleMM: String? = ""
    private var currentUserData : SignUpModel? = SignUpModel()

    private var images : ArrayList<String> = ArrayList()

    override fun customerRandomList() {
        allCustomerResponse?.clear()
        databaseRef = FirebaseDatabase.getInstance().getReference("Customers")

            databaseRef!!.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    view.getNullValue(p0.message)
                }
                override fun onDataChange(p0: DataSnapshot) {
                    for (userData in p0.children.iterator()) {
                        allCustomerResponse?.add(userData.getValue(ContactRandomData::class.java)!!)
                    }
                view.getCustomerRandomList(p0)
                }
            })
    }

    override fun usersFilterList() {
        sortedUsersData()
    }

    override fun getContactRandomImages() {
        databaseRef = FirebaseDatabase.getInstance().getReference("RandomPic")
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
        })
    }


    private fun sortedUsersData(){
        userList?.clear()
        databaseRef = FirebaseDatabase.getInstance().reference.child(DbName)

        db = FirebaseFirestore.getInstance()
        userNameQuery = db?.collection(DbName)

        try{
            currentUserData =   Reservoir.get(Constant.reservoir_key, SignUpModel::class.java)
        }catch (e:NullPointerException){}


        male = PreferanceRepository.getString(Constant.male)
        female  = PreferanceRepository.getString(Constant.female)
        coupleFM = PreferanceRepository.getString(Constant.coupleFM)
        coupleFF = PreferanceRepository.getString(Constant.coupleFF)
        coupleMM = PreferanceRepository.getString(Constant.coupleMM)


        if(male!=null){
            maleQuery = userNameQuery?.whereEqualTo(Constant.male , male)
        }
        if(female!=null || currentUserData?.female!=null){
            femaleQuery = userNameQuery?.whereEqualTo(Constant.female,female)
        }
        if(coupleFF!=null || currentUserData?.coupleFF!=null){
            coupleFFQuery = userNameQuery?.whereEqualTo(Constant.coupleFF,coupleFF)
        }
        if(coupleMM!=null || currentUserData?.coupleMM!=null){
            coupleMMQuery = userNameQuery?.whereEqualTo(Constant.coupleMM, coupleMM)
        }
        if(coupleFM!=null || currentUserData?.coupleFM!=null){
            coupleFMQuery = userNameQuery?.whereEqualTo(Constant.coupleFM, coupleFM)
        }

        val combinedTask: Task<*> = Tasks.whenAllSuccess<QuerySnapshot>(maleQuery?.get(),femaleQuery?.get(), coupleMMQuery?.get(),
            coupleFFQuery?.get(),coupleFMQuery?.get()).addOnSuccessListener {

        }.addOnFailureListener {
            view.getNullValue(it.message!!)
        }.addOnCompleteListener {
            for (queryDocumentSnapshots in it.result!!.iterator()) {
                for (documentSnapshot in queryDocumentSnapshots) {
                    userList?.add(documentSnapshot.toObject(SignUpModel::class.java))
                }
            }
            view.getUsersFilterList(userList)

        }
    }
}