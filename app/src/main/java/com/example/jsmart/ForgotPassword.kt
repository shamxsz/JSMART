package com.example.jsmart

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.example.jsmart.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : BaseActivity() {
    private var binding:ActivityForgotPasswordBinding? = null
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        auth = FirebaseAuth.getInstance()

        binding?.btnForgotPasswordSubmit?.setOnClickListener {
            resetPassword()
        }

    }

    private fun validateForm(email: String): Boolean {
        return when {
            TextUtils.isEmpty(email)||!Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding?.etForgotEmailAddress?.error = "Enter valid email address"
                false
            }
            else -> true
        }
    }

    private fun resetPassword(){
        val email = binding?.etForgotEmailAddress?.text.toString()
        if (validateForm(email)){
            showProgressBar()
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    hideProgressBar()
                    binding?.tvSubmitMsg?.visibility = View.VISIBLE
                    binding?.btnForgotPasswordSubmit?.visibility = View.GONE
                }
                else{
                    hideProgressBar()
                    showToast(this, "Cannot reset password at the moment")
                }
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}