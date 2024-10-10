package com.example.apitest

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.CheckBox
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.apitest.databinding.ActivitySubwayBinding

class SubwayActivity : AppCompatActivity() {
    private lateinit var imageViewOverlays: Array<ImageView>
    private lateinit var checkBoxes: Array<CheckBox>
    private lateinit var checkBox0: CheckBox

    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subway)
        enableEdgeToEdge()
        val binding = ActivitySubwayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ImageView 인스턴스 초기화
        imageViewOverlays = arrayOf(
            findViewById(R.id.img_subwayone),
            findViewById(R.id.img_subwaytwo),
            findViewById(R.id.img_subwaythree),
            findViewById(R.id.img_subwayfour),
            findViewById(R.id.img_subwayfive),
            findViewById(R.id.img_subwaysix),
            findViewById(R.id.img_subwayseven),
            findViewById(R.id.img_subwayeight),
            findViewById(R.id.img_subwaynine)
        )

        // CheckBox 인스턴스 초기화
        checkBoxes = arrayOf(
            findViewById(R.id.checkBox1),
            findViewById(R.id.checkBox2),
            findViewById(R.id.checkBox3),
            findViewById(R.id.checkBox4),
            findViewById(R.id.checkBox5),
            findViewById(R.id.checkBox6),
            findViewById(R.id.checkBox7),
            findViewById(R.id.checkBox8),
            findViewById(R.id.checkBox9)
        )
        checkBox0 = findViewById(R.id.checkBox0)

        // 메인 CheckBox 리스너 설정
        checkBox0.setOnCheckedChangeListener { _, isChecked ->
            setAllCheckBoxesChecked(isChecked)
        }

        // 개별 CheckBox 리스너 설정
        for ((index, checkBox) in checkBoxes.withIndex()) {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                updateImageVisibility(isChecked, imageViewOverlays[index])
                updateMainCheckBoxState()
            }
        }

        // 메뉴 버튼 리스너 설정
        binding.menu1.setOnClickListener {
            val intent = Intent(this, SubwayAPI::class.java)
            startActivity(intent)
        }
        binding.menu2.setOnClickListener {
            val intenttt = Intent(this, TimeTest::class.java)
            startActivity(intenttt)
        }
    }

    private fun setAllCheckBoxesChecked(isChecked: Boolean) {
        for ((index, checkBox) in checkBoxes.withIndex()) {
            checkBox.setOnCheckedChangeListener(null)  // 리스너 제거
            checkBox.isChecked = isChecked
            updateImageVisibility(isChecked, imageViewOverlays[index])
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                updateImageVisibility(isChecked, imageViewOverlays[index])
                updateMainCheckBoxState()
            }
        }
    }

    private fun updateMainCheckBoxState() {
        checkBox0.setOnCheckedChangeListener(null)  // 리스너 제거
        checkBox0.isChecked = checkBoxes.all { it.isChecked }
        checkBox0.setOnCheckedChangeListener { _, isChecked ->
            setAllCheckBoxesChecked(isChecked)
        }
    }

    private fun updateImageVisibility(isChecked: Boolean, imageView: ImageView) {
        imageView.visibility = if (isChecked) ImageView.VISIBLE else ImageView.GONE
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
