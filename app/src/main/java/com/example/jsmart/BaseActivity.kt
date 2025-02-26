package com.example.jsmart

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

open class BaseActivity : AppCompatActivity() {
    private lateinit var pb:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_base)

    }
    fun showProgressBar() {
        pb = Dialog(this, android.R.style.Theme_Black)
        val view = LayoutInflater.from(this).inflate(R.layout.progress_bar, null)

        pb.requestWindowFeature(Window.FEATURE_NO_TITLE)
        pb.window?.setBackgroundDrawableResource(android.R.color.transparent)
        pb.setContentView(view)
        pb.setCancelable(false)
        pb.show()
    }

    fun hideProgressBar(){
        pb.hide()
    }
    fun showToast(activity: Activity,msg:String){
        Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show()
    }
}