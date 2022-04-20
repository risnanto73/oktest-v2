package com.tiorisnanto.myapplication.tiket.coban_putri_malang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tiorisnanto.myapplication.R
import com.tiorisnanto.myapplication.databinding.ActivityDashboardCobanBinding

class DashboardCobanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardCobanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardCobanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.title = "Dashboard Coban Putri Malang"

        binding.btnAddNote.setOnClickListener {
            startActivity(Intent(this, CobanPutriMalangActivity::class.java))
        }
    }
}