package com.tiorisnanto.myapplication.ui.home.fragment.note

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tiorisnanto.myapplication.databinding.ActivityAdminBinding
import kotlinx.android.synthetic.main.activity_details.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    val dbHandler = DBHelper(this, null)
    var dataList = ArrayList<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddNote.setOnClickListener {
            startActivity(Intent(this, DataActivity::class.java))
        }


    }

    override fun onResume() {
        super.onResume()
        toDoList()
    }

    private fun toDoList() {
        binding.tvTotalPendapatanHariIni.text = "Rp. " + dbHandler.totalPendapatan()

        binding.tvTotalPengunjungHariIni.text = dbHandler.totalPengunjung().toString()

        binding.tvTotalPengunjungAnakHariIni.text = dbHandler.totalPengunjungAnak() + " Orang"

        binding.tvTotalPengunjungDewasaHariIni.text = dbHandler.totalPengunjungDewasa() + " Orang"

        binding.tvTotalPendapatan.text = "Rp. " + dbHandler.totalPendapatan1(dateFormat = Date())
    }




}