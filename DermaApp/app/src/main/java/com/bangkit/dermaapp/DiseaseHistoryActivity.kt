package com.bangkit.dermaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.bangkit.dermaapp.DiseaseByDoctorActivity.Companion.EXTRA_DISEASE_SYSTEM
import com.bangkit.dermaapp.DiseaseByDoctorActivity.Companion.ID_IMAGE
import com.bangkit.dermaapp.DiseaseByDoctorActivity.Companion.IMG_LINK
import com.bangkit.dermaapp.DiseaseByDoctorActivity.Companion.UID_HISTORY
import com.bangkit.dermaapp.databinding.ActivityDiseaseHistoryBinding
import com.bangkit.dermaapp.history.adapter.HistoryAdapter
import com.bangkit.dermaapp.history.entity.HistoryPenyakit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DiseaseHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiseaseHistoryBinding
    private lateinit var refAllHistory: DatabaseReference
    private lateinit var refHistoryByUser: DatabaseReference
    private lateinit var refUser: DatabaseReference

    private lateinit var firebaseAuth: FirebaseAuth


    private lateinit var historyPenyakitList: MutableList<HistoryPenyakit>
    private var historyPenyakitListArray = ArrayList<HistoryPenyakit>()

    companion object {
        const val FIREBASE_URL = "https://b21-cap0391-default-rtdb.asia-southeast1.firebasedatabase.app/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseaseHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Riwayat Pemeriksaan"

        firebaseAuth = FirebaseAuth.getInstance()

        val uid = firebaseAuth.currentUser?.uid.toString()

        refHistoryByUser = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("riwayat_penyakit").child(uid)
        refAllHistory = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("riwayat_penyakit")
        refUser = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("users")
        roleUser()


    }

    private fun showHistoryAllUser() {
        refAllHistory.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(child: DataSnapshot) {
                if (child.exists()) {
                    historyPenyakitListArray.clear()
                    Log.d("ALL CHILD", child.toString())
                    for (h in child.children) {
                        val key = h.key
                        if (key != null) {
                            getUid(key)
                        }

                        Log.d("TAG atas 1", h.toString())
                        Log.d("TAG atas 2", child.toString())
                        Log.d("TAG atas 3", child.children.toString())
                        Log.d("TAG atas 4", isChild.toString())
                        Log.d("TAG atas 5", key.toString())
                        Log.d("TAG atas 7", child.childrenCount.toString())
                        Log.d("TAG atas 8", child.value.toString())
                        Log.d("TAG atas 9", "------------------")
                    }
                    val adapterHistoryPenyakit = HistoryAdapter(
                        this@DiseaseHistoryActivity,
                        R.layout.item_riwayat,
                        historyPenyakitListArray
                    )
                    binding.lvHistory.adapter = adapterHistoryPenyakit


                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message.toString())
            }

        })

        binding.lvHistory.setOnItemClickListener { parent, view, position, id ->
            val item = historyPenyakitListArray.get(position)
            val intent = Intent(this, DiseaseByDoctorActivity::class.java)
            intent.putExtra(UID_HISTORY, item.uid)
            intent.putExtra(ID_IMAGE, item.id_gambar)
            intent.putExtra(IMG_LINK, item.gambar_penyakit)
            intent.putExtra(EXTRA_DISEASE_SYSTEM, item.penyakit_berdarkan_sistem)
            startActivity(intent)
        }
    }

    private fun getUid(key: String) {
        refAllHistory.child(key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (h in p0.children) {
                    val historyPenyakit = h.getValue(HistoryPenyakit::class.java)
                    Log.d("TAG bawah KEY 1", h.toString())
                    Log.d("TAG bawah KEY 2", p0.toString())
                    Log.d("TAG bawah KEY 3", p0.children.toString())
                    Log.d("TAG HISTORY ALL", historyPenyakit.toString())
                    historyPenyakitListArray.add(historyPenyakit!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.details)
                Log.d("TAG", error.toException().toString())
            }

        })

    }

    private fun showHistoryByUser() {
        historyPenyakitList = mutableListOf()
        try {
            refHistoryByUser.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        historyPenyakitList.clear()
                        for (h in p0.children) {
                            val historyPenyakit = h.getValue(HistoryPenyakit::class.java)
                            if (historyPenyakit != null) {
                                historyPenyakitList.add(historyPenyakit)
                            }
                        }

                        val adapterHistoryPenyakit = HistoryAdapter(
                            this@DiseaseHistoryActivity,
                            R.layout.item_riwayat,
                            historyPenyakitList
                        )
                        binding.lvHistory.adapter = adapterHistoryPenyakit
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", error.message.toString())
                }

            })
        } catch (e: Exception) {
            Log.d("this", e.message.toString())
        }


    }


    private fun roleUser() {
        val uid = firebaseAuth.currentUser?.uid.toString()
        refUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(child: DataSnapshot) {
                if (child.exists()) {
                    val userRole = child.child(uid).child("role").value.toString()
                    if (userRole == "Dokter") {
                        showHistoryAllUser()
                        Log.d("ROLE", "Hai Dokter")
                    } else {
                        showHistoryByUser()
                        Log.d("ROLE", "Hai User")
                    }
                    Log.d("ROLE", child.child(uid).child("role").value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ROLE ", error.message.toString())
                Log.d("ROLE ", error.toException().toString())
            }

        })

    }

}