package com.example.jsmart

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jsmart.databinding.ActivityWelcomeScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class WelcomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Thread.sleep(2000)
        installSplashScreen()
        setContentView(R.layout.activity_welcome_screen)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        btnLogin.setOnClickListener {
            val loginIntent = Intent(this,LoginScreen::class.java)
            startActivity(loginIntent)

        }
        btnSignUp.setOnClickListener {
            val signupIntent = Intent(this,SignUpScreen::class.java)
            startActivity(signupIntent)

        }

        val auth = Firebase.auth
        if (auth.currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }


}