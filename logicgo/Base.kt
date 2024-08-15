package com.example.logicgo

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.logicgo.service.exc

abstract class Base<T : ViewBinding> : AppCompatActivity() {

    lateinit var bind: T
    abstract fun setBind(inflater: LayoutInflater): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = setBind(layoutInflater)
        setContentView(bind.root)

        exc { initUI() }
    }

    abstract fun initUI()

    fun <T> T.tos() = Toast.makeText(this@Base, "$this", Toast.LENGTH_SHORT).show()

}