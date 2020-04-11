package com.zgame.zgame.presenter

import com.zgame.zgame.model.Location

object SignUp2Presenter {

    private var stateName : Array<String> ? = null
    fun getState(position : Int): Array<String>{

        when(position){
            0 ->{stateName =  Location.demoArr()}
            1 ->{
                stateName = Location.Angola()}
            2 ->{}
            3 ->{}
            4 ->{}
        }
        return stateName!!
    }
}