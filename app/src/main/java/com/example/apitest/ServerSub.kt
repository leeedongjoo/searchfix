package com.example.apitest

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.apitest.databinding.ActivityServerSubBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ServerSub : AppCompatActivity() {

    private lateinit var binding: ActivityServerSubBinding // 뷰 바인딩 초기화

    // Firebase Realtime Database 참조
    private val database = FirebaseDatabase.getInstance("https://subway-e1881-default-rtdb.firebaseio.com/").reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ActivityServerSubBinding을 사용한 뷰 바인딩 설정
        binding = ActivityServerSubBinding.inflate(layoutInflater)
        setContentView(binding.root) // setContentView는 binding.root로 호출

        // 검색 버튼 클릭 시 동작 설정
        binding.btnSearch.setOnClickListener {
            val departureStation = binding.etSearch.text.toString().trim() // 사용자가 입력한 역이름을 가져옴

            if (departureStation.isEmpty()) {
                Toast.makeText(this, "역 이름을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 고정된 시간대 12:00의 데이터를 가져오기
            database.child("subwayData").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var found = false
                    for (dataSnapshot in snapshot.children) {
                        val row = dataSnapshot.value as Map<String, Any>
                        if (row["출발역"] == departureStation) {
                            val serialNumber = row["연번"] as Long
                            val dayDivision = row["요일구분"] as String
                            val lineName = row["호선"] as Long
                            val stationCode = row["역번호"] as Long
                            val direction = row["상하구분"] as String

                            // 고정된 시간대 12:00의 혼잡도를 가져오기
                            val timeSlot = "12:00"
                            if (row.containsKey(timeSlot)) {
                                val congestion = row[timeSlot] as Double

                                // 뷰 바인딩을 사용하여 UI에 데이터 표시
                                binding.showresultT.text = "$serialNumber"
                                binding.showresultT2.text = "$dayDivision"
                                binding.showresultT3.text = "$lineName 호선"
                                binding.showresultT4.text = "역번호: $stationCode"
                                binding.showresultT5.text = "출발역: $departureStation"
                                binding.showresultT6.text = "상하구분: $direction"
                                binding.showresultT7.text = "12:00 시간대 밀집도: $congestion%"

                                // 로그에 출력
                                Log.d("SearchResult", "연번: $serialNumber, 요일구분: $dayDivision, 호선: $lineName, 역번호: $stationCode, 출발역: $departureStation, 상하구분: $direction, 밀집도: $congestion%")

                                Toast.makeText(this@ServerSub, "검색 성공!", Toast.LENGTH_SHORT).show()
                                found = true
                                break
                            }
                        }
                    }

                    if (!found) {
                        // 검색 결과가 없을 때 로그 출력
                        Log.d("SearchResult", "검색 결과가 없습니다: $departureStation")
                        Toast.makeText(this@ServerSub, "해당 시간에 검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("DatabaseError", "Database error occurred", error.toException())
                    Toast.makeText(this@ServerSub, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
