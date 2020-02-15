package com.example.myapplication

import java.lang.StringBuilder
import java.util.*
import kotlin.random.Random
import kotlin.random.nextUInt

fun main() {
    val check : BitSet = BitSet(47)
    var count : Int = 0
    val result : StringBuilder = StringBuilder()

    while(true) {
        val num : UInt = Random.nextUInt() % 45u + 1u
        if(check.get(num.toInt())) continue

        check.set(num.toInt())
        count += 1
        result.append(num)
        result.append(" ")

        if(count == 6) break
    }

    print(result.toString())
}