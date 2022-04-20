package com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_putri_malang

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.tiorisnanto.myapplication.R

class DataActivity : AppCompatActivity() {

    val dbHandler = DBHelper(this, null)
    var dataList = ArrayList<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)
    }

    fun fabClicked(view: View) {
        val intent = Intent(this, DetailsActivity::class.java)
        startActivity(intent)
    }

    public override fun onResume() {
        super.onResume()
        loadIntoList()
    }

    fun loadIntoList() {
        dataList.clear()
        val cursor = dbHandler.getAllRow()
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))

            map["date"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DATE))
            map["hour"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_HOUR))
            map["count"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_COUNT))
            map["price"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PRICE))
            map["adult"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ADULT))
            map["child"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CHILD))
            map["count_adult"] =
                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_COUNT_ADULT))
            map["count_child"] =
                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_COUNT_CHILD))
            map["adult_price"] =
                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ADULT_PRICE))
            map["child_price"] =
                cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CHILD_PRICE))

            dataList.add(map)

            cursor.moveToNext()
        }

        findViewById<ListView>(R.id.listView).adapter =
            CustomAdapter(this@DataActivity, dataList)
        findViewById<ListView>(R.id.listView).setOnItemClickListener { _, _, i, _ ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("id", dataList[+i]["id"])
            intent.putExtra("time", dataList[+i]["date"])
            intent.putExtra("hours", dataList[+i]["hour"])
            intent.putExtra("hour", dataList[+i]["hour"])
            intent.putExtra("months", dataList[+i]["month"])
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