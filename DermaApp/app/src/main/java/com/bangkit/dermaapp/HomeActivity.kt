package com.bangkit.dermaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import com.bangkit.dermaapp.databinding.ActivityHomeBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        showUser()

        binding.btnLogout.setOnClickListener {
            logout()
        }
        binding.btnScan.setOnClickListener {
            //test retrofit mechanism scan disease with retrofit
          //  val intent = Intent(this, RetrofitExaminationActivity::class.java)

            //test retrofit mechanism scan disease with not retrofit
            //val intent = Intent(this, ExaminationActivity::class.java)
          //  startActivity(intent)
        }

        binding.btnProfile.setOnClickListener {
            val mOptionDialogFragment = ProfileUserDialogFragment()

            val mFragmentManager = supportFragmentManager
            mOptionDialogFragment.show(mFragmentManager,ProfileUserDialogFragment::class.java.simpleName)
        }


    }

    private fun logout(){
        firebaseAuth.signOut()
        /*Intent(this, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }*/
        finish()
    }

    private fun showUser(){
        val user = firebaseAuth.currentUser
        if (user != null){
            if (user.photoUrl != null){
                Glide.with(this)
                    .load(user.photoUrl)
                    .into(binding.imgUserHome)
            } else {
                Glide.with(this)
                    .load(R.drawable.ic_user)
                    .into(binding.imgUserHome)
            }

            binding.tvNameUser.text = user.displayName

        }
    }
}