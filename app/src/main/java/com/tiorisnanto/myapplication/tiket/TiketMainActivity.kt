package com.tiorisnanto.myapplication.tiket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tiorisnanto.myapplication.R
import com.tiorisnanto.myapplication.databinding.ActivityTiketMainBinding
import com.tiorisnanto.myapplication.tiket.coban_putri_malang.DashboardCobanActivity

class TiketMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTiketMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTiketMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Menu Wisata"

        binding.btnCobanPutriMalang.setOnClickListener{
            startActivity(Intent(this,DashboardCobanActivity::class.java))
        }
    }
}