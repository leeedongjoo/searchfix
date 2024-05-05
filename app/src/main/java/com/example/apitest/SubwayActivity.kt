package com.example.apitest

import android.graphics.Matrix
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.GestureDetector
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.apitest.databinding.ActivitySubwayBinding
import kotlin.math.max
import kotlin.math.min

class SubwayActivity : AppCompatActivity() {
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var gestureDetector: GestureDetector
    private lateinit var imageView: ImageView
    private val matrix = Matrix()  // 이미지 뷰에 적용할 Matrix 인스턴스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySubwayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = binding.imgSubway
        imageView.scaleType = ImageView.ScaleType.MATRIX // 이미지 뷰의 ScaleType을 Matrix로 설정하여 변형 가능하게 함

        setupGestureDetectors() // 제스처 디텍터 초기화 및 설정
    }

    private fun setupGestureDetectors() {
        // 확대 및 축소를 처리하는 ScaleGestureDetector 초기화
        scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val scaleFactor = detector.scaleFactor
                // 터치 포인트를 중심으로 이미지를 확대/축소
                matrix.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
                imageView.imageMatrix = matrix  // 이미지 뷰에 Matrix 적용
                return true
            }
        })

        // 드래그(이동)를 처리하는 GestureDetector 초기화
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                // 이미지를 드래그한 만큼 이동
                matrix.postTranslate(-distanceX, -distanceY)
                imageView.imageMatrix = matrix  // 변경된 Matrix 적용
                return true
            }
        })

        // 이미지 뷰에 대한 터치 리스너 설정
        imageView.setOnTouchListener { v, event ->
            scaleGestureDetector.onTouchEvent(event)  // 확대/축소 이벤트 처리
            gestureDetector.onTouchEvent(event)  // 드래그 이벤트 처리
            true  // 이벤트가 처리되었음을 시스템에 알림
        }
    }
}