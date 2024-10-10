package com.example.apitest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.apitest.R
import com.example.apitest.SubwayAPI

class SubwayInfo : Fragment() {

    private lateinit var buttons: List<Button>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.subway_info, container, false)

        buttons = listOf(
            view.findViewById(R.id.button),
            view.findViewById(R.id.button_1),
            view.findViewById(R.id.button_2),
            view.findViewById(R.id.button_3),
            view.findViewById(R.id.button_4),
            view.findViewById(R.id.button_5),
            view.findViewById(R.id.button_6),
            view.findViewById(R.id.button_7),
            view.findViewById(R.id.button_8),
            view.findViewById(R.id.button_9),
            view.findViewById(R.id.button_10)
        )

        return view
    }

    fun filterButtons(query: String) {
        for (button in buttons) {
            val buttonText = button.text.toString()
            if (buttonText.contains(query, ignoreCase = true)) {
                button.visibility = View.VISIBLE
            } else {
                button.visibility = View.GONE
            }
        }
    }
}
