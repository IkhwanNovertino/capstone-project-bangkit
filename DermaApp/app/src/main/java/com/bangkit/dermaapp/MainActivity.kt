package com.bangkit.dermaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bangkit.dermaapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.button.setOnClickListener {

            val email = binding.edtEmail.text.trim().toString()
            val password = binding.edtPassoword.text.trim().toString()

            if (email.isNotEmpty() || password.isNotEmpty()){
                login(email, password)
            } else {
                Toast.makeText(this,"Tolong di input",Toast.LENGTH_LONG).show()
            }

        }

        binding.tvCreateUser.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(email: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
            if (task.isSuccessful){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this, "Error",Toast.LENGTH_LONG).show()
            }
        }
    }
}