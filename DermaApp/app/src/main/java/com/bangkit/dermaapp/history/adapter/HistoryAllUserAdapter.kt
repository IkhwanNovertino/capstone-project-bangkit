package com.bangkit.dermaapp.history.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.dermaapp.DiseaseByDoctorActivity
import com.bangkit.dermaapp.DiseaseByDoctorActivity.Companion.EXTRA_BY_DOCTOR
import com.bangkit.dermaapp.DiseaseByDoctorActivity.Companion.NAME_DOCTOR
import com.bangkit.dermaapp.R
import com.bangkit.dermaapp.databinding.ItemHistoryAllUserBinding
import com.bangkit.dermaapp.history.entity.HistoryPenyakit
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class HistoryAllUserAdapter : RecyclerView.Adapter<HistoryAllUserAdapter.HistoryViewHolder>() {

    private var listHistory = ArrayList<HistoryPenyakit>()

    fun setHistoryAllUsers(history: ArrayList<HistoryPenyakit>) {

            this.listHistory.clear()
            this.listHistory.addAll(history)
        notifyDataSetChanged()

        //if (history == null) return


    }



    inner class HistoryViewHolder(private val binding: ItemHistoryAllUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryPenyakit) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(history.gambar_penyakit)
                    .error(R.drawable.ic_user)
                    .apply(RequestOptions().override(85, 85))
                    .into(imgPenyakit)

                tvPenyakitBySistem.text = history.penyakit_berdarkan_sistem


                if (history.penyakit_berdasar_dokter == ""){
                    tvStatus.text = "Status : Belum diperiksa"
                    imageStatus.setImageResource(R.drawable.ic_not_yet)
                } else {
                    tvStatus.text = "Status : Sudah diperiksa"
                    imageStatus.setImageResource(R.drawable.ic_confirm)
                }
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DiseaseByDoctorActivity::class.java)
                    intent.putExtra(DiseaseByDoctorActivity.UID_HISTORY, history.uid)
                    intent.putExtra(DiseaseByDoctorActivity.ID_IMAGE, history.id_gambar)
                    intent.putExtra(EXTRA_BY_DOCTOR, history.penyakit_berdasar_dokter)
                    intent.putExtra(DiseaseByDoctorActivity.IMG_LINK, history.gambar_penyakit)
                    intent.putExtra(NAME_DOCTOR, history.nama_dokter)
                    intent.putExtra(
                        DiseaseByDoctorActivity.EXTRA_DISEASE_SYSTEM,
                        history.penyakit_berdarkan_sistem
                    )
                    itemView.context.startActivity(intent)
                }


            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAllUserAdapter.HistoryViewHolder {
        val itemHistoryBinding =
            ItemHistoryAllUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(itemHistoryBinding)
    }

    override fun onBindViewHolder(holder: HistoryAllUserAdapter.HistoryViewHolder, position: Int) {
        holder.bind(listHistory[position])
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }
}

