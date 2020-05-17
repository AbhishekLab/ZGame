package com.zgame.zgame.model

import android.content.Context
import com.zgame.zgame.R

class DrawerListModel {

    companion object {


        fun drawerRecyclerDataWithIcons(context: Context): ArrayList<DataModelWithBoolean> {

            val menuHome = DataModelWithBoolean(
                context.getString(R.string.home),
                R.drawable.ic_home,
                false
            )
            val menuProfile =
                DataModelWithBoolean(
                    context.getString(R.string.profile),
                    R.drawable.ic_nav_profile,
                    false
                )
            val menuMail =
                DataModelWithBoolean(
                    context.getString(R.string.mail_box),
                    R.drawable.ic_mail,
                    false
                )
            val menuCollection = DataModelWithBoolean(
                context.getString(R.string.collection),
                R.drawable.ic_collections, false
            )
            val menuSetting = DataModelWithBoolean(
                context.getString(R.string.setting),
                R.drawable.ic_nav_setting, false
            )
            val menuSupport =
                DataModelWithBoolean(
                    context.getString(R.string.support),
                    R.drawable.ic_nav_support,
                    false
                )

            val menuAboutUs =
                DataModelWithBoolean(
                    context.getString(R.string.about_us),
                    R.drawable.ic_nav_about_us,
                    false
                )

            val menuLogout =
                DataModelWithBoolean(
                    context.getString(R.string.log_out),
                    R.drawable.ic_nav_logout,
                    false
                )

            val list = arrayListOf<DataModelWithBoolean>()
            list.add(menuHome)
            list.add(menuProfile)
            list.add(menuMail)
            list.add(menuCollection)
            list.add(menuSetting)
            list.add(menuSupport)
            list.add(menuAboutUs)
            list.add(menuLogout)
            return list
        }
    }
}

data class DataModelWithBoolean(var title: String, var icon: Int, var isChangeable: Boolean)