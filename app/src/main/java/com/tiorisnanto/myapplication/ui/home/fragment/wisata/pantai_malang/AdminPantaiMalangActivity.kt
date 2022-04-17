package com.tiorisnanto.myapplication.ui.home.fragment.wisata.pantai_malang

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tiorisnanto.myapplication.databinding.ActivityPantaiMalangBinding

class AdminPantaiMalangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPantaiMalangBinding
    val dbHandler = DBHelperPantaiMalang(this, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantaiMalangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoToAdd.setOnClickListener {
            startActivity(Intent(this, DataPantaiMalangActivity::class.java))
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