package com.example.logicgo.service

import android.util.Log

fun exc(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        e.print()
    }
}

fun Exception.print() {
    printStackTrace()
}

fun Any?.log() {
    Log.wtf("FATZ", "$this")
}