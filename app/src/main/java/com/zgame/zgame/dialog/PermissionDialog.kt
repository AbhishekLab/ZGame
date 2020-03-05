package com.zgame.zgame.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.zgame.zgame.R

class PermissionDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       val  builder =  AlertDialog.Builder(activity)

        val layoutInflator = activity!!.layoutInflater
        val view = layoutInflator.inflate(R.layout.permission_dialog,null)

        builder.setView(view).setTitle("Permission")

        return builder.create()
    }
}