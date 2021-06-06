package com.bangkit.dermaapp.history.entity

data class HistoryPenyakit(
    val uid: String,
    val id_gambar: String?,
    val gambar_penyakit: String,
    val penyakit_berdarkan_sistem: String,
    val nama_dokter : String,
    val penyakit_berdasar_dokter: String
){
    constructor(): this("","", "", "", "",""){

    }
}

