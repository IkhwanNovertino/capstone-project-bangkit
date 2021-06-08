package com.bangkit.dermaapp.history.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.dermaapp.DetailUserDiseaseActivity
import com.bangkit.dermaapp.DetailUserDiseaseActivity.Companion.DETECT_DOCTOR
import com.bangkit.dermaapp.DetailUserDiseaseActivity.Companion.DETECT_SYSTEM
import com.bangkit.dermaapp.DetailUserDiseaseActivity.Companion.IMAGE_DETAIL
import com.bangkit.dermaapp.DetailUserDiseaseActivity.Companion.NAME_DOCTOR_DETAIL
import com.bangkit.dermaapp.DetailUserDiseaseActivity.Companion.TREATMENT
import com.bangkit.dermaapp.DiseaseByDoctorActivity
import com.bangkit.dermaapp.R
import com.bangkit.dermaapp.databinding.ItemRiwayatBinding
import com.bangkit.dermaapp.history.entity.HistoryPenyakit
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class HistoryUserAdapter : RecyclerView.Adapter<HistoryUserAdapter.HistoryViewHolder>() {

    private var listHistory = ArrayList<HistoryPenyakit>()

    fun setHistoryUser(history: ArrayList<HistoryPenyakit>) {

        this.listHistory.clear()
        this.listHistory.addAll(history)
        notifyDataSetChanged()

        //if (history == null) return


    }

    fun clearHistory(){
        if (listHistory.size > 0){
            this.listHistory.clear()
            notifyDataSetChanged()
        }
    }

    inner class HistoryViewHolder(private val binding: ItemRiwayatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryPenyakit) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(history.gambar_penyakit)
                    .error(R.drawable.ic_user)
                    .apply(RequestOptions().override(85, 85))
                    .into(imgPenyakit)

                tvPenyakitBySistem.text = history.penyakit_berdarkan_sistem
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailUserDiseaseActivity::class.java)
                    intent.putExtra(IMAGE_DETAIL, history.gambar_penyakit)
                    intent.putExtra(DETECT_SYSTEM, history.penyakit_berdarkan_sistem)
                    intent.putExtra(DETECT_DOCTOR, history.penyakit_berdasar_dokter)
                    intent.putExtra(NAME_DOCTOR_DETAIL, history.nama_dokter)
                    intent.putExtra(TREATMENT, history.rekomendasi_pengobatan)
                    itemView.context.startActivity(intent)
                }

                if (history.penyakit_berdasar_dokter == ""){
                    tvStatus.text = "Status : Belum diperiksa"
                    imageStatus.setImageResource(R.drawable.ic_not_yet)
                } else {
                    tvStatus.text = "Status : Sudah diperiksa"
                    imageStatus.setImageResource(R.drawable.ic_confirm)
                }


            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryUserAdapter.HistoryViewHolder {
        val itemHistoryBinding =
            ItemRiwayatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(itemHistoryBinding)
    }

    override fun onBindViewHolder(holder: HistoryUserAdapter.HistoryViewHolder, position: Int) {
        holder.bind(listHistory[position])
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }
}

