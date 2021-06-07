/*
package com.bangkit.dermaapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.dermaapp.DiseaseHistoryActivity
import com.bangkit.dermaapp.history.entity.HistoryPenyakit
import com.google.firebase.database.*

class HistoryDiseaseViewModel : ViewModel() {

    private var history = MutableLiveData<ArrayList<HistoryPenyakit>>()
    var historyPenyakitListArray = ArrayList<HistoryPenyakit>()
    private var refAllHistory: DatabaseReference =
        FirebaseDatabase.getInstance(DiseaseHistoryActivity.FIREBASE_URL)
            .getReference("riwayat_penyakit")

    fun getAllHistoryUsers() : LiveData<ArrayList<HistoryPenyakit>> {
        history = MutableLiveData<ArrayList<HistoryPenyakit>>()
        showHistoryAllUser()

        return history

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
                        //  Log.d("TAG atas 4", isChild.toString())
                        Log.d("TAG atas 5", key.toString())
                        Log.d("TAG atas 7", child.childrenCount.toString())
                        Log.d("TAG atas 8", child.value.toString())
                        Log.d("TAG atas 9", "------------------")
                    }



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
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.details)
                Log.d("TAG", error.toException().toString())
            }

        })

    }

}*/
