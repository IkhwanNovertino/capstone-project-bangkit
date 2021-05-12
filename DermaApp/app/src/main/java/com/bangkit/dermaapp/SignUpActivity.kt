package com.bangkit.dermaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
            if (email.isNotEmpty() || password.isNotEmpty()){
                createUser(email, password)
                Toast.makeText(this, "sudah terisi", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Email dan password tolong di isi :)", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun createUser(email: String, password: String){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task ->
                    if (task.isSuccessful){
                        Log.e("TASK MESSAGE", "successful")


                    } else {

                        Log.e("TASK MESSAGE","failed")

                    }
                }

    }
}