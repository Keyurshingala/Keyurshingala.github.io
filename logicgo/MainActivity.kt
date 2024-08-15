package com.example.logicgo

import android.view.LayoutInflater
import com.example.logicgo.databinding.ActivityMainBinding
import com.example.logicgo.service.client
import com.example.logicgo.service.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : Base<ActivityMainBinding>() {

    override fun setBind(inflater: LayoutInflater) = ActivityMainBinding.inflate(inflater)

    override fun initUI() {
        CoroutineScope(Dispatchers.IO).launch {
            val res = client().getPosts()

            withContext(Dispatchers.Main){
                res.body().log()
                res.tos()
            }
        }
    }
}