package com.bangkit.dermaapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bangkit.dermaapp.databinding.FragmentProfileUserDialogBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class ProfileUserDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentProfileUserDialogBinding

    private lateinit var imageUri: Uri
    private lateinit var fbAuth: FirebaseAuth


    companion object {
        private const val REQ_CAMERA_USER_DIALOG = 300
        private const val PER_CAMERA_USER_DIALOG = 333
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_user_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileUserDialogBinding.bind(view)

        fbAuth = FirebaseAuth.getInstance()

        val user = fbAuth.currentUser

        if (user != null) {
            if (user.photoUrl != null) {
                Glide.with(this)
                    .load(user.photoUrl)
                    .into(binding.imgUser)
            } else {
                binding.imgUser.setImageResource(R.drawable.ic_user)
            }

            binding.edName.setText(user.displayName)
            binding.edEmail.setText(user.email)


            if (user.isEmailVerified) {
                statusVerifEmail(true)
            } else {
                statusVerifEmail(false)
            }


        }

        binding.tvSendLinkVerifEmail.setOnClickListener {
            loading(true)
            user?.sendEmailVerification()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showSnackbarMessage("Link verifikasi sudah dikirim ke email")
                } else {
                    showSnackbarMessage("Gagal kirim link verifikasi ke email")
                }

                loading(false)
            }
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
                binding.edName.error = "Nama masih kosong"
                binding.edName.requestFocus()
                return@setOnClickListener
            }

            UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(image)
                .build().also {
                    user?.updateProfile(it)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            showSnackbarMessage("Data profil berhasil di ubah")
                            Intent(activity, HomeActivity::class.java).also { intent ->
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                        } else {
                            showSnackbarMessage("Data profil tidak berhasil dirubah")
                        }
                    }
                }
        }

        binding.btnLogout.setOnClickListener { logout() }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun camera() {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQ_CAMERA_USER_DIALOG)
        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.CAMERA),
                PER_CAMERA_USER_DIALOG
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CAMERA_USER_DIALOG && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadImageUser(imageBitmap)
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

    private fun statusVerifEmail(status: Boolean) {
        if (status) {
            binding.imgVerifUser.setImageResource(R.drawable.ic_verified_user)
            binding.tvStatusEmail.text = "Status Email: Sudah Terverifikasi"
        } else {
            binding.imgVerifUser.setImageResource(R.drawable.ic_unverified_user)
            binding.tvStatusEmail.text = "Status Email: Belum Terverifikasi"
            binding.tvSendLinkVerifEmail.text = "Tekan untuk verifikasi email"
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PER_CAMERA_USER_DIALOG) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQ_CAMERA_USER_DIALOG)
            } else {
                showSnackbarMessage("Belum ada izin menggunakan camera")
            }

        }
    }


    private fun logout() {
        fbAuth.signOut()
        /*Intent(this, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }*/
        activity?.finish()
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding?.root as View, message, Snackbar.LENGTH_SHORT).show()
    }


}