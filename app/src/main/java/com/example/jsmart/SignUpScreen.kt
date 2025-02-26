package com.example.jsmart

import DatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import com.example.jsmart.databinding.ActivitySignUpScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpScreen : BaseActivity() {
    private var binding : ActivitySignUpScreenBinding? = null
    private lateinit var auth : FirebaseAuth
    private val dbHelper = DatabaseHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        auth = FirebaseAuth.getInstance()
        binding?.tvLoginPage?.setOnClickListener {
            startActivity(Intent(this,LoginScreen::class.java))
            finish()
        }
        binding?.btnSignUp?.setOnClickListener{
            registerUser()
        }
    }
    private fun registerUser() {
        val name = binding?.etName?.text.toString()
        val email = binding?.etEmailAddress?.text.toString()
        val password = binding?.etPassword?.text.toString()

        if (validateForm(name, email, password)) {
            showProgressBar()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        firebaseUser?.let {
                            val userInfo = User(it.uid, name, email, null)
                            dbHelper.insertUser(userInfo)
                        }

                        showToast(this, "User registered successfully!")
                        hideProgressBar()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        showToast(this, "User registration failed. Try again later.")
                        hideProgressBar()
                    }
                }
        }
    }


    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when{
            TextUtils.isEmpty(name)->{
                binding?.etName?.error = "Enter Name"
                false
            }
            TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()->{
                binding?.etEmailAddress?.error = "Enter valid email address"
                false
            }
            TextUtils.isEmpty(password)->{
                binding?.etPassword?.error = "Enter password"
                false
            }
            password.length < 6 -> {
                binding?.etPassword?.error = "Enter 6 or more characters"
                false
            }
            else -> {
                true
            }
        }
    }


}