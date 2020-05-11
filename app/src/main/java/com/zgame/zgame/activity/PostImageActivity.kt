package com.zgame.zgame.activity

import android.view.View
import com.bumptech.glide.Glide
import com.zgame.zgame.R
import com.zgame.zgame.base.BaseActivity
import com.zgame.zgame.databinding.ActivityPostImageBinding
import com.zgame.zgame.editor.ToolType
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.ViewType

class PostImageActivity : BaseActivity<ActivityPostImageBinding>(),OnPhotoEditorListener {

    private var isVisibleBrush : Boolean = false
    private var mEditor : PhotoEditor ? = null

    private lateinit var mBinding: ActivityPostImageBinding

    override fun onPermissionsGranted(requestCode: Int) {
    }

    override fun contentView(): Int = R.layout.activity_post_image

    override fun initUI(binding: ActivityPostImageBinding) {
        mBinding = binding

        mEditor = PhotoEditor.Builder(this, mBinding.imgPicEdit)
            .setPinchTextScalable(true)
            .build()

        mEditor?.setOnPhotoEditorListener(this)

        Glide.with(this).load(R.drawable.gradient_2_image).apply {
            mBinding.imgPicEdit.scaleY = 2f
        }.into(mBinding.imgPicEdit.source)

        mBinding.txtAddGradient.setOnClickListener {
            if (mBinding.txtAddGradient.drawable.constantState == resources.getDrawable(R.drawable.ic_plus).constantState) {
                viewGradient()
            } else {
                hideGradient()
            }
        }

        mBinding.txtGradient1.setOnClickListener {
            setBackGround1()
        }
        mBinding.txtGradient2.setOnClickListener {
            setBackGround2()
        }
        mBinding.txtGradient3.setOnClickListener {
            setBackGround3()
        }
        mBinding.txtGradient4.setOnClickListener {
            setBackGround4()
        }
        mBinding.txtGradient5.setOnClickListener {
            setBackGround5()
        }
        mBinding.imgEmoji.setOnClickListener {
            mBinding.imgBrush.alpha = 0.6f
            mBinding.imgEmoji.alpha = 1f
            mBinding.imgEraser.visibility = View.GONE
        }
        mBinding.imgBrush.setOnClickListener {
            mBinding.imgBrush.alpha = 1f
            mBinding.imgEmoji.alpha = 0.6f
            mBinding.imgEraser.visibility = View.VISIBLE

            uiEditing(ToolType.BRUSH)
        }

        mBinding.imgEraser.setOnClickListener {
            mBinding.imgBrush.alpha = 0.6f
            mBinding.imgEmoji.alpha = 0.6f
            mBinding.imgEraser.alpha = 1f
            uiEditing(ToolType.ERASER)
        }

    }

    private fun viewGradient() {
        mBinding.txtAddGradient.setImageResource(R.drawable.ic_minus)
        mBinding.txtGradient1.visibility = View.VISIBLE
        mBinding.txtGradient2.visibility = View.VISIBLE
        mBinding.txtGradient3.visibility = View.VISIBLE
        mBinding.txtGradient4.visibility = View.VISIBLE
        mBinding.txtGradient5.visibility = View.VISIBLE
    }

    private fun hideGradient() {
        mBinding.txtAddGradient.setImageResource(R.drawable.ic_plus)
        mBinding.txtGradient1.visibility = View.GONE
        mBinding.txtGradient2.visibility = View.GONE
        mBinding.txtGradient3.visibility = View.GONE
        mBinding.txtGradient4.visibility = View.GONE
        mBinding.txtGradient5.visibility = View.GONE
    }

    private fun setBackGround1() {
        Glide.with(this).load(R.drawable.gradient_1_image).apply{
            mBinding.imgPicEdit.scaleX = 2f
            mBinding.imgPicEdit.scaleY = 2f
        }
            .into(mBinding.imgPicEdit.source)
    }

    private fun setBackGround2() {
        Glide.with(this).load(R.drawable.gradient_2_image).apply{
            mBinding.imgPicEdit.scaleX = 2f
            mBinding.imgPicEdit.scaleY = 2f
        }
            .into(mBinding.imgPicEdit.source)
    }

    private fun setBackGround3() {
        Glide.with(this).load(R.drawable.gradient_3_image).apply{
            mBinding.imgPicEdit.scaleX = 2f
            mBinding.imgPicEdit.scaleY = 2f
        }
            .into(mBinding.imgPicEdit.source)
    }

    private fun setBackGround4() {
        Glide.with(this).load(R.drawable.gradient_4_image).apply{
            mBinding.imgPicEdit.scaleX = 2f
            mBinding.imgPicEdit.scaleY = 2f
        }
            .into(mBinding.imgPicEdit.source)
    }

    private fun setBackGround5() {
        Glide.with(this).load(R.drawable.gradient_5_image).apply{
            mBinding.imgPicEdit.scaleX = 2f
            mBinding.imgPicEdit.scaleY = 2f
        }
            .into(mBinding.imgPicEdit.source)
    }

    private fun uiEditing(toolType: ToolType) {

        when(toolType){
            ToolType.BRUSH -> {
                mEditor?.setBrushDrawingMode(true)
            }
        }
    }

    override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartViewChangeListener(viewType: ViewType?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStopViewChangeListener(viewType: ViewType?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
