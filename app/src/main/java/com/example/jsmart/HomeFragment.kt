package com.example.jsmart

import DatabaseHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jsmart.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext()) // Corrected context passing
        auth = FirebaseAuth.getInstance()
        loadUserGreetings()
        return binding.root
    }

    private fun loadUserGreetings() {
        val user = getUserData()
        if (user != null) {
            val name = user?.name?.takeIf { it.isNotBlank() } ?: "Guest"
            binding.tvGreetings.text = "Hello, $name"

        }
    }

    private fun getUserData(): User? {
        val firebaseUser = auth.currentUser
        return firebaseUser?.let { dbHelper.getUserById(it.uid) }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}