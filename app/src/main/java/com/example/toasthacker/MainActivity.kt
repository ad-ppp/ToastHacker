package com.example.toasthacker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        tvConfirm.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Handler().postDelayed({
            val toast = Toast.makeText(this,"lalalala",Toast.LENGTH_SHORT)
            ToastHacker.tryToHack(toast)
            toast.show()
        },2000)
    }
}
