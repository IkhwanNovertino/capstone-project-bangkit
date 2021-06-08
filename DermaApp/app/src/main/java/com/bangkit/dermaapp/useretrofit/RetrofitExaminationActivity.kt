package com.bangkit.dermaapp.useretrofit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.dermaapp.R
import com.bangkit.dermaapp.databinding.ActivityRetrofitExaminationBinding
import com.bangkit.dermaapp.history.entity.HistoryPenyakit
import com.bangkit.dermaapp.useretrofit.ApiConfig.getRetrofit
import com.bangkit.dermaapp.retrofit.ApiService
import com.bangkit.dermaapp.retrofit.ImageResponse
import com.bangkit.dermaapp.testretrofit.Config
import com.bangkit.dermaapp.testretrofit.ResponseTest
import com.bangkit.dermaapp.testretrofit.ResponseTest2
import com.bangkit.dermaapp.testretrofit.Service
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class RetrofitExaminationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRetrofitExaminationBinding
    private lateinit var bitmap: Bitmap

    private lateinit var loadingView: AlertDialog

    //private lateinit var refUser: DatabaseReference
    private lateinit var refHistoryPenyakit: DatabaseReference

    private lateinit var firebaseAuth: FirebaseAuth




    companion object {
        const val CAMERA_PERMISSION_CODE = 600
        const val CAMERA_REQUEST_CODE = 666
        private const val GALLERY_PERMISSION_CODE = 900
        private const val GALLERY_REQUEST_CODE = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetrofitExaminationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //ref = FirebaseDatabase.getInstance("https://b21-cap0391-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("history")
        firebaseAuth = FirebaseAuth.getInstance()


        //refUser = FirebaseDatabase.getInstance("https://b21-cap0391-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")

        refHistoryPenyakit = FirebaseDatabase.getInstance("https://b21-cap0391-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("riwayat_penyakit")

        Log.d("RDB", FirebaseDatabase.getInstance().reference.toString())

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.loading_dialog)
        loadingView = builder.create()

        binding.btnGallery.setOnClickListener {
            gallery()
        }
        binding.btnCamera.setOnClickListener {


            camera()
/*
            val penyakitBySistem = binding.tvDiseaseName.text.toString()
            val penyakitByDokter = binding.tvTreatmentRecommendations.text.toString()
            val namaDokter = ""
            val imageLink = binding.tvDiseaseName.text.toString()

            ref = FirebaseDatabase.getInstance().getReference("history")

            val historyId = ref.push().key

            val history = HistoryPenyakit(
                historyId, imageLink
            )

            Log.d("RDB", "History Penyakit ${history.image_penyakit}")

            Log.d("RDB", "$historyId")

            Log.d("RDB", history.id!!)

            Log.d("RDB", FirebaseDatabase.getInstance().reference.toString())

                ref.child(history.id).setValue(history).addOnCompleteListener {
                    //jika data berhasil ditambahkan

                    Log.d("RDB", "masuk ke ref success")
                    Toast.makeText(
                        this,
                        "Data berhasil ditambahkan :)",
                        Toast.LENGTH_SHORT
                    ).show()
                }

*/


        }

        binding.btnPrediction.setOnClickListener {
            //uploadTestImage()
            uploadImage()
        }

    }

/*
    private fun saveDataProto(){
        val imageLink = binding.tvDiseaseName.text.toString()

        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val historyId = ref.push().key

        val history = HistoryPenyakit(
            historyId, imageLink
        )

        Log.d("RDB", "History Penyakit ${history.image_penyakit}")

        Log.d("RDB", "$historyId")

        Log.d("RDB", history.id!!)

        if (historyId != null) {
            ref.child(uid).setValue(history).addOnCompleteListener {
                //jika data berhasil ditambahkan

                Log.d("RDB", "masuk ke ref success")
                Toast.makeText(
                    this,
                    "Data berhasil ditambahkan :)",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }
*/

    private fun camera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                Toast.makeText(this, "BLM ADA PERMISSION KK", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun gallery() {
        Log.d("TAG", "gallery")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding?.root as View, message, Snackbar.LENGTH_SHORT).show()
    }

    private val image: String
        get() {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val dataByte = baos.toByteArray()
            return Base64.encodeToString(dataByte, Base64.DEFAULT)
        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            bitmap = data!!.extras!!.get("data") as Bitmap
            binding.imgIndication.setImageBitmap(bitmap)
        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            Log.d("TAG", "gallery")
            binding.imgIndication.setImageURI(data?.data)
            Log.d("TAG", "masukkan ke img 1")
            val uri: Uri? = data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            Log.d("TAG", "coba masuk ke function image upload")
        }
    }

    private fun uploadImage() {
        val title = "IMG_${System.currentTimeMillis()}"

        val api = getRetrofit()!!.create(ApiService::class.java)
        val call = api.uploadImageImgur(title, image)


        call.enqueue(object : Callback<ImageResponse> {
            override fun onResponse(
                call: Call<ImageResponse>,
                response: Response<ImageResponse>
            ) {
                try {
                    val dataSemua = response.body()
                    Log.d("Response", response.isSuccessful.toString())
                    val dataLink = response.body()?.data?.link.toString()
                    val dataStatus = response.body()?.status.toString()
                    Log.d("RESPONSE", dataSemua.toString())
                    Log.d("RESPONSE", dataLink)
                    Log.d("RESPONSE", dataStatus)
                    Log.d("RESPONSE", dataSemua?.data?.name.toString())
                    Log.d("RESPONSE", dataSemua?.data?.title.toString())

                    val link = dataSemua?.data?.link.toString()

                    binding.tvDiseaseName.text = link
                    // save to database
/*

                    Glide.with(this@RetrofitExaminationActivity)
                        .load(link)
                        .apply(RequestOptions().override(200, 200))
                        .error(R.drawable.ic_launcher_background)
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(binding.imgSegmentation)
*/

                  //  saveDataProto()

                    //saveUser()

                    //save history

                    val uid = firebaseAuth.currentUser?.uid.toString()
                    val imageSkin = link
                    val diseaseBySystem = "Dummy Scabies"
                    val doctorName = ""
                    val diseaseByDoctor = ""
                    val recommendationTreatment = ""

                    val idDisease = refHistoryPenyakit.push().key

                    //val mhsId = ref.push().key
                    val historyPenyakit = HistoryPenyakit(
                        uid,idDisease,imageSkin,diseaseBySystem, doctorName, diseaseByDoctor, recommendationTreatment
                    )

                    refHistoryPenyakit.child(uid).child(idDisease!!).setValue(historyPenyakit).addOnCompleteListener {
                        //jika data berhasil ditambahkan
                        //Toast.makeText(applicationContext, "Data berhasil ditambahkan :)", Toast.LENGTH_SHORT).show()
                        Log.d("THIS", "data berhasil ditambahkan")
                    }


                } catch (e: Exception) {
                    Log.d("error", e.message.toString())
                }


            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            override fun onFailure(
                call: Call<ImageResponse>,
                t: Throwable
            ) {
                Log.d(
                    "Error Response", "GAGAL"
                )
            }

        }
        )
    }


/*
    private fun uploadTestImage() {
        val title = "IMG_${System.currentTimeMillis()}"

        val api = Config.getTestRetrofit()!!.create(Service::class.java)
        val call = api.uploadTestImageImgur(image)


        call.enqueue(object : Callback<ResponseTest2> {
            override fun onResponse(
                call: Call<ResponseTest2>,
                response: Response<ResponseTest2>
            ) {
                try {
                    val dataSemua = response.body()
                    Log.d("Response", response.isSuccessful.toString())
                    val dataLink = response.body()?.data
                    val dataStatus = response.body()?.status.toString()
                    Log.d("RESPONE DATA", dataSemua.toString())
                    Log.d("RESPONSE SUCCESS", dataLink.toString())
                    Log.d("RESPONSE CODE", dataStatus)

                    val nameDiseaseSkin = response.body()?.data?.disease
                    binding.tvDiseaseName.text = nameDiseaseSkin



                } catch (e: Exception) {
                    Log.d("error", e.message.toString())
                }


            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            override fun onFailure(
                call: Call<ResponseTest2>,
                t: Throwable
            ) {
                Log.d(
                    "Error Response", "GAGAL"
                )

                Log.d(
                    "Error Response", t.message.toString()
                )
                Log.d(
                    "Error Response", t.localizedMessage!!.toString()
                )
            }

        }
        )
    }*/
}