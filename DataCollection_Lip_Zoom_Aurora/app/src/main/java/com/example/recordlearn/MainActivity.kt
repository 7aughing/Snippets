package com.example.recordlearn

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var btn_start: Button
    lateinit var btn_stop: Button
    lateinit var btn_play_audio:Button
    lateinit var canvas: Canvas
    lateinit var randomString: String
    var cnt:Int = 0
    lateinit var file:File

    val storage = Firebase.storage
    val storageRef = storage.reference

    lateinit var mHandler: Handler


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_light.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, RandomLightActivity::class.java)
            val requestCodeLight = REQUEST_CODE_LIGHT
            startActivityForResult(intent, requestCodeLight)
        })

        btn_zoom.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, ZoomActivity::class.java)
            val requestCodeZoom = REQUEST_CODE_ZOOM
            startActivityForResult(intent, requestCodeZoom)
        })

        btn_lipreading.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, LipreadingActivity::class.java)
            val requestCodeLip = REQUEST_CODE_LIPREADING
            startActivityForResult(intent, requestCodeLip)
        })

    }


    companion object{
        private const val TAG = "DataCollection"
        private val REQUEST_CODE_LIGHT = 111
        private val REQUEST_CODE_ZOOM = 222
        private val REQUEST_CODE_LIPREADING = 333
    }

}




