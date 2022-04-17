package com.tiorisnanto.myapplication.ui.home.fragment.wisata

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tiorisnanto.myapplication.databinding.ActivityBaseWisataBinding
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_putri_malang.AdminActivity
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_putri_malang.DBHelper
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_rais_malang.AdminRaisMalangActivity
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_rais_malang.DBHelperRaisMalang
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.pantai_malang.AdminPantaiMalangActivity
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.pantai_malang.DBHelperPantaiMalang

class BaseWisataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseWisataBinding
    val dbHandler = DBHelper(this, null)
    val dbHandlerRais = DBHelperRaisMalang(this, null)
    val dbHandlerPantai = DBHelperPantaiMalang(this, null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseWisataBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnCobanPutriMalang.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }

        binding.btnCobanRaisMalang.setOnClickListener {
            startActivity(
                Intent(
                    this, AdminRaisMalangActivity::class.java
                )
            )
        }

        binding.btnPantaiMalang.setOnClickListener {
            startActivity(
                Intent(
                    this, AdminPantaiMalangActivity::class.java
                )
            )
        }

    }

    override fun onResume() {
        super.onResume()
        toDoList()
    }

    private fun toDoList() {
        binding.tvBulan.text = dbHandler.totalMonth()
        binding.tvTotalPengunjung.text = "Total Pengunjung = "+ dbHandler.totalPengunjungMonth()
        binding.tvTotalPendapatan.text = "Total Pendapatan = "+ dbHandler.totalPendapatanMonth()

        binding.tvBulanRaisMalang.text = dbHandlerRais.totalMonth()
        binding.tvTotalPengunjungRaisMalang.text = "Total Pengunjung = "+ dbHandlerRais.totalPengunjungMonth()
        binding.tvTotalPendapatanRaisMalang.text = "Total Pendapatan = "+ dbHandlerRais.totalPendapatanMonth()

        binding.tvBulanPantaiMalang.text = dbHandlerPantai.totalMonth()
        binding.tvTotalPengunjungPantaiMalang.text = "Total Pengunjung = "+ dbHandlerPantai.totalPengunjungMonth()
        binding.tvTotalPendapatanPantaiMalang.text = "Total Pendapatan = "+ dbHandlerPantai.totalPendapatanMonth()

    }
}