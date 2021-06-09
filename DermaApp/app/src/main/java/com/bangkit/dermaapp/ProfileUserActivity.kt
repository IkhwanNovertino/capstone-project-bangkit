package com.bangkit.dermaapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.dermaapp.DiseaseHistoryActivity.Companion.FIREBASE_URL
import com.bangkit.dermaapp.databinding.ActivityProfileUserBinding
import com.bangkit.dermaapp.history.entity.User
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class ProfileUserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileUserBinding
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var imageUri: Uri
    private lateinit var refUser : DatabaseReference

    companion object{
        private const val REQ_CAMERA_USER = 123
        private const val PER_CAMERA_USER = 321
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fbAuth = FirebaseAuth.getInstance()
        val user = fbAuth.currentUser

        refUser = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("users")

        if (user != null) {
            if (user.photoUrl != null) {
                Log.d("this", "NULL 1")
                Glide.with(this)
                    .load(user.photoUrl)
                    .into(binding.imgUser)
            } else {
                Log.d("this", "NULL 2")

                binding.imgUser.setImageResource(R.drawable.ic_user)
            }

            binding.edName.setText(user.displayName)
            binding.edEmail.setText(user.email)

        }



        binding.imgUser.setOnClickListener { camera() }
        binding.btnSave.setOnClickListener {
            val image = when {
                ::imageUri.isInitialized -> imageUri
                user?.photoUrl == null -> Uri.parse("https://picsum.photos/id/316/200")
                else -> user.photoUrl
            }

            val name = binding.edName.text.toString().trim()
            if (name.isEmpty()) {
                binding.edName.error = "Nama belum di isi"
                binding.edName.requestFocus()
                return@setOnClickListener
            }

            saveUser()

            UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(image)
                .build().also {
                    loading(true)
                    user?.updateProfile(it)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Intent(this, HomeActivity::class.java).also { intent ->
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                        } else {
                            showSnackbarMessage("Gagal")
                        }
                        loading(false)
                    }
                }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CAMERA_USER && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadImageUser(imageBitmap)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun camera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQ_CAMERA_USER)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                PER_CAMERA_USER
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PER_CAMERA_USER) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQ_CAMERA_USER)
            } else {
                showSnackbarMessage("Belum ada permission camera")
            }

        }
    }


    private fun uploadImageUser(imageBitmap: Bitmap) {
        loading(true)
        val baos = ByteArrayOutputStream()
        val ref =
            FirebaseStorage.getInstance().reference.child("img/${FirebaseAuth.getInstance().currentUser?.uid}")
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        ref.putBytes(image).addOnCompleteListener {
            if (it.isSuccessful) {
                ref.downloadUrl.addOnCompleteListener {
                    it.result?.let {
                        imageUri = it
                        binding.imgUser.setImageBitmap(imageBitmap)
                    }
                }
            }
            loading(false)
        }
    }

    private fun loading(load: Boolean) {
        if (load) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


    private fun saveUser(){
        val uid = fbAuth.currentUser?.uid.toString()
        val nama = binding.edName.text.toString().trim()

        val checkRole = binding.rgOptionsRole.checkedRadioButtonId

        var role =  ""
        val userRole = binding.rbUser.text.toString().trim()
        val doctorRole = binding.rbDoctor.text.toString().trim()

        if (checkRole != -1){
            when(checkRole){
                R.id.rb_user -> role = userRole
                R.id.rb_doctor -> role = doctorRole
            }

        }

        //val mhsId = ref.push().key


        val user = User(
            uid,nama,role
        )
        refUser.child(uid).setValue(user).addOnCompleteListener {
            Log.d("CHECK","Berhasil")
            //Toast.makeText(applicationContext, "Data berhasil ditambahkan :)", Toast.LENGTH_SHORT).show()
        }


    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding?.root as View, message, Snackbar.LENGTH_SHORT).show()
    }
}