package com.example.apitest

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apitest.databinding.ActivityMainBinding
import com.example.apitest.databinding.ActivitySubwayBinding
import com.opencsv.CSVReader
import com.opencsv.exceptions.CsvException
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class SubwayAPI() : AppCompatActivity() {
    private val timeFormat = SimpleDateFormat("EE, HH:mm", Locale.KOREA)  // "시:분" 형식으로 시간 지정
    private val handler = Handler(Looper.getMainLooper())



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val searchField = findViewById<EditText>(R.id.etSearch)
        val searchButton = findViewById<Button>(R.id.btnSearch)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.goToactsub.setOnClickListener {
//            var intent = Intent(this, SubwayActivity::class.java)
//            startActivity(intent)
//        }


        binding.btnSearch.setOnClickListener {
            val calendar = Calendar.getInstance()
            val formattedTime = timeFormat.format(calendar.time)
            val parts = formattedTime.split(", ")  // "EE, HH:mm" 포맷에 맞춰 분리

            val day = parts[0]  // 요일 (EE)
            val hour = parts[1].split(":")[0]  // 시간 (HH)
            val minute = parts[1].split(":")[1]  // 분 (mm)

            val searchText = binding.etSearch.text.toString()
            try {
                if (searchText.isNotEmpty()) {
                    searchDepartureStation(searchText, day) // 출발역 및 요일 검색 메서드 호출
                } else {
                    Log.d("Search", "Please enter a departure station to search.")
                    Toast.makeText(applicationContext, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Log.e("MainActivity", "Failed to load data: ", e)
            } catch (e: CsvException) {
                Log.e("MainActivity", "CSV parsing error: ", e)
            }
        }
    }


    @Throws(IOException::class, CsvException::class)
    private fun searchDepartureStation(departureStation: String, day: String) {
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val assetManager = this.assets
        val inputStream = assetManager.open("subway.csv")
        val csvReader = CSVReader(InputStreamReader(inputStream, "EUC-KR"))
        val allContent = csvReader.readAll()

        var found = false
        val dayCondition = when (day) {
            "토" -> "토요일"
            "일" -> "일요일"
            else -> "평일"
        }

        for (row in allContent.drop(1)) {
            if (row[4].equals(departureStation, ignoreCase = true) && row[1].equals(
                    dayCondition,
                    ignoreCase = true
                )
            ) {
                // 검색된 데이터 변수에 저장
                val serialNumber = row[0]  // 연번
                val dayDivision = row[1]  // 요일구분
                val lineName = row[2]  // 호선
                val stationCode = row[3]  // 역번호
                val direction = row[5]  // 상하구분
                val firstDensity = row[6]  // 첫 번째 시간 밀집도
                val secondDensity = row[7]  // 두 번째 시간 밀집도
                val thirdDensity = row[8]  // 세 번째 시간 밀집도

                // 로그로 결과 출력
                Log.d(
                    "SearchResult",
                    "연번: $serialNumber, 요일구분: $dayDivision, 호선: $lineName, 역번호: $stationCode, 출발역: $departureStation, 상하구분: $direction, 시간: $firstDensity, $secondDensity, $thirdDensity"
                )

                // UI에 검색 결과 표시
                binding.showresultTX.text = "$serialNumber"
                binding.showresultTX2.text = "$dayDivision"
                binding.showresultTX3.text = "$lineName 호선"
                binding.showresultTX4.text = "역번호: $stationCode"
                binding.showresultTX5.text = "출발역: $departureStation"
                binding.showresultTX6.text = "상하구분: $direction"
                binding.showresultTX7.text = "시간당 밀집도: $firstDensity%"

                Toast.makeText(applicationContext, "검색성공(확인용)", Toast.LENGTH_SHORT).show()

                found = true
            }

            if (!found) {
                Log.d("Search", "No results found for: $departureStation on $dayCondition.")
                Toast.makeText(applicationContext, "정확한 역이름을 검색해주세요.", Toast.LENGTH_SHORT).show()
            }

            csvReader.close()
        }
    }
}
