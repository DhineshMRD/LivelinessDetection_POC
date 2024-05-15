package com.example.samplelivelinesdetection

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.samplelivelinesdetection.databinding.ActivityMainBinding
import com.us47codex.liveness_detection.tasks.DetectionTask
import com.us47codex.liveness_detection.tasks.EyesBlinkDetectionTask
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var cameraController: LifecycleCameraController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startCamera()
            } else {
                Toast.makeText(this, "Permission deny", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.launch(Manifest.permission.CAMERA)

    }

    private fun startCamera() {
        cameraController = LifecycleCameraController(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(this),
            FaceAnalyzer(buildLivenessDetector())
        )
        cameraController.bindToLifecycle(this)
        binding.cameraPreview.controller = cameraController

       /* binding.cameraSwitch.setOnClickListener {
            cameraController.cameraSelector =
                if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
        }*/
    }

    private fun buildLivenessDetector(): LivenessDetector {
        return LivenessDetector(EyesBlinkDetectionTask(),this@MainActivity)
    }

/*    private fun buildLivenessDetector(): LivenessDetector {
        val listener = object : LivenessDetector.Listener {
            @SuppressLint("SetTextI18n")
            override fun onTaskStarted(task: DetectionTask) {

                Toast.makeText(
                    this@MainActivity,
                    "Task started",
                    Toast.LENGTH_LONG
                ).show()            }

            override fun onTaskCompleted( isLastTask: Boolean) {
                    if (isLastTask) {
                        finishForResult()
                    }
                }


            override fun onTaskFailed(task: DetectionTask, code: Int) {
                when (code) {
                    LivenessDetector.ERROR_MULTI_FACES -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Please make sure there is only one face on the screen.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    LivenessDetector.ERROR_NO_FACE -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Please make sure there is a face on the screen.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    LivenessDetector.ERROR_OUT_OF_DETECTION_RECT -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Please make sure there is a face in the Rectangle.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    else -> {
                        Toast.makeText(
                            this@MainActivity,
                            "${task.taskName()} Failed.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        return LivenessDetector(EyesBlinkDetectionTask(),this@MainActivity)
    }

    private fun finishForResult() {
     //   val result = ArrayList(imageFiles.takeLast(livenessDetector.getTaskSize()))
     //   setResult(RESULT_OK, Intent().putStringArrayListExtra(ResultContract.RESULT_KEY, result))
        finish()
    }*/
}