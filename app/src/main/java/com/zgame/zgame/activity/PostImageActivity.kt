package com.zgame.zgame.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.util.Log.e
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.databinding.ActivityPostImageBinding
import com.zgame.zgame.editor.EmojiBSFragment
import com.zgame.zgame.editor.EmojiBSFragment.EmojiListener
import com.zgame.zgame.editor.ToolType
import com.zgame.zgame.utils.Constant
import com.zgame.zgame.utils.Constant.PICK_REQUEST
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.ViewType
import java.io.IOException


class PostImageActivity : BaseActivity<ActivityPostImageBinding>(), OnPhotoEditorListener,
    EmojiListener {

    private var mEditor: PhotoEditor? = null
    private var alertDialog: AlertDialog? = null
    private var mEmojiBSFragment : EmojiBSFragment? = null

    private lateinit var mBinding: ActivityPostImageBinding

    override fun onPermissionsGranted(requestCode: Int) {
        when (requestCode) {
            PICK_REQUEST -> openGallery()
        }
    }

    override fun contentView(): Int = R.layout.activity_post_image

    override fun initUI(binding: ActivityPostImageBinding) {
        mBinding = binding

        mEmojiBSFragment = EmojiBSFragment()
        mEmojiBSFragment?.setEmojiListener(this)

        mEditor = PhotoEditor.Builder(this, mBinding.imgPicEdit)
            .setPinchTextScalable(true)
            .build()


        mEditor?.setOnPhotoEditorListener(this)

        Glide.with(this).load(R.drawable.gradient_2_image).apply {
            mBinding.imgPicEdit.scaleY = 2.0f
        }.into(mBinding.imgPicEdit.source)


        mBinding.cvGallery.setOnClickListener {
            mBinding.imgBrush.alpha = 0.6f
            mBinding.imgEraser.visibility = View.GONE
            mBinding.imgEraser.visibility = View.GONE
            requestAppPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), R.string.permission_text, PICK_REQUEST
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
                        mBinding.imgPicEdit.scaleY = 1.0f
                        mBinding.imgPicEdit.scaleX = 1.05f
                        mBinding.imgPicEdit.source.setImageBitmap(bitmap)
                    } catch (e: IOException) {
                        e("Errorr", e.message)
                    }
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

        color1.setOnClickListener { mEditor?.brushColor  = resources.getColor(R.color.blue_color_picker)
            alertDialog?.dismiss()}
        color2.setOnClickListener { mEditor?.brushColor  = resources.getColor(R.color.brown_color_picker)
            alertDialog?.dismiss()}
        color3.setOnClickListener { mEditor?.brushColor  = resources.getColor(R.color.green_color_picker)
            alertDialog?.dismiss()}
        color4.setOnClickListener { mEditor?.brushColor  = resources.getColor(R.color.orange_color_picker)
            alertDialog?.dismiss()}
        color5.setOnClickListener { mEditor?.brushColor  = resources.getColor(R.color.red_color_picker)
            alertDialog?.dismiss()}
        color6.setOnClickListener { mEditor?.brushColor  = resources.getColor(R.color.black)
            alertDialog?.dismiss()}
        color7.setOnClickListener { mEditor?.brushColor  = resources.getColor(R.color.red_orange_color_picker)
            alertDialog?.dismiss()}
        color8.setOnClickListener { mEditor?.brushColor  = resources.getColor(R.color.sky_blue_color_picker)
            alertDialog?.dismiss()}
        color9.setOnClickListener { mEditor?.brushColor  = resources.getColor(R.color.violet_color_picker)
            alertDialog?.dismiss()}
        color10.setOnClickListener { mEditor?.brushColor  = resources.getColor(R.color.white)
            alertDialog?.dismiss()}
        color11.setOnClickListener { mEditor?.brushColor  = resources.getColor(R.color.yellow_color_picker)
            alertDialog?.dismiss()}
        color12.setOnClickListener { mEditor?.brushColor  = resources.getColor(R.color.yellow_green_color_picker)
            alertDialog?.dismiss()}
    }

    override fun onEmojiClick(emojiUnicode: String?) {
        mEditor?.addEmoji(emojiUnicode)
    }
}
