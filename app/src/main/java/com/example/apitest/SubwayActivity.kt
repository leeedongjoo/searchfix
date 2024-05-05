package com.example.apitest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.apitest.databinding.ActivitySubwayBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class SubwayActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubwayBinding
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f
    private lateinit var imageView: ImageView
    // BottomSheet layout 변수
    // BottomSheet layout 변수
    private val bottomSheetLayout by lazy { findViewById<LinearLayout>(R.id.bottom_sheet_layout) }
    private val bottomSheetExpandPersistentButton by lazy { findViewById<Button>(R.id.page_1) }
    private val bottomSheetExamplePersistentButton by lazy { findViewById<Button>(R.id.page_2) }
    private val bottomSheetShowModalButton by lazy { findViewById<Button>(R.id.page_3) }
    private val bottomSheetHidePersistentButton by lazy { findViewById<Button>(R.id.page_4) }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubwayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageView = binding.imgSubway
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        binding.menu1.setOnClickListener {
            val intent = Intent(this, SubwayAPI::class.java)
            startActivity(intent)
        }
        binding.menu2.setOnClickListener {
            val intentt = Intent(this, TimeTest::class.java)
            startActivity(intentt)
        }

        initializePersistentBottomSheet()
        persistentBottomSheetEvent()
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

    override fun onResume() {
        super.onResume()
        binding.menu3.setOnClickListener {
            // BottomSheet의 peek_height만큼 보여주기
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    // Persistent BottomSheet 초기화
    private fun initializePersistentBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // BottomSheetBehavior state에 따른 이벤트
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        Log.d("MainActivity", "state: hidden")
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        Log.d("MainActivity", "state: expanded")
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        Log.d("MainActivity", "state: collapsed")
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        Log.d("MainActivity", "state: dragging")
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        Log.d("MainActivity", "state: settling")
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        Log.d("MainActivity", "state: half expanded")
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    // PersistentBottomSheet 내부 버튼 click event
    private fun persistentBottomSheetEvent() {
        bottomSheetExpandPersistentButton.setOnClickListener {
            // BottomSheet의 최대 높이만큼 보여주기
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        bottomSheetHidePersistentButton.setOnClickListener {
            // BottomSheet 숨김
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        // bottomSheetShowModalButton.setOnClickListener {
        // 추후 modal bottomSheet 띄울 버튼
        // }
    }
}
