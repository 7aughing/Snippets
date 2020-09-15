package com.example.recordlearn
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.*
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.activity_lipreading.*
import java.io.File

class LipreadingActivity : AppCompatActivity() {

    lateinit var randomString: String
    var cnt:Int = 0
    lateinit var file:File

    var preview: Preview?=null
    var camera:Camera?=null
    var videoCapture:VideoCapture?=null
    var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA


    private lateinit var mHandler: Handler
    private lateinit var mRunnable:Runnable
    private var idx:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lipreading)

        if (allPermissionsGranted()){
            startCamera()
        }else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        if(isExternalStorageReadable()==false || isExternalStorageWritable()==false){
            Toast.makeText(this, "No permission!",Toast.LENGTH_SHORT).show()
        }

        btn_start_video_lipreading.setOnClickListener(object :MyOnClickListener(){})
        btn_back_from_lipreading.setOnClickListener(object :MyOnClickListener(){})

        randomString = getRandomStr()
        textView.setText(randomString)

        mRunnable= Runnable {
            var ss =  SpannableString(randomString)
            ss.setSpan(ForegroundColorSpan(Color.parseColor("#00B2EE")),
                idx, idx+1,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            textView.setText(ss)
            idx+=1
            mHandler.postDelayed(mRunnable, 700)


            if(idx == randomString.length){
                idx = 0
                mHandler.removeCallbacks(mRunnable)
            }
        }

        mHandler = Handler()
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


    private fun getRandomStr(): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var res = ""
        for (i in 0..3){
            var cur_char = charset[(0..25).random()]
            while (i>=1 && cur_char==res[i-1]){
                cur_char = charset[(0..25).random()]
            }
            res += cur_char
        }
        return res.trim()
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
                finish()
            }
        }
    }


    companion object {
        private const val TAG = "LIPREADING"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO
        )
        private val REQUEST_CODE_TO_PLAY = 999
        private var isRecording:Boolean = false
    }


    open inner class MyOnClickListener:View.OnClickListener{
        @SuppressLint("RestrictedApi")
        override fun onClick(btn: View) {
            when(btn.id){
                R.id.btn_start_video_lipreading -> {
//                    // write to memory
//                    val dir = this@LipreadingActivity.filesDir.absolutePath + "/Lipreading/" + Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)+"/"

                    // write to sdcard
                    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path + "/Camera/Lipreading/" + Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)+"/"
                    if(!File(dir).exists()){
                        File(dir).mkdirs()
                    }
//                    Toast.makeText(this@LipreadingActivity, dir,Toast.LENGTH_LONG).show()

                    val filename = dir +randomString.replace(" ","")+".mp4"
                    file = File(filename)

                    if(isRecording == false){
                        btn_start_video_lipreading.setText("Stop Video")
                        isRecording=!isRecording

                        mHandler.postDelayed(mRunnable, 500)
                        Looper.myLooper()?.let {
                            Handler(it).postDelayed({
                                textView.setText(randomString)
                            }, 3500)
                        }

                        videoCapture?.startRecording(
                            file,
                            ContextCompat.getMainExecutor(this@LipreadingActivity),

                            object: VideoCapture.OnVideoSavedCallback{
                                override fun onVideoSaved(file: File) {
//                                    Toast.makeText(this@MainActivity, file.absolutePath, Toast.LENGTH_SHORT).show()
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

                        Log.i("MAIN_Activity", file.absolutePath)
                        var intent = Intent(this@LipreadingActivity, PlayVideoActivity::class.java)
                        intent.putExtra(
                            "video_path",
                            file.absolutePath
                        )
                        startActivityForResult(intent, REQUEST_CODE_TO_PLAY)

                        btn_start_video_lipreading.setText("Start Video")
                        randomString=getRandomStr()
                        textView.setText(randomString)
                    }
                }

                R.id.btn_back_from_lipreading ->{
                    val intent = Intent(this@LipreadingActivity, MainActivity::class.java)
                    setResult(RESULT_OK, intent)
                    startActivity(intent)
                }
            }
        }
    }


    private fun deleteFile() {
        if (file != null && file.exists()) {
            Log.i(TAG, "delete unused file:" + file.getName() + ",result=" + file.delete())
            if (file.exists()) {
                Log.i(TAG, "delete unused file:" + file.getName() + ",result=" + deleteFile(file.getName()))
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_TO_PLAY){
            if (resultCode == RESULT_CANCELED) {
                var hint_string = "FINISHED: " + cnt.toString()
                var sp = SpannableString(hint_string)
                sp.setSpan(
                    ForegroundColorSpan(Color.parseColor("#FF0000")),
                    "FINISHED: ".length-1, hint_string.length,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
                savedCount.setText(sp)
                deleteFile()

            }else if (resultCode == RESULT_OK){

                cnt += 1
                var hint_string = "FINISHED: " + cnt.toString()
                var sp = SpannableString(hint_string)
                sp.setSpan(
                    ForegroundColorSpan(Color.parseColor("#FF0000")),
                    "FINISHED: ".length-1, hint_string.length,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
                savedCount.setText(sp)

                if (cnt == 30){
                    Toast.makeText(this,"DONE",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }



    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /* Checks if external storage is available to at least read */
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }



}