package com.tiorisnanto.myapplication.tiket.coban_putri_malang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tiorisnanto.myapplication.R
import com.tiorisnanto.myapplication.databinding.ActivityCobanPutriMalangBinding

class CobanPutriMalangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCobanPutriMalangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCobanPutriMalangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Daftar Tiket Coban Putri Malang"
    }

    fun fabClicked(view: View) {
        startActivity(Intent(this, DetailCobanPutriMalangActivity::class.java))
    }
}