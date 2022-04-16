package com.tiorisnanto.myapplication.ui.home.fragment.wisata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tiorisnanto.myapplication.databinding.ActivityBaseWisataBinding
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_putri_malang.AdminActivity
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_rais_malang.AdminRaisMalangActivity
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.pantai_malang.AdminPantaiMalangActivity

class BaseWisataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseWisataBinding

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
}