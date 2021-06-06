package com.bangkit.dermaapp.history.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bangkit.dermaapp.R
import com.bangkit.dermaapp.history.entity.HistoryPenyakit

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class HistoryAdapter(
    val mCtx: Context,
    val layoutResId: Int,
    val historylList: List<HistoryPenyakit>
) : ArrayAdapter<HistoryPenyakit>(mCtx, layoutResId, historylList) {
    //private lateinit var binding : ItemHistoryBinding

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)

        val view: View = layoutInflater.inflate(layoutResId, null)
        val imagePenyakit : ImageView = view.findViewById(R.id.img_penyakit)
        val tvPenyakitSistem : TextView = view.findViewById(R.id.tv_penyakit_by_sistem)
        val tvNamaDokter : TextView = view.findViewById(R.id.tv_nama_dokter)
        val tvPenyakitDoktor : TextView = view.findViewById(R.id.tv_penyakit_by_dokter)

        val historyPenyakit : HistoryPenyakit = historylList[position]

        tvPenyakitSistem.text = historyPenyakit.penyakit_berdarkan_sistem
        tvPenyakitDoktor.text = historyPenyakit.penyakit_berdasar_dokter
        tvNamaDokter.text = historyPenyakit.nama_dokter

        try {
            Glide.with(mCtx)
                .load(historyPenyakit.gambar_penyakit)
                .error(R.drawable.ic_user)
                .apply(RequestOptions().override(85,85))
                .into(imagePenyakit)
        } catch (e: Exception){
            Log.d("TAG", e.message.toString())
        }


        return view



    }
}