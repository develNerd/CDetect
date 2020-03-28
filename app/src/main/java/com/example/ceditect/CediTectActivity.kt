package com.example.ceditect

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ceditect.databinding.ActivityCediTectBinding
import de.crysxd.cameraXTracker.CameraFragment
import de.crysxd.cameraXTracker.ar.BoundingBoxArOverlay
import de.crysxd.cameraXTracker.ar.PathInterpolator
import de.crysxd.cameraXTracker.ar.PositionTranslator
import kotlinx.android.synthetic.main.activity_cedi_tect.*
import timber.log.Timber


class CediTectActivity : AppCompatActivity() , DetectListener {

    private lateinit var imageAnalyzer: ClassifyCediImageAnalyzer

    private val camera
        get() = supportFragmentManager.findFragmentById(R.id.cameraFragment) as CameraFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivityCediTectBinding = DataBindingUtil.setContentView(this,R.layout.activity_cedi_tect)
        imageAnalyzer = ViewModelProviders.of(this).get(ClassifyCediImageAnalyzer::class.java)
        binding.viewmodel = imageAnalyzer
        // Setup logging

        imageAnalyzer.detectListener = this

        imageAnalyzer.downloadCediModel()
        imageAnalyzer.isModelDownloaded()


        if (Timber.treeCount() == 0) {
            Timber.plant(Timber.DebugTree())
        }

        val boundingBoxArOverlay = BoundingBoxArOverlay(this, BuildConfig.DEBUG)

        camera.imageAnalyzer = imageAnalyzer
        camera.arOverlayView.observe(camera, Observer {
            it.doOnLayout { view ->
                imageAnalyzer.arObjectTracker
                    .pipe(PositionTranslator(view.width, view.height))
                    .pipe(PathInterpolator())
                    .addTrackingListener(boundingBoxArOverlay)
            }

            it.add(boundingBoxArOverlay)
        })
    }

    override fun onStarted() {
        TODO("Not yet implemented")
    }

    override fun onSuccess(labelValue: String) {
       labeled.text = labelValue


    }

    override fun onFailure(error: String) {
        Toast.makeText(
                this,
                error,
                Toast.LENGTH_SHORT
            )
            .show()


    }

    override fun onModelDownloadComplete(successMessage: String) {
        Toast.makeText(
                this,
                successMessage,
                Toast.LENGTH_SHORT
            )
            .show()
    }
}
