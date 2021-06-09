package com.bangkit.dermaapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.dermaapp.DiseaseHistoryActivity.Companion.FIREBASE_URL
import com.bangkit.dermaapp.databinding.ActivityDiseaseByDoctorBinding
import com.bangkit.dermaapp.history.entity.HistoryPenyakit
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DiseaseByDoctorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiseaseByDoctorBinding
    private lateinit var refHistoryPenyakit: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    companion object {
        const val UID_HISTORY = "history_uid"
        const val ID_IMAGE = "id_key"
        const val IMG_LINK = "image_link"
        const val EXTRA_DISEASE_SYSTEM = "extra_disease_system"
        const val NAME_DOCTOR ="doctor_name"
        const val EXTRA_BY_DOCTOR = "extra_by_doctor"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseaseByDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Penyakit Kulit"
        firebaseAuth = FirebaseAuth.getInstance()

        val imgLink = intent.getStringExtra(IMG_LINK)
        val diseaseBySystem = intent.getStringExtra(EXTRA_DISEASE_SYSTEM)
        val nameDoctor = intent.getStringExtra(NAME_DOCTOR)
        val diseaseByDoctor = intent.getStringExtra(EXTRA_BY_DOCTOR)

        refHistoryPenyakit =
            FirebaseDatabase.getInstance(FIREBASE_URL).getReference("riwayat_penyakit")


        Glide.with(this)
            .load(imgLink)
            .into(binding.imageDisease)

        binding.edDetectionBySystem.setText(diseaseBySystem)
        binding.tvNameDoctor.text = nameDoctor
        binding.btnSave.setOnClickListener {
            diseaseDicideByDoctor()
        }
        binding.edDetectionByDoctor.setText(diseaseByDoctor)

        refHistoryPenyakit.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                val key = snapshot.key
                Log.d("THIS 1", snapshot.toString())
                Log.d("THIS 2", children.toString())
                Log.d("THIS 3", key.toString())

                for (h in snapshot.children) {
                    val historyPenyakit = h.getValue(HistoryPenyakit::class.java)
                    Log.d("THIS 9", h.key.toString())
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        /*refHistoryPenyakit.child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {


                val children = snapshot.children

                val key = snapshot.key
                Log.d("THIS 1", snapshot.toString())
                Log.d("THIS 2", children.toString())
                Log.d("THIS 3", key.toString())

                for (h in snapshot.children) {
                    val historyPenyakit = h.getValue(HistoryPenyakit::class.java)
                    Log.d("THIS 4", h.toString())
                    Log.d("THIS 5", snapshot.toString())
                    Log.d("THIS 6", snapshot.children.toString())
                    Log.d("THIS 7", historyPenyakit.toString())
                    //historyPenyakitListArray.add(historyPenyakit!!)
                    Log.d("THIS 8", snapshot.key.toString())
                    Log.d("THIS 9", h.key.toString())
                }




            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
*/


    }

    private fun diseaseDicideByDoctor() {
        //val uid = firebaseAuth.currentUser?.uid.toString()
        val imageSkin = intent.getStringExtra(IMG_LINK).toString()
        val diseaseBySystem = binding.edDetectionBySystem.text.toString().trim()
        val doctorName = firebaseAuth.currentUser?.displayName.toString().trim()
        val diseaseByDoctor = binding.edDetectionByDoctor.text.toString().trim()
        val idKeyUid = intent.getStringExtra(UID_HISTORY).toString()
        val idKeyImage = intent.getStringExtra(ID_IMAGE).toString()
        val treatment = binding.edRecommendationTreatment.text.toString().trim()

        Log.d("THIS", idKeyImage)

        val historyPenyakit = HistoryPenyakit(
            idKeyUid, idKeyImage, imageSkin, diseaseBySystem, doctorName, diseaseByDoctor, treatment
        )

        refHistoryPenyakit.child(idKeyUid).child(idKeyImage).setValue(historyPenyakit)
            .addOnCompleteListener {
                //jika data berhasil ditambahkan
                showSnackbarMessage("Berhasil dikirim ke user")
                Log.d("THIS", "data berhasil disimpan")
            }

    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding?.root as View, message, Snackbar.LENGTH_SHORT).show()
    }
}