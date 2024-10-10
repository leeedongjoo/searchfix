package com.example.apitest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.apitest.databinding.ActivityMainBinding
import com.example.apitest.databinding.ActivitySubwayBinding
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory
import android.widget.Toast
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

import java.time.LocalTime

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubwayBinding // 뷰 바인딩 초기화

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySubwayBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }}