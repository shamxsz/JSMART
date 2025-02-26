package com.example.jsmart

import DatabaseHelper
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.jsmart.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var dbHelper: DatabaseHelper
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        dbHelper = DatabaseHelper(requireContext())

        binding.ibSignOut.setOnClickListener {
            signOutUser()
        }

        binding.btnChangeProfile.setOnClickListener {
            pickImageFromGallery()
        }

        loadUserData()

        return binding.root
    }

    private fun signOutUser() {
        if (auth.currentUser != null) {
            auth.signOut()
            val intent = Intent(requireContext(), WelcomeScreen::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imageUri?.let { uri ->
                // ✅ Directly display the selected image
                Glide.with(this)
                    .load(uri)
                    .into(binding.ivProfilePicture)
            }
        }
    }
    private fun loadUserData() {
        val user = getUserData()
        if (user != null) {
            binding.tvName.text = user.name

            if (!user.profilePhoto.isNullOrEmpty()) {
                Glide.with(this)
                    .load(user.profilePhoto)
                    .into(binding.ivProfilePicture)
            }
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

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
