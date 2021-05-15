package com.bangkit.dermaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.bangkit.dermaapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnCreate.setOnClickListener {

            val email = binding.edtEmail.text.trim().toString()
            val password = binding.edtPassword.text.trim().toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmail.error = "Email tidak valid"
                binding.edtEmail.requestFocus()
            }

            if (password.isEmpty() || password.length < 6) {
                binding.edtPassword.error = "Password kurang dari 6"
                binding.edtPassword.requestFocus()
            }


            if (email.isNotEmpty() || password.isNotEmpty()) {
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.length > 6) {
                    createUser(email, password)
                }
            } else {
                Toast.makeText(this, "Email dan Password belum di isi", Toast.LENGTH_LONG).show()
            }
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createUser(email: String, password: String){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task ->
                    if (task.isSuccessful){
                        Intent(this, HomeActivity::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                        }
                        Log.e("TASK MESSAGE", "successful")
                    } else {
                        Log.e("TASK MESSAGE","failed")
                    }
                }

    }
}