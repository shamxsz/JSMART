package com.example.jsmart

import DatabaseHelper
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.jsmart.databinding.ActivityLoginScreenBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginScreen : BaseActivity() {
    private var binding: ActivityLoginScreenBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var dbHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = Firebase.auth
        dbHelper = DatabaseHelper(this) // Initialize SQLite database

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding?.tvRegister?.setOnClickListener {
            startActivity(Intent(this, SignUpScreen::class.java))
            finish()
        }

        binding?.tvForgotPassword?.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }

        binding?.btnLogin?.setOnClickListener {
            loginUser()
        }

        binding?.btnSignInWithGoogle?.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun loginUser(){
        val email = binding?.etEmailAddress?.text.toString()
        val password = binding?.etPassword?.text.toString()
        if (validateForm(email,password)){
            showProgressBar()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        hideProgressBar()
                    }
                    else{
                        showToast(this, "Can't login currently. Try again later")
                        hideProgressBar()
                    }
                }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            account?.let {
                firebaseAuthWithGoogle(it)
            }
        } else {
            showToast(this, "Google Login Failed")
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        showProgressBar()
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            hideProgressBar()
            if (task.isSuccessful) {
                val firebaseUser = auth.currentUser
                firebaseUser?.let {
                    val existingUser = dbHelper.getUserById(it.uid)

                    if (existingUser == null) {
                        // Store user only if they are not already in SQLite
                        val userInfo = User(it.uid, it.displayName ?: "Unknown", it.email ?: "", it.photoUrl?.toString())
                        dbHelper.insertUser(userInfo)
                    }
                }

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                showToast(this, "Can't login currently. Try again later")
            }
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding?.etEmailAddress?.error = "Enter a valid email address"
                false
            }
            TextUtils.isEmpty(password) -> {
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
