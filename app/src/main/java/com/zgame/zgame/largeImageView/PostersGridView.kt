package com.zgame.zgame.largeImageView

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout

class PostersGridView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var imageLoader: ((ImageView, String?) -> Unit)? = null
    var onPosterClick: ((Int, ImageView) -> Unit)? = null


}