package com.zgame.zgame.fragment

import android.util.Log.d
import android.view.View
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.zgame.zgame.MainActivity
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseFragment
import com.zgame.zgame.databinding.FragmentUserGalleryBinding

class UserGalleryFragment : BaseFragment<FragmentUserGalleryBinding>() {

    private lateinit var mBinding: FragmentUserGalleryBinding

    override fun getContentView(): Int = R.layout.fragment_user_gallery

    override fun initView(binding: FragmentUserGalleryBinding) {
        mBinding = binding

        (activity as MainActivity).permission()

        if ((activity as MainActivity).checkAuthInstance()) {
            fetchUserResources()
        } else {
            fetchRandomResources()
        }
    }

    private fun fetchRandomResources() {
        d("response", "fetchRandomResources")
    }

    private fun fetchUserResources() {
        /* val localFile = File.createTempFile("images", "jpg")
        riversRef.getFile(localFile)
            .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                // Successfully downloaded data to local file
                // ...
            }).addOnFailureListener(OnFailureListener {
                // Handle failed download
                // ...
            })*/
        showToast("fetchUserResources")
        d("response", "fetchUserResources")
    }

    override fun initNav(view: View) {
    }
}




