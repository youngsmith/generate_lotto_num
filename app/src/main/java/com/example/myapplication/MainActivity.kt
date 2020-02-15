package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder
import java.util.*
import kotlin.random.Random
import kotlin.random.nextUInt


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        toast_button.setOnClickListener {
            Toast.makeText(this@MainActivity, "You clicked me.", Toast.LENGTH_SHORT).show()
        }


        random_button.setOnClickListener {
            val check : BitSet = BitSet(47)
            var count : Int = 0
            val result : StringBuilder = StringBuilder()

            while(true) {
                val unsignedNum : UInt = Random.nextUInt() % 45u + 1u
                val signedNum : Int = unsignedNum.toInt()

                if(check.get(signedNum)) continue

                check.set(signedNum)
                count += 1
                result.append(signedNum)
                result.append(" ")

                if(count == 6) break
            }

            lotto_num_textView.text = result
        }


    }
}
