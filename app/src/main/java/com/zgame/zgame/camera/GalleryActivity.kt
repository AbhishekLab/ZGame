package com.zgame.zgame.camera

import android.graphics.Matrix
import android.os.Bundle
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.lifecycle.LifecycleOwner
import com.zgame.zgame.R
import kotlinx.android.synthetic.main.activity_gallery.*
import java.util.concurrent.Executors

class GalleryActivity : AppCompatActivity(), LifecycleOwner {

    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var viewFinder: TextureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)


        viewFinder = findViewById(R.id.view_finder)

        viewFinder.post { startCamera() }

        /*viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }*/
    }



    private fun startCamera() {
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetResolution(Size(640, 480))
        }.build()

        val preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener(object : Preview.OnPreviewOutputUpdateListener{
            override fun onUpdated(output: Preview.PreviewOutput) {
                var parent : ViewGroup = view_finder.parent as ViewGroup

                parent.removeView(view_finder)
                parent.addView(view_finder)

                view_finder.surfaceTexture = output.surfaceTexture
                updateTransform()
            }

        })
    }

    private fun updateTransform() {
        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        // Correct preview output to account for display rotation
        val rotationDegrees = when(viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix)
    }
}