package com.example.apitest

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apitest.databinding.ActivityMainBinding
import com.opencsv.CSVReader
import com.opencsv.exceptions.CsvException
import java.io.IOException
import java.io.InputStreamReader

class SubwayAPI : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var subwayInfoFragment: SubwayInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val etSearch = binding.etSearch
        val btnSearch = binding.btnSearch
        subwayInfoFragment = SubwayInfo()

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, subwayInfoFragment)
            .commit()

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                subwayInfoFragment.filterButtons(query)
            }
        })

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString().trim()
            subwayInfoFragment.filterButtons(query)

            val searchText = binding.etSearch.text.toString()
            try {
                if (searchText.isNotEmpty()) {
                    searchDepartureStation(searchText) // 출발역 검색 메서드 호출
                } else {
                    Toast.makeText(applicationContext, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Toast.makeText(applicationContext, "데이터 로드 실패", Toast.LENGTH_SHORT).show()
            } catch (e: CsvException) {
                Toast.makeText(applicationContext, "CSV 파싱 오류", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Throws(IOException::class, CsvException::class)
    private fun searchDepartureStation(departureStation: String) {
        val assetManager = this.assets
        val inputStream = assetManager.open("subway.csv")
        val csvReader = CSVReader(InputStreamReader(inputStream, "EUC-KR"))
        val allContent = csvReader.readAll()

        var found = false
        for (row in allContent.drop(1)) {
            if (row[4].equals(departureStation, ignoreCase = true)) {
                found = true
                break
            }
        }

        if (!found) {
            Toast.makeText(applicationContext, "정확한 역이름을 검색해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
