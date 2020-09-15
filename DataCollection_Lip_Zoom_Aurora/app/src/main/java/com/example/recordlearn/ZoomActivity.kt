package com.example.recordlearn

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.VideoCapture.OnVideoSavedCallback
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.activity_lipreading.*
import kotlinx.android.synthetic.main.activity_lipreading.viewFinder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_zoom.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import kotlin.random.Random

class ZoomActivity : AppCompatActivity() {
    lateinit var file:File

    var preview: Preview?=null
    var camera:Camera?=null
    var videoCapture:VideoCapture?=null
    var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom)

        if (allPermissionsGranted()){
            startCamera()
        }else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        btnStartVideo.setOnClickListener(object : MyOnClickListener() {})
        btn_back_from_zoom.setOnClickListener(object :MyOnClickListener(){})

    }


    @SuppressLint("RestrictedApi")
    private fun startCamera(){
        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()

            preview = Preview.Builder().build()

            videoCapture = VideoCapture.Builder().build()

            cameraProvider.unbindAll()

            camera  = cameraProvider.bindToLifecycle(this,
                cameraSelector,
                preview, videoCapture)

            preview?.setSurfaceProvider(viewFinder.createSurfaceProvider())

        }, ContextCompat.getMainExecutor(this))

    }


    override fun onDestroy() {
        super.onDestroy()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                startCamera()
            }else{
//                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    companion object {
        private const val TAG = "CameraXBasic"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO
        )

        var isRecording:Boolean = false
    }


    open inner class MyOnClickListener: View.OnClickListener{
        @SuppressLint("RestrictedApi")
        override fun onClick(btn: View) {
            when(btn.id){
                R.id.btnStartVideo -> {


                    val date = Date()
                    val sdf = SimpleDateFormat("yyyyMMdd-HHmmss")
                    val dateNowStr: String = sdf.format(date)
//                    val file = File(getFilesDir().getPath()+'/'+dateNowStr+".mp4")
                    val file = File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path+"/Camera/"+dateNowStr+".mp4")


                    if(isRecording == false){
                        btnStartVideo.setText("Stop Video")
                        isRecording=!isRecording

//                        Timer()

                        videoCapture?.startRecording(
                            file,
                            ContextCompat.getMainExecutor(this@ZoomActivity),

                            object: VideoCapture.OnVideoSavedCallback{
                                override fun onVideoSaved(file: File) {
//                                    Toast.makeText(this@ZoomActivity, file.absolutePath, Toast.LENGTH_SHORT).show()
                                }
                                override fun onError(
                                    videoCaptureError: Int,
                                    message: String,
                                    cause: Throwable?
                                ) {
                                    Log.e("","onError: $message")
                                }
                            }
                        )
                    }else{
                        videoCapture?.stopRecording()
                        isRecording=!isRecording

                        var intent = Intent(this@ZoomActivity, PlayVideoActivity::class.java)
                        intent.putExtra(
                            "video_path",
                            file.absolutePath
                        )
                        btnStartVideo.setText("Start Video")
                    }
                }

                R.id.btn_back_from_zoom ->{
                    val intent = Intent(this@ZoomActivity, MainActivity::class.java)
                    setResult(RESULT_OK, intent)
                    startActivity(intent)
                }
            }
        }
    }


}