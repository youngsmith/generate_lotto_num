package com.example.myapplication

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.lang.StringBuilder
import java.text.NumberFormat
import java.util.*
import kotlin.random.Random
import kotlin.random.nextUInt

class MainActivity : AppCompatActivity() {
    val mapper = jacksonObjectMapper()
    val numFormatter = NumberFormat.getNumberInstance()
    val drewNumArray : IntArray = IntArray(6)

    @RequiresApi(Build.VERSION_CODES.N)
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

                drewNumArray[totalDrawCount] = signedDrewNum

                totalDrawCount += 1
                if(totalDrawCount >= MAX_DRAW_NUM_COUNT) break
            }

            drewNumArray.sort()
            drewNumArray.forEach {
                    result.append(it)
                    result.append(" ")
                }

            lotto_num_textView.text = result
        }


        get_lotto_info_btn.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            val round = round_editText.text
            val url = LOTTO_URL + round

            val stringRequest = StringRequest(
                 Request.Method.GET
                ,url
                ,Response.Listener<String> { response ->
                    var resultMap : HashMap<String, String> = HashMap<String, String>()
                    try {
                        resultMap = mapper.readValue(response, object : TypeReference<HashMap<String, String>>() {})
                    }
                    catch (exception : Exception) {
                        println(exception)
                    }

                    // 로또 당첨 번호 세팅
                    val winningNum : StringBuilder = StringBuilder()
                    resultMap
                        .asSequence()
                        .filter { it.key.startsWith(DRAW_NUM_PREFIX) }
                        .forEach {
                            winningNum.append(it.value)
                            winningNum.append(" ")
                        }
                    winning_num_textView.text = winningNum

                    // 상금 세팅
                    //.getOrDefault("firstWinamnt", NO_DATA)
                    val winnings : String = resultMap
                        .get("firstWinamnt")
                        ?.let { numFormatter.format(it.toLong()) } ?: NO_DATA

                    winnings_textView.text = winnings
                }
                ,Response.ErrorListener {
                    winnings_textView.text = RESPONSE_ERROR
                }
            )

            queue.add(stringRequest)
        }


        simulation_button.setOnClickListener {
            var totalTryCount : Long = 0
            val check: BitSet = BitSet(47)
            var totalDrawCount: Int = 0
            val tryDrewNumArray : IntArray = IntArray(6)

            while(true) {
                totalTryCount += 1
                check.clear()
                totalDrawCount = 0
                tryDrewNumArray.fill(0)

                while (true) {
                    val unsignedDrewNum: UInt = Random.nextUInt() % MAX_LOTTO_NUM + MIN_LOTTO_NUM
                    val signedDrewNum: Int = unsignedDrewNum.toInt()

                    if (check.get(signedDrewNum)) continue
                    check.set(signedDrewNum)

                    tryDrewNumArray[totalDrawCount] = signedDrewNum

                    totalDrawCount += 1
                    if (totalDrawCount >= MAX_DRAW_NUM_COUNT) break
                }

                tryDrewNumArray.sort()

                var allMatch : Boolean = true
                for(idx : Int in 0..5) {
                    if(drewNumArray[idx] != tryDrewNumArray[idx]) {
                        allMatch = false
                        break
                    }
                }

                if(allMatch || totalTryCount > 9000000) break
            }

            simulation_result_textView.text = totalTryCount.toString()
        }
    }


}
