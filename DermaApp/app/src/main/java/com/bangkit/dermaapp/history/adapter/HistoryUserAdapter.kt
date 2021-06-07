package com.bangkit.dermaapp.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
                tvPenyakitByDokter.text = history.penyakit_berdasar_dokter
                tvNamaDokter.text = history.nama_dokter


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

