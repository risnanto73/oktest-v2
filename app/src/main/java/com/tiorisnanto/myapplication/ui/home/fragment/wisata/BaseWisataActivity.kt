package com.tiorisnanto.myapplication.ui.home.fragment.wisata

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.tiorisnanto.myapplication.databinding.ActivityBaseWisataBinding
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_putri_malang.AdminActivity
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_putri_malang.DBHelper
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_rais_malang.AdminRaisMalangActivity
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_rais_malang.DBHelperRaisMalang
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.pantai_malang.AdminPantaiMalangActivity
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.pantai_malang.DBHelperPantaiMalang

class BaseWisataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseWisataBinding
    val dbHandlerPantaiMalang = DBHelperPantaiMalang(this, null)
    val dbHandlerCobanRaisMalang = DBHelperRaisMalang(this, null)
    val dbHandlerCobanPutriMalang = DBHelper(this, null)

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
        binding.txtMonthPendapatanCobanPutriMalang.text = dbHandlerCobanRaisMalang.getPendapatan()
        binding.txtMonthPengunjungCobanPutriMalang.text = dbHandlerCobanRaisMalang.getPengunjung()
        binding.txtMonthCobanPutriMalang.text = dbHandlerCobanRaisMalang.getMonth()
    }
}