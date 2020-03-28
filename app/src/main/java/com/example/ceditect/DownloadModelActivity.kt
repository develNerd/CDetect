package com.example.ceditect

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager
import com.google.firebase.ml.common.modeldownload.FirebaseRemoteModel
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.automl.FirebaseAutoMLRemoteModel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.activity_download_model.*
import kotlinx.android.synthetic.main.activity_main.*


class DownloadModelActivity : AppCompatActivity() {

    private var modelDownloaded = false
    private lateinit var labeler: FirebaseVisionImageLabeler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_model)

        var visible: Boolean = false


        val transitionsContainer =
            findViewById(R.id.transition_group) as ViewGroup

        val ButtonContinue =
            transitionsContainer.findViewById<View>(R.id.Continue) as Button

        val pleasewait =
            transitionsContainer.findViewById<View>(R.id.pleaseWait) as RelativeLayout

        val ImageAlldone =
            transitionsContainer.findViewById<View>(R.id.imgAlldone) as ImageView


        val circularProgressBar = findViewById<CircularProgressBar>(R.id.circularProgressBar)
        circularProgressBar.apply {
            // Set Progress
            progress = 65f
            // or with animation
            setProgressWithAnimation(65f, 1000) // =1s

            // Set Progress Max
            progressMax = 200f

            // Set ProgressBar Color
            progressBarColor = Color.BLACK
            // or with gradient
            progressBarColorStart = Color.GRAY
            progressBarColorEnd = Color.RED
            progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.GRAY
            // or with gradient
            backgroundProgressBarColorStart = Color.WHITE
            backgroundProgressBarColorEnd = Color.RED
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 7f // in DP
            backgroundProgressBarWidth = 3f // in DP

            // Other
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        val remoteModel = FirebaseAutoMLRemoteModel.Builder("Cedi_Tect_20203793036").build()


        val conditions = FirebaseModelDownloadConditions.Builder()
            .build()
        FirebaseModelManager.getInstance().download(remoteModel, conditions)
            .addOnCompleteListener {
                Handler(Looper.getMainLooper()).postDelayed({

                    TransitionManager.beginDelayedTransition(transitionsContainer)
                    visible = !visible
                    ButtonContinue.visibility = View.VISIBLE
                    ImageAlldone.visibility = View.VISIBLE
                    txtAllDone.visibility = View.VISIBLE
                    circularProgressBar.visibility = View.GONE
                    pleasewait.visibility = View.GONE


                }, 150)
                Toast.makeText(
                    this,
                    "Model Downloaded.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(
                        this,
                        "Failure in downloading Auto ML model.",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }

        FirebaseModelManager.getInstance().isModelDownloaded(remoteModel)
            .addOnSuccessListener {
                modelDownloaded = true



                val labelerOptions = FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(remoteModel)
                    .setConfidenceThreshold(0.65f)
                    .build()
                val labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(labelerOptions)



            }
            .addOnFailureListener {
                Toast.makeText(
                        this,
                        "Failure in downloading Auto ML model.",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }


        /*  val remoteModel = FirebaseRemoteModel.Builder("Cedi_Tect_20203793036")
              .enableModelUpdates(true)
              .setInitialDownloadConditions(conditions)
              .setUpdatesDownloadConditions(conditions)
              .build()
          FirebaseModelManager.getInstance().registerRemoteModel(remoteModel)

          val labelerOptions = FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder()
              .setRemoteModelName("Cedi_Tect_20203793036")
              .setConfidenceThreshold(0.65f)
              .build()

          labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(labelerOptions)

          FirebaseModelManager.getInstance().downloadRemoteModelIfNeeded(remoteModel)
              .addOnSuccessListener {
                  modelDownloaded = true
                  Handler(Looper.getMainLooper()).postDelayed({

                      TransitionManager.beginDelayedTransition(transitionsContainer)
                      visible = !visible
                      ButtonContinue.visibility = View.VISIBLE
                      ImageAlldone.visibility = View.VISIBLE
                      txtAllDone.visibility = View.VISIBLE
                      circularProgressBar.visibility = View.GONE
                      pleasewait.visibility = View.GONE


                  }, 150)

              }
              .addOnFailureListener {
                  Toast.makeText(
                          this,
                          "Failure in downloading Auto ML model.",
                          Toast.LENGTH_SHORT
                      )
                      .show()
              }
  */

        ButtonContinue.setOnClickListener {
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
        }



    }


}
