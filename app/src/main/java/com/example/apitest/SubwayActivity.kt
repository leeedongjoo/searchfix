package com.example.apitest

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.apitest.databinding.ActivitySubwayBinding
import kotlin.math.log

class SubwayActivity : AppCompatActivity() {
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f
    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var binding = ActivitySubwayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageView = findViewById(R.id.img_subway)

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        binding.youngsan.setOnClickListener {
            binding.testtx.text = "success"

        }
        binding.menu1.setOnClickListener {
            var intent = Intent(this, SubwayAPI::class.java)
            startActivity(intent)
        }
        binding.menu2.setOnClickListener {
            var intenttt = Intent(this, TimeTest::class.java)
            startActivity(intenttt)
        }


    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return event?.let {
            scaleGestureDetector?.onTouchEvent(it) ?: false
        } ?: false
    }
    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(1.0f, 9.0f)
            imageView.scaleX = scaleFactor
            imageView.scaleY = scaleFactor
            return true
        }
    }
}