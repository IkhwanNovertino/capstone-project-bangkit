package com.bangkit.dermaapp.history.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.dermaapp.DiseaseByDoctorActivity
import com.bangkit.dermaapp.R
import com.bangkit.dermaapp.databinding.ItemRiwayatBinding
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

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DiseaseByDoctorActivity::class.java)
                    intent.putExtra(DiseaseByDoctorActivity.UID_HISTORY, history.uid)
                    intent.putExtra(DiseaseByDoctorActivity.ID_IMAGE, history.id_gambar)
                    intent.putExtra(DiseaseByDoctorActivity.IMG_LINK, history.gambar_penyakit)
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
            ItemRiwayatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(itemHistoryBinding)
    }

    override fun onBindViewHolder(holder: HistoryAllUserAdapter.HistoryViewHolder, position: Int) {
        holder.bind(listHistory[position])
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }
}


/*
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
}*/
