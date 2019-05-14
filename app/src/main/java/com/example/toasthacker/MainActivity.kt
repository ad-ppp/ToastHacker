package com.example.toasthacker

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.toasthacker.toast.BadTokenListener
import com.example.toasthacker.toast.ToastCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        tvConfirm.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Handler().postDelayed({
            val toast = ToastCompat.makeText(this, "lalalala", Toast.LENGTH_SHORT)
            toast.setBadTokenListener(BadTokenListener {
                Log.d("ToastCompat", "onBadTokenCaught")
            })
            toast.show()
        }, 2000)
    }
}
