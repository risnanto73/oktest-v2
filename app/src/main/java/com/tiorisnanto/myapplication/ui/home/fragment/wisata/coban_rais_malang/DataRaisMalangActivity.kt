package com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_rais_malang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.tiorisnanto.myapplication.R
import com.tiorisnanto.myapplication.databinding.ActivityDataRaisMalangBinding
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_putri_malang.CustomAdapter
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_putri_malang.DBHelper
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_putri_malang.DetailsActivity

class DataRaisMalangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataRaisMalangBinding
    val dbHandler = DBHelperRaisMalang(this, null)
    var dataList = ArrayList<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataRaisMalangBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun fabClicked(view: View) {
        startActivity(Intent(this, DetailRaisMalangActivity::class.java))
    }

    override fun onResume() {
        super.onResume()
        loadIntoList()
    }

    private fun loadIntoList() {
        dataList.clear()
        val cursor = dbHandler.getAllRow()
        cursor?.moveToFirst()

        while (cursor != null && !cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(DBHelperRaisMalang.COLUMN_ID))

            map["date"] = cursor.getString(cursor.getColumnIndex(DBHelperRaisMalang.COLUMN_DATE))
            map["hour"] = cursor.getString(cursor.getColumnIndex(DBHelperRaisMalang.COLUMN_HOUR))
            map["count"] = cursor.getString(cursor.getColumnIndex(DBHelperRaisMalang.COLUMN_COUNT))
            map["price"] = cursor.getString(cursor.getColumnIndex(DBHelperRaisMalang.COLUMN_PRICE))
            map["adult"] = cursor.getString(cursor.getColumnIndex(DBHelperRaisMalang.COLUMN_ADULT))
            map["child"] = cursor.getString(cursor.getColumnIndex(DBHelperRaisMalang.COLUMN_CHILD))
            map["count_adult"] =
                cursor.getString(cursor.getColumnIndex(DBHelperRaisMalang.COLUMN_COUNT_ADULT))
            map["count_child"] =
                cursor.getString(cursor.getColumnIndex(DBHelperRaisMalang.COLUMN_COUNT_CHILD))
            map["adult_price"] =
                cursor.getString(cursor.getColumnIndex(DBHelperRaisMalang.COLUMN_ADULT_PRICE))
            map["child_price"] =
                cursor.getString(cursor.getColumnIndex(DBHelperRaisMalang.COLUMN_CHILD_PRICE))

            dataList.add(map)

            cursor.moveToNext()
        }
        findViewById<ListView>(R.id.listView).adapter =
            CustomAdapter(this@DataRaisMalangActivity, dataList)
        findViewById<ListView>(R.id.listView).setOnItemClickListener { _, _, i, _ ->
            val intent = Intent(this, DetailRaisMalangActivity::class.java)
            intent.putExtra("id", dataList[+i]["id"])
            intent.putExtra("time", dataList[+i]["date"])
            intent.putExtra("hours", dataList[+i]["hour"])
            intent.putExtra("hour", dataList[+i]["hour"])
            intent.putExtra("count", dataList[+i]["count"])
            intent.putExtra("price", dataList[+i]["price"])
            intent.putExtra("adult", dataList[+i]["adult"])
            intent.putExtra("child", dataList[+i]["child"])
            intent.putExtra("count_adult", dataList[+i]["count_adult"])
            intent.putExtra("count_child", dataList[+i]["count_child"])
            intent.putExtra("adult_price", dataList[+i]["adult_price"])
            intent.putExtra("child_price", dataList[+i]["child_price"])

            startActivity(intent)
        }
    }
}