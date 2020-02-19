package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder
import java.util.*
import kotlin.random.Random
import kotlin.random.nextUInt

class MainActivity : AppCompatActivity() {
    val mapper = jacksonObjectMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        random_button.setOnClickListener {
            val check : BitSet = BitSet(47)
            var totalDrawCount : Int = 0
            val result : StringBuilder = StringBuilder()

            while(true) {
                val unsignedDrewNum : UInt = Random.nextUInt() % MAX_LOTTO_NUM + MIN_LOTTO_NUM
                val signedDrewNum : Int = unsignedDrewNum.toInt()

                if(check.get(signedDrewNum)) continue

                check.set(signedDrewNum)
                totalDrawCount += 1
                result.append(signedDrewNum)
                result.append(" ")

                if(totalDrawCount >= MAX_DRAW_NUM_COUNT) break
            }

            lotto_num_textView.text = result
        }


        get_lotto_info_btn.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            val round = round_editText.text
            var url = LOTTO_URL + round

            val stringRequest = StringRequest(
                 Request.Method.GET
                ,url
                ,Response.Listener<String> { response ->
                    val resultMap : HashMap<String, String> = mapper.readValue(response, object : TypeReference<HashMap<String, String>>() {})
                    val winningNum : StringBuilder = StringBuilder()

                    resultMap.asSequence()
                         .filter { it.key.startsWith(DRAW_NUM_PREFIX) }
                        ?.forEach {
                            winningNum.append(it.value)
                            winningNum.append(" ")
                        }

                    winnings_textView.text = resultMap["firstWinamnt"]
                    winning_num_textView.text = winningNum
                }
                ,Response.ErrorListener {
                    winnings_textView.text = "did not work!"
                }
            )


            queue.add(stringRequest)
        }
    }


}
