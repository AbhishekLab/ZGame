package com.zgame.zgame.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.contract.PostContract
import com.zgame.zgame.databinding.ActivityUploadImageBinding
import com.zgame.zgame.editor.EmojiBSFragment
import com.zgame.zgame.editor.ToolType
import com.zgame.zgame.presenter.PostPresenter
import com.zgame.zgame.utils.Constant
import ja.burhanrashid52.photoeditor.PhotoEditor
import java.io.File
import java.io.IOException

class UploadImageActivity : BaseActivity<ActivityUploadImageBinding>(),
    EmojiBSFragment.EmojiListener, PostContract.PostView  {

    private lateinit var mBinding: ActivityUploadImageBinding
    private var addedImageUri: Uri? = null
    private var mEditor: PhotoEditor? = null
    private var mEmojiBSFragment: EmojiBSFragment? = null
    private var mSaveImageUri: Uri? = null
    private lateinit var postPresenter: PostPresenter
    private var userUniqueName : String? = null

    override fun contentView() = R.layout.activity_upload_image

    override fun onPermissionsGranted(requestCode: Int) {
        when (requestCode) {
            Constant.PICK_REQUEST -> openGallery()

            Constant.CAMERA_PERMISSION -> openCamera()

            //Constant.SAVE_REQUEST -> saveImage()
        }
    }

    override fun initUI(binding: ActivityUploadImageBinding) {
        mBinding = binding

        postPresenter = PostPresenter(this)
        userUniqueName = PreferanceRepository.getString(Constant.uniqueName)

        mEmojiBSFragment = EmojiBSFragment()
        mEmojiBSFragment?.setEmojiListener(this)

        Glide.with(this).load(R.drawable.gradient_demo).into(mBinding.imgAddImage)

        mBinding.cvGallery.setOnClickListener {
            requestAppPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), R.string.permission_text, Constant.PICK_REQUEST
            )
        }

        mBinding.cvShareNSave.setOnClickListener {
            if(mSaveImageUri == null){
                mSaveImageUri = Uri.fromFile(File("file:///android_asset/gradient_demo.png"))
            }
            postPresenter.postImage(mSaveImageUri,userUniqueName)
        }

        mBinding.cvCamera.setOnClickListener {
            requestAppPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), R.string.permission_text, Constant.CAMERA_PERMISSION
            )
        }

        mBinding.imgEmoji.setOnClickListener {
            mBinding.imgEmoji.alpha = 1f
            mEmojiBSFragment?.show(supportFragmentManager, mEmojiBSFragment?.tag)
        }

    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            Constant.PICK_REQUEST
        )
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, Constant.CAMERA_PERMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constant.PICK_REQUEST -> {
                    try {
                        val uri = data!!.data
                        mSaveImageUri = uri
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                        Glide.with(this).load(bitmap).into(mBinding.imgAddImage)
                    } catch (e: IOException) {
                        Log.e("Errorr", e.message)
                    }
                }

                Constant.CAMERA_PERMISSION -> {
                    val imgInBitmapDrawable: Bitmap = (data!!.extras!!["data"] as Bitmap?)!!
                    //val bytes = ByteArrayOutputStream()
                    //imgInBitmapDrawable.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val path = MediaStore.Images.Media.insertImage(
                        contentResolver,
                        imgInBitmapDrawable,
                        "Gallery",
                        null
                    )
                    addedImageUri = Uri.parse(path.toString())
                    mSaveImageUri = addedImageUri

                    Log.e("ImageUrl", addedImageUri.toString())
                    Glide.with(this).load(addedImageUri).into(mBinding.imgAddImage)
                }
            }
        }
    }

    override fun onEmojiClick(emojiUnicode: String?) {

    }

    override fun postSuccess() {
        showToast("Sucess")
    }

    override fun postFailed(message: String) {
        showToast("Failed")
    }
}