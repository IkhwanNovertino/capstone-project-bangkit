package com.bangkit.dermaapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dermaapp.databinding.ActivityDiseaseHistoryBinding
import com.bangkit.dermaapp.history.adapter.HistoryAllUserAdapter
import com.bangkit.dermaapp.history.adapter.HistoryUserAdapter
import com.bangkit.dermaapp.history.entity.HistoryPenyakit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DiseaseHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiseaseHistoryBinding
    private lateinit var refAllHistory: DatabaseReference
    private lateinit var refHistoryByUser: DatabaseReference
    private lateinit var refUser: DatabaseReference

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var historyAllUserAdapter: HistoryAllUserAdapter
    private lateinit var historyUserAdapter: HistoryUserAdapter


    private var historyPenyakitListArray = ArrayList<HistoryPenyakit>()

    companion object {
        const val FIREBASE_URL =
            "https://b21-cap0391-default-rtdb.asia-southeast1.firebasedatabase.app/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseaseHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Riwayat Pemeriksaan"

        firebaseAuth = FirebaseAuth.getInstance()


        val uid = firebaseAuth.currentUser?.uid.toString()

        refHistoryByUser =
            FirebaseDatabase.getInstance(FIREBASE_URL).getReference("riwayat_penyakit").child(uid)
        refAllHistory = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("riwayat_penyakit")
        refUser = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("users")


        roleUser()


        //testRecyclerView()

        binding.btnDeleted.setOnClickListener {
            clearHistory()
           historyUserAdapter.clearHistory()
        }


    }

    private fun showHistoryAllUser() {
        historyAllUserAdapter = HistoryAllUserAdapter()
        with(binding.rvHistory) {
            layoutManager = LinearLayoutManager(this@DiseaseHistoryActivity)
            setHasFixedSize(true)
            adapter = historyAllUserAdapter
        }

        refAllHistory.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(child: DataSnapshot) {
                historyPenyakitListArray.clear()
                if (child.exists()) {
                    //  historyPenyakitListArray.clear()
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
                    val BA = historyAllUserAdapter.setHistoryAllUsers(historyPenyakitListArray)
                    Log.d("TAGBA", BA.toString())
                    Log.d("TAGBA", historyPenyakitListArray.toString())
                    /*   val adapterHistoryPenyakit = HistoryAdapter(
                           this@DiseaseHistoryActivity,
                           R.layout.item_riwayat,
                           historyPenyakitListArray
                       )
                       binding.lvHistory.adapter = adapterHistoryPenyakit*/


                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message.toString())
            }

        })

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

                historyAllUserAdapter.setHistoryAllUsers(historyPenyakitListArray)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.details)
                Log.d("TAG", error.toException().toString())
            }

        })

    }

    private fun showHistoryByUser() {
        historyUserAdapter = HistoryUserAdapter()
        with(binding.rvHistory) {
            layoutManager = LinearLayoutManager(this@DiseaseHistoryActivity)
            setHasFixedSize(true)
            adapter = historyUserAdapter
        }


        try {
            refHistoryByUser.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        historyPenyakitListArray.clear()
                        for (h in p0.children) {
                            val historyPenyakit = h.getValue(HistoryPenyakit::class.java)
                            if (historyPenyakit != null) {
                                historyPenyakitListArray.add(historyPenyakit)
                            }
                        }
                        historyUserAdapter.setHistoryUser(historyPenyakitListArray)
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
                        showClear(false)
                        showHistoryAllUser()

                        Log.d("ROLE", "Hai Dokter")
                    } else {
                        showClear(true)
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

    private fun showClear(state: Boolean) {
        if (state) {
            binding.tvHistory.visibility = View.VISIBLE
            binding.btnDeleted.visibility = View.VISIBLE
        } else {
            binding.tvHistory.visibility = View.GONE
            binding.btnDeleted.visibility = View.GONE
        }

    }

    private fun clearHistory() {
        refHistoryByUser.removeValue()
    }


}