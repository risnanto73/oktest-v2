package com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_rais_malang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tiorisnanto.myapplication.databinding.ActivityRaisMalangBinding

class AdminRaisMalangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRaisMalangBinding
    val dbHandler = DBHelperRaisMalang(this, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRaisMalangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoToAdd.setOnClickListener {
            startActivity(Intent(this, DataRaisMalangActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        toDoList()
    }

    private fun toDoList() {
        binding.tvTotalPendapatanHariIni.text = "Rp. " + dbHandler.getTotalPendapatan()

        binding.tvTotalPengunjungHariIni.text = dbHandler.totalPengunjung()+" Orang"

        binding.tvTotalPengunjungAnakHariIni.text = dbHandler.totalPengunjungAnak() + " Orang"

        binding.tvTotalPengunjungDewasaHariIni.text = dbHandler.totalPengunjungDewasa() + " Orang"
    }
}