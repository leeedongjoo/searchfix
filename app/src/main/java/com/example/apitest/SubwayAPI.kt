package com.example.apitest

import android.content.Intent
import android.os.Bundle
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
import java.util.*

class SubwayAPI : AppCompatActivity() {
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
            val searchText = binding.etSearch.text.toString()
            try {
                if (searchText.isNotEmpty()) {
                    searchDepartureStation(searchText) // 출발역 검색 메서드 호출

                    binding.btnSearch.setOnClickListener {
                        var intent = Intent(this, SubwayInfo::class.java)
                        startActivity(intent)
                    }
                } else {
                    Log.d("Search", "Please enter a departure station to search.") // 검색어가 비어있을 때 로그 출력
                    Toast.makeText(applicationContext, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (e: IOException) {
                Log.e("MainActivity", "Failed to load data: ", e) // 데이터 로드 실패 시 로그 출력
            } catch (e: CsvException) {
                Log.e("MainActivity", "CSV parsing error: ", e) // CSV 파싱 오류 시 로그 출력
            }

        }

    }

    @Throws(IOException::class, CsvException::class)
    // CSV 파일에서 가져온 내용을 위의 코드에 들어가 입력한 값이 파일내에 있는지 확인해서 출력해줌
    private fun searchDepartureStation(departureStation: String) {
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val assetManager = this.assets // AssetManager 객체 가져오기
        val inputStream = assetManager.open("subway.csv") // assets 폴더에 있는 CSV 파일 열기
        val csvReader = CSVReader(InputStreamReader(inputStream, "EUC-KR")) // CSVReader 초기화
        val allContent = csvReader.readAll() // CSV 파일 전체 내용 읽어오기


        var found = false // 결과를 찾았는지 여부를 나타내는 플래그 변수
        for (row in allContent.drop(1)) { // CSV 파일 내 각 행에 대해 반복 (첫 번째 행은 헤더이므로 건너뜀)
            if (row[4].equals(departureStation, ignoreCase = true)) {
                // 시간대가 현재 시간대에 해당하고 출발역이 일치하는 경우
                val serialNumber = row[0] // 연번
                val dayDivision = row[1] // 요일구분
                val lineName = row[2] // 호선
                val stationCode = row[3] // 역번호
                val direction = row[5] // 상하구분
                Log.d("SearchResult", "연번: $serialNumber, 요일구분: $dayDivision, 호선: $lineName, 역번호: $stationCode, 출발역: $departureStation, 상하구분: $direction, 시간: ${row[6]}, ${row[7]}, ${row[8]}")
                binding.showresultTX.text = "$serialNumber"
                binding.showresultTX2.text = "$dayDivision"
                binding.showresultTX3.text = "$lineName 호선"
                binding.showresultTX4.text = "역번호: $stationCode"
                binding.showresultTX5.text = "출발역: $departureStation"
                binding.showresultTX6.text = "상하구분: $direction"
                binding.showresultTX7.text = "시간당 밀집도: ${row[6]}%"
                Toast.makeText(applicationContext, "검색성공(확인용)", Toast.LENGTH_SHORT).show();

                found = true // 결과를 찾았으므로 플래그 변수 설정

            }
        }

        if (!found) {
            Log.d("Search", "No results found for: $departureStation in the current time block.") // 검색 결과가 없을 때 로그 출력
            Toast.makeText(applicationContext, "정확한 역이름을 검색해주세요.", Toast.LENGTH_SHORT).show()

        }

    }

}
