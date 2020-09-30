package com.zgame.zgame.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log.e
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.base.PreferanceRepository
import com.zgame.zgame.contract.PostContract
import com.zgame.zgame.databinding.ActivityPostImageBinding
import com.zgame.zgame.editor.EmojiBSFragment
import com.zgame.zgame.editor.EmojiBSFragment.EmojiListener
import com.zgame.zgame.editor.ToolType
import com.zgame.zgame.presenter.PostPresenter
import com.zgame.zgame.utils.Constant
import com.zgame.zgame.utils.Constant.CAMERA_PERMISSION
import com.zgame.zgame.utils.Constant.PICK_REQUEST
import com.zgame.zgame.utils.Constant.SAVE_REQUEST
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditor.OnSaveListener
import ja.burhanrashid52.photoeditor.SaveSettings
import ja.burhanrashid52.photoeditor.ViewType
import java.io.File
import java.io.IOException


class PostImageActivity : BaseActivity<ActivityPostImageBinding>(), OnPhotoEditorListener,
    EmojiListener , PostContract.PostView {

    private var mEditor: PhotoEditor? = null
    private var alertDialog: AlertDialog? = null
    private var mEmojiBSFragment: EmojiBSFragment? = null
    private var profilePhoto: Uri? = null
    private var mSaveImageUri: Uri? = null
    private lateinit var postPresenter:PostPresenter
    private var userUniqueName : String? = null

    private lateinit var mBinding: ActivityPostImageBinding

    override fun onPermissionsGranted(requestCode: Int) {
        when (requestCode) {
            PICK_REQUEST -> openGallery()

            CAMERA_PERMISSION -> openCamera()

            SAVE_REQUEST -> saveImage()
        }
    }


    override fun contentView(): Int = R.layout.activity_post_image

    override fun initUI(binding: ActivityPostImageBinding) {
        mBinding = binding

        postPresenter = PostPresenter(this)
        userUniqueName = PreferanceRepository.getString(Constant.uniqueName)

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y

        mEmojiBSFragment = EmojiBSFragment()
        mEmojiBSFragment?.setEmojiListener(this)

        mEditor = PhotoEditor.Builder(this, mBinding.imgPicEdit)
            .setPinchTextScalable(true)
            .build()


        mEditor?.setOnPhotoEditorListener(this)

        mBinding.imgPicEdit.layoutParams.height = height

        Glide.with(this).load(R.drawable.gradient_demo).apply {
        }.into(mBinding.imgPicEdit.source)


        mBinding.cvGallery.setOnClickListener {
            mBinding.imgBrush.alpha = 0.6f
            mBinding.imgEraser.visibility = View.GONE
            mBinding.imgEraser.visibility = View.GONE
            requestAppPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), R.string.permission_text, PICK_REQUEST
            )
        }

        mBinding.cvShareNSave.setOnClickListener {
            requestAppPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), R.string.permission_text, SAVE_REQUEST
            )
        }

        mBinding.cvCamera.setOnClickListener {
            mBinding.imgBrush.alpha = 0.6f
            mBinding.imgEraser.visibility = View.GONE
            mBinding.imgEraser.visibility = View.GONE

            requestAppPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), R.string.permission_text, CAMERA_PERMISSION
            )
        }

        mBinding.imgEmoji.setOnClickListener {
            mBinding.imgBrush.alpha = 0.6f
            mBinding.imgEmoji.alpha = 1f
            mBinding.imgEraser.alpha = 0.6f
            mBinding.imgEraser.visibility = View.GONE
            mEditor?.setBrushDrawingMode(false)
            mEmojiBSFragment?.show(supportFragmentManager, mEmojiBSFragment?.tag)
        }

        mBinding.imgBrush.setOnClickListener {
            mBinding.imgBrush.alpha = 1f
            mBinding.imgEmoji.alpha = 0.6f
            mBinding.imgEraser.alpha = 0.6f

            mBinding.imgEraser.visibility = View.VISIBLE

            chooseColor()

            uiEditing(ToolType.BRUSH)
        }

        mBinding.imgEraser.setOnClickListener {
            mBinding.imgBrush.alpha = 0.6f
            mBinding.imgEmoji.alpha = 0.6f
            mBinding.imgEraser.alpha = 1f
            uiEditing(ToolType.ERASER)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST)
    }


    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_PERMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_REQUEST -> {
                    try {
                        mEditor?.clearAllViews()
                        mEditor?.setBrushDrawingMode(false)
                        val uri = data!!.data
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                        mBinding.imgPicEdit.source.setImageBitmap(bitmap)
                    } catch (e: IOException) {
                        e("Errorr", e.message)
                    }
                }

                CAMERA_PERMISSION -> {
                    mEditor?.clearAllViews()
                    mEditor?.setBrushDrawingMode(false)

                    val imgInBitmapDrawable: Bitmap = (data!!.extras!!["data"] as Bitmap?)!!
                    //val bytes = ByteArrayOutputStream()
                    //imgInBitmapDrawable.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val path = MediaStore.Images.Media.insertImage(
                        contentResolver,
                        imgInBitmapDrawable,
                        "Gallery",
                        null
                    )
                    profilePhoto = Uri.parse(path.toString())

                    e("ImageUrl", profilePhoto.toString())

                    // Glide.with(this).load(imgInBitmapDrawable).into(mBinding.imgPicEdit.source)
                    mBinding.imgPicEdit.source.setImageURI(profilePhoto)
                }
            }
        }
    }

    private fun uiEditing(toolType: ToolType) {
        when (toolType) {
            ToolType.BRUSH -> {
                mEditor?.setBrushDrawingMode(true)
                mEditor?.brushSize = 10f
            }
            ToolType.ERASER -> {
                mEditor?.brushEraser()
            }
        }
    }

    override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
        e("onEdit", "onEditTextChangeListener")
    }

    override fun onStartViewChangeListener(viewType: ViewType?) {
        e("onStart", "onEditTextChangeListener")
    }

    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        e("onRemove", "onEditTextChangeListener")
    }

    override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        e("onAdd", "onEditTextChangeListener")
    }

    override fun onStopViewChangeListener(viewType: ViewType?) {
        e("onStop", "onEditTextChangeListener")
    }

    private fun chooseColor() {
        val dialogBuilder = AlertDialog.Builder(this)
        val layoutView: View = layoutInflater.inflate(R.layout.dialog_color_choose, null)

        val color1 = layoutView.findViewById<View>(R.id.view_blue)
        val color2 = layoutView.findViewById<View>(R.id.view_light_brown)
        val color3 = layoutView.findViewById<View>(R.id.view_green)
        val color4 = layoutView.findViewById<View>(R.id.view_orange)
        val color5 = layoutView.findViewById<View>(R.id.view_red)
        val color6 = layoutView.findViewById<View>(R.id.view_black)
        val color7 = layoutView.findViewById<View>(R.id.view_red_orange)
        val color8 = layoutView.findViewById<View>(R.id.view_sky_blue)
        val color9 = layoutView.findViewById<View>(R.id.view_violet)
        val color10 = layoutView.findViewById<View>(R.id.view_white)
        val color11 = layoutView.findViewById<View>(R.id.view_yellow)
        val color12 = layoutView.findViewById<View>(R.id.view_yellow_green)

        dialogBuilder.setView(layoutView)
        alertDialog = dialogBuilder.create()
        alertDialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog?.show()

        color1.setOnClickListener {
            mEditor?.brushColor = resources.getColor(R.color.blue_color_picker)
            alertDialog?.dismiss()
        }
        color2.setOnClickListener {
            mEditor?.brushColor = resources.getColor(R.color.brown_color_picker)
            alertDialog?.dismiss()
        }
        color3.setOnClickListener {
            mEditor?.brushColor = resources.getColor(R.color.green_color_picker)
            alertDialog?.dismiss()
        }
        color4.setOnClickListener {
            mEditor?.brushColor = resources.getColor(R.color.orange_color_picker)
            alertDialog?.dismiss()
        }
        color5.setOnClickListener {
            mEditor?.brushColor = resources.getColor(R.color.red_color_picker)
            alertDialog?.dismiss()
        }
        color6.setOnClickListener {
            mEditor?.brushColor = resources.getColor(R.color.black)
            alertDialog?.dismiss()
        }
        color7.setOnClickListener {
            mEditor?.brushColor = resources.getColor(R.color.red_orange_color_picker)
            alertDialog?.dismiss()
        }
        color8.setOnClickListener {
            mEditor?.brushColor = resources.getColor(R.color.sky_blue_color_picker)
            alertDialog?.dismiss()
        }
        color9.setOnClickListener {
            mEditor?.brushColor = resources.getColor(R.color.violet_color_picker)
            alertDialog?.dismiss()
        }
        color10.setOnClickListener {
            mEditor?.brushColor = resources.getColor(R.color.white)
            alertDialog?.dismiss()
        }
        color11.setOnClickListener {
            mEditor?.brushColor = resources.getColor(R.color.yellow_color_picker)
            alertDialog?.dismiss()
        }
        color12.setOnClickListener {
            mEditor?.brushColor = resources.getColor(R.color.yellow_green_color_picker)
            alertDialog?.dismiss()
        }
    }

    override fun onEmojiClick(emojiUnicode: String?) {
        mEditor?.addEmoji(emojiUnicode)
    }


    private fun saveImage() {
        mBinding.progressBar.visibility = View.VISIBLE
        var success = true
        var file: File? = null
        val folder = File(Environment.getExternalStorageDirectory(), "UsersGallery")

        if (!folder.exists()) {
            success = folder.mkdirs()
        }
        file = if (success) {
            File(folder.toString() + File.separator + "" + System.currentTimeMillis() + ".png")
        } else {

            File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + ""
                        + System.currentTimeMillis() + ".png"
            )
        }


        try {
            file!!.createNewFile()
            val saveSettings = SaveSettings.Builder()
                .setClearViewsEnabled(false)
                .setTransparencyEnabled(true)
                .build()

            mEditor?.saveAsFile(file.absolutePath, saveSettings, object : OnSaveListener {
                override fun onSuccess(imagePath: String) {

                    mSaveImageUri = Uri.fromFile(File(imagePath))
                    postPresenter.postImage(mSaveImageUri,userUniqueName)
                }

                override fun onFailure(exception: Exception) {
                    mBinding.progressBar.visibility = View.GONE
                    showSnackbar("Failed to save Image")
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
            mBinding.progressBar.visibility = View.GONE
            showSnackbar(e.message!!)
        }
    }

    private fun shareImage() {
        if (mSaveImageUri == null) {
            showSnackbar(getString(R.string.msg_save_image_to_share))
            return
        }
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, buildFileProviderUri(mSaveImageUri!!))
        startActivity(Intent.createChooser(intent, getString(R.string.msg_share_image)))
    }

    private fun buildFileProviderUri(uri: Uri): Uri? {
        return FileProvider.getUriForFile(
            this,
            GalleryActivity.FILE_PROVIDER_AUTHORITY,
            File(uri.path)
        )
    }

    override fun postSuccess() {
        mBinding.progressBar.visibility = View.GONE
        showToast("Image upload successfully")
        finish()
    }

    override fun postFailed(message: String) {
        mBinding.progressBar.visibility = View.GONE
        showToast(message)
    }
}
