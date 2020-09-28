package com.zgame.zgame.model

import android.content.Context
import com.zgame.zgame.R

class DrawerListModel {

    companion object {


        fun drawerRecyclerDataWithIcons(context: Context, hideIcon : Boolean): ArrayList<DataModelWithBoolean> {

            val menuHome = DataModelWithBoolean(
                context.getString(R.string.home),
                R.drawable.ic_home,
                true
            )
            val menuProfile =
                DataModelWithBoolean(
                    context.getString(R.string.profile),
                    R.drawable.ic_nav_profile,
                    hideIcon
                )
            val menuMail =
                DataModelWithBoolean(
                    context.getString(R.string.mail_box),
                    R.drawable.ic_mail,
                    true
                )
            val menuCollection = DataModelWithBoolean(
                context.getString(R.string.collection),
                R.drawable.ic_collections, true
            )
            val menuSetting = DataModelWithBoolean(
                context.getString(R.string.setting),
                R.drawable.ic_nav_setting, true
            )
            val menuSupport =
                DataModelWithBoolean(
                    context.getString(R.string.support),
                    R.drawable.ic_nav_support,
                    true
                )

            val menuAboutUs =
                DataModelWithBoolean(
                    context.getString(R.string.about_us),
                    R.drawable.ic_nav_about_us,
                    true
                )

            val menuLogout =
                DataModelWithBoolean(
                    context.getString(R.string.log_out),
                    R.drawable.ic_nav_logout,
                    hideIcon
                )

            val list = arrayListOf<DataModelWithBoolean>()

            if(hideIcon){
                list.add(menuHome)
                list.add(menuProfile)
                list.add(menuMail)
                list.add(menuCollection)
                list.add(menuSetting)
                list.add(menuSupport)
                list.add(menuAboutUs)
                list.add(menuLogout)
            }else{
                list.add(menuHome)
                list.add(menuMail)
                list.add(menuCollection)
                list.add(menuSetting)
                list.add(menuSupport)
                list.add(menuAboutUs)
            }

            return list
        }
    }
}

data class DataModelWithBoolean(var title: String, var icon: Int, var isChangeable: Boolean)