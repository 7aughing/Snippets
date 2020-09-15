package com.example.recordlearn

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.*
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_random_light.*
import kotlinx.android.synthetic.main.activity_random_light.viewFinder
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.Timer
import kotlin.concurrent.schedule


class RandomLightActivity : AppCompatActivity()  {
    lateinit var btn_start: Button
    lateinit var btn_stop: Button
    lateinit var btn_play_audio: Button
    lateinit var canvas: Canvas

    lateinit var file:File

    var preview: Preview?=null
    var camera: Camera?=null
    var videoCapture: VideoCapture?=null
    var imageCapture: ImageCapture?=null
    var cameraSelector: CameraSelector=CameraSelector.DEFAULT_FRONT_CAMERA

    private var mPlayer: MediaPlayer?=null
    private var mRecorder: MediaRecorder?= null

    private var soundFile: File?=null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_light)

        if (allPermissionsGranted()){
            startCamera()
        }else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        btn_start_video_light.setOnClickListener(object :MyOnClickListener(){})
        btn_back_from_light.setOnClickListener(object :MyOnClickListener(){})
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

        if(soundFile!=null && soundFile!!.exists()){
            mRecorder?.stop()
            mRecorder?.release()
            mRecorder = null
        }
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
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    companion object {
        private const val TAG = "LIGHT"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO
        )
        var isRecording:Boolean = false
        private val REQUEST_CODE_TO_PLAY = 9999
    }


    open inner class MyOnClickListener:View.OnClickListener{
        @SuppressLint("RestrictedApi")
        override fun onClick(btn: View) {
            when(btn.id){
                R.id.btn_start_video_light -> {

                    if(isRecording == false){
                        viewFinder.setVisibility(View.GONE);
                        linear_layout2.setVisibility(View.GONE);
                        btn_start_video_light.setVisibility(View.GONE);
                        myImageView2.setVisibility(View.GONE);
                        background.setVisibility(View.VISIBLE);

                        val colors = arrayOf(
                            intArrayOf(255, 0, 0),
                            intArrayOf(0, 255, 0),
                            intArrayOf(0, 0, 255),
                            intArrayOf(255, 255, 0)
                        )
                        val sequences = intArrayOf(0, 1, 2, 3)
                        val list: MutableList<Int> = ArrayList<Int>()

                        for (i in sequences.indices) {
                            list.add(sequences[i])
                        }

                        // 随机打乱顺序
                        list.shuffle()
                        val ite: Iterator<*> = list.iterator()

                        var r = 0
                        var g = 0
                        var b = 0
                        val index = ite.next() as Int
                        Log.i("zhitao: color: ", index.toString())
                        r = colors[index][0]
                        g = colors[index][1]
                        b = colors[index][2]
                        background.setBackgroundColor(Color.rgb(r,g,b))

                        val handler = Handler()
                        val runnable = Runnable {
                            // 500ms后执行该方法
                            // handler自带方法实现定时器
                            try {
                                background.setVisibility(View.GONE);
                                background.setVisibility(View.VISIBLE);
                                var index1 = ite.next() as Int
                                Log.i("zhitao: color: ", index1.toString())
                                r = colors[index1][0]
                                g = colors[index1][1]
                                b = colors[index1][2]
                                background.setBackgroundColor(Color.rgb(r,g,b))
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        handler.postDelayed(runnable,500);

                        val runnable2 = Runnable {
                            // 500ms后执行该方法
                            // handler自带方法实现定时器
                            try {
                                background.setVisibility(View.GONE);
                                background.setVisibility(View.VISIBLE);
                                var index2 = ite.next() as Int
                                Log.i("zhitao: color: ", index2.toString())
                                r = colors[index2][0]
                                g = colors[index2][1]
                                b = colors[index2][2]
                                background.setBackgroundColor(Color.rgb(r,g,b))
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        handler.postDelayed(runnable2,1000)

                        val runnable3 = Runnable {
                            // 500ms后执行该方法
                            // handler自带方法实现定时器
                            try {
                                background.setVisibility(View.GONE);
                                background.setVisibility(View.VISIBLE);
                                var index3 = ite.next() as Int
                                Log.i("zhitao: color: ", index3.toString())
                                r = colors[index3][0]
                                g = colors[index3][1]
                                b = colors[index3][2]
                                background.setBackgroundColor(Color.rgb(r,g,b))
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        handler.postDelayed(runnable3,1500)

                        val runnable_end = Runnable {
                            // 500ms后执行该方法
                            // handler自带方法实现定时器
                            try {
                                viewFinder.setVisibility(View.VISIBLE);
                                linear_layout2.setVisibility(View.VISIBLE);
                                linear_layout2.setVisibility(View.VISIBLE);
                                btn_start_video_light.setVisibility(View.VISIBLE);
                                myImageView2.setVisibility(View.VISIBLE);
                                background.setVisibility(View.GONE);

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        handler.postDelayed(runnable_end,2000);

                        val color_sequence_name=list.joinToString("")
                        val date = Date()
                        System.out.println(date)
                        val sdf = SimpleDateFormat("yyyyMMdd-HHmmss")
                        val dateNowStr: String = sdf.format(date)
//                        val file = File(getFilesDir().getPath()+'/'+dateNowStr+'-'+color_sequence_name+".mp4")
                        file = File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path+"/Camera/"+dateNowStr+'-'+color_sequence_name+".mp4")


                        Looper.myLooper()?.let {
                            Handler(it).postDelayed({
                                try {
                                        videoCapture?.stopRecording()
                                        isRecording = !isRecording

                                        var intent = Intent(this@RandomLightActivity, PlayVideoActivityLight::class.java)
                                        intent.putExtra(
                                            "video_path",
                                            file.absolutePath
                                        )
                                        startActivityForResult(intent,
                                            RandomLightActivity.REQUEST_CODE_TO_PLAY
                                        )

                                        btn_start_video_light.setText("Start Video")
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                }
                            }, 2200)
                        }


                        btn_start_video_light.setText("Stop Video")
                        isRecording = !isRecording
                        videoCapture?.startRecording(
                            file,
                            // ContextCompat ：Helper for accessing features in Context
                            ContextCompat.getMainExecutor(this@RandomLightActivity),

                            object: VideoCapture.OnVideoSavedCallback{
                                override fun onVideoSaved(file: File) {
//                                    Toast.makeText(this@RandomLightActivity, file.absolutePath, Toast.LENGTH_LONG).show()
                                }

                                override fun onError(
                                    videoCaptureError: Int,
                                    message: String,
                                    cause: Throwable?
                                ) {
                                    Log.e("zhitao","onError: $message")
                                }
                            }
                        )

                    }
                }


                R.id.btn_back_from_light ->{
                    val intent = Intent(this@RandomLightActivity, MainActivity::class.java)
                    setResult(RESULT_OK, intent)
                    startActivity(intent)
                }
            }
        }
    }

    private fun deleteFile() {
        if (file != null && file.exists()) {
            Log.i(RandomLightActivity.TAG, "delete unused file:" + file.getName() + ",result=" + file.delete())
            if (file.exists()) {
                Log.i(RandomLightActivity.TAG, "delete unused file:" + file.getName() + ",result=" + deleteFile(file.getName()))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RandomLightActivity.REQUEST_CODE_TO_PLAY){
            if (resultCode == RESULT_CANCELED) {
                deleteFile()
            }else if (resultCode == RESULT_OK){
                Log.i(RandomLightActivity.TAG, "saved successfully!")
            }
        }
    }
}
