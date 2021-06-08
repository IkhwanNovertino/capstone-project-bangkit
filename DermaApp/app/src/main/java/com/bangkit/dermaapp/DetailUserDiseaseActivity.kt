package com.bangkit.dermaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.dermaapp.databinding.ActivityDetailUserDiseaseBinding
import com.bumptech.glide.Glide

class DetailUserDiseaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserDiseaseBinding

    companion object {
        const val IMAGE_DETAIL = "image_detail"
        const val DETECT_SYSTEM = "detect_system"
        const val DETECT_DOCTOR = "detect_doctor"
        const val NAME_DOCTOR_DETAIL = "name_doctor_detail"
        const val TREATMENT = "treatment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserDiseaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataAndShowIt()
    }

    private fun getDataAndShowIt() {
        val image = intent.getStringExtra(IMAGE_DETAIL)
        val detectSystem = intent.getStringExtra(DETECT_SYSTEM)
        val detectDoctor = intent.getStringExtra(DETECT_DOCTOR)
        val nameDoctor = intent.getStringExtra(NAME_DOCTOR_DETAIL)
        val treatment = intent.getStringExtra(TREATMENT)

        Glide.with(this)
            .load(image)
            .into(binding.imageSkin)

        with(binding) {
            tvPenyakitBySistem.text = detectSystem
            tvPenyakitByDokter.text = detectDoctor
            tvNameDoctor.text = nameDoctor
            tvTreatmentRecommendations.text = treatment
        }
    }
}