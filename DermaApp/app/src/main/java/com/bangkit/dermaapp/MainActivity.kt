package com.bangkit.dermaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
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

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.edtEmail.error = "Email tidak valid"
                binding.edtEmail.requestFocus()
            }

            if (password.isEmpty() || password.length < 6){
                binding.edtPassoword.error = "Password kurang dari 6"
                binding.edtPassoword.requestFocus()
            }


            if (email.isNotEmpty() || password.isNotEmpty()){
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.length > 6) {
                    login(email, password)
                }
            } else {
                Toast.makeText(this, "Email dan Password belum di isi",Toast.LENGTH_LONG).show()
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
                Intent(this, HomeActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }

            } else {
                Toast.makeText(this, "Email atau Password salah",Toast.LENGTH_LONG).show()
            }
        }

    }
}