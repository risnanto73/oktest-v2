package com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_rais_malang

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_putri_malang.DBHelper
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DBHelperRaisMalang(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "RaisMalang.db"
        const val TABLE_NAME = "RaisMalang"

        const val COLUMN_ID = "id"

        const val COLUMN_DATE = "date"
        const val COLUMN_HOUR = "hour"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_COUNT = "count"
        const val COLUMN_PRICE = "price"
        const val COLUMN_ADULT = "adult"
        const val COLUMN_CHILD = "child"
        const val COLUMN_COUNT_ADULT = "count_adult"
        const val COLUMN_COUNT_CHILD = "count_child"
        const val COLUMN_ADULT_PRICE = "adult_price"
        const val COLUMN_CHILD_PRICE = "child_price"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_NAME " +
                    "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_DATE TEXT,$COLUMN_HOUR TEXT, $COLUMN_IMAGE TEXT, $COLUMN_COUNT TEXT, $COLUMN_PRICE TEXT, $COLUMN_ADULT TEXT, $COLUMN_CHILD TEXT, $COLUMN_COUNT_ADULT TEXT, $COLUMN_COUNT_CHILD TEXT, $COLUMN_ADULT_PRICE TEXT, $COLUMN_CHILD_PRICE TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertRow(
        date: String,
        hour: String,
        image: ByteArray,
        count: String,
        price: String,
        adult: String,
        child: String,
        count_adult: String,
        count_child: String,
        adult_price: String,
        child_price: String
    ) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_DATE, date)
        contentValues.put(COLUMN_HOUR, hour)
        contentValues.put(COLUMN_IMAGE, image)
        contentValues.put(COLUMN_COUNT, count)
        contentValues.put(COLUMN_PRICE, price)
        contentValues.put(COLUMN_ADULT, adult)
        contentValues.put(COLUMN_CHILD, child)
        contentValues.put(COLUMN_COUNT_ADULT, count_adult)
        contentValues.put(COLUMN_COUNT_CHILD, count_child)
        contentValues.put(COLUMN_ADULT_PRICE, adult_price)
        contentValues.put(COLUMN_CHILD_PRICE, child_price)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    fun updateRow(
        id: String,
        date: String,
        hour: String,
        image: ByteArray,
        count: String,
        price: String,
        adult: String,
        child: String,
        count_adult: String,
        count_child: String,
        adult_price: String,
        child_price: String
    ) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_DATE, date)
        contentValues.put(COLUMN_HOUR, hour)
        contentValues.put(COLUMN_IMAGE, image)
        contentValues.put(COLUMN_COUNT, count)
        contentValues.put(COLUMN_PRICE, price)
        contentValues.put(COLUMN_ADULT, adult)
        contentValues.put(COLUMN_CHILD, child)
        contentValues.put(COLUMN_COUNT_ADULT, count_adult)
        contentValues.put(COLUMN_COUNT_CHILD, count_child)
        contentValues.put(COLUMN_ADULT_PRICE, adult_price)
        contentValues.put(COLUMN_CHILD_PRICE, child_price)
        db.update(TABLE_NAME, contentValues, "$COLUMN_ID = ?", arrayOf(id))
        db.close()
    }

    fun deleteRow(id: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id))
        db.close()
    }

    fun getAllRow(): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC", null)
    }

    //get total pendapatan per hari
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTotalPendapatan(): String {
        val date = DateTimeFormatter
            .ofPattern("yyyy.MM.dd")
            .withZone(ZoneOffset.systemDefault())
            .format(Instant.now())

        val db = this.writableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM($COLUMN_PRICE) FROM $TABLE_NAME WHERE $COLUMN_DATE = '$date'",
            null
        )
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun totalPengunjung(): String {
        val date = DateTimeFormatter
            .ofPattern("yyyy.MM.dd")
            .withZone(ZoneOffset.systemDefault())
            .format(Instant.now())

        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM($COLUMN_COUNT) FROM $TABLE_NAME WHERE $COLUMN_DATE = '$date'",
            null
        )
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun totalPengunjungAnak(): String {
        val date = DateTimeFormatter
            .ofPattern("yyyy.MM.dd")
            .withZone(ZoneOffset.systemDefault())
            .format(Instant.now())
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM($COLUMN_COUNT_CHILD) FROM $TABLE_NAME WHERE $COLUMN_DATE = '$date'",
            null
        )
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun totalPengunjungDewasa(): String {
        val date = DateTimeFormatter
            .ofPattern("yyyy.MM.dd")
            .withZone(ZoneOffset.systemDefault())
            .format(Instant.now())
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM($COLUMN_COUNT_ADULT) FROM $TABLE_NAME WHERE $COLUMN_DATE = '$date'",
            null
        )
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }

    //get pendapatan per hari
    @RequiresApi(Build.VERSION_CODES.O)
    fun getPendapatan(): String {
//        val date = DateTimeFormatter
//            .ofPattern("MM")
//            .withZone(ZoneOffset.systemDefault())
//            .format(Instant.now())

        val db = this.writableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM($COLUMN_PRICE) FROM $TABLE_NAME WHERE $COLUMN_DATE",
            null
        )
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }

    //get pengunjung by date
    @RequiresApi(Build.VERSION_CODES.O)
    fun getPengunjung(): String {


        val db = this.writableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM($COLUMN_COUNT) FROM $TABLE_NAME WHERE $COLUMN_DATE",
            null
        )
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }

    //get month by date
    @RequiresApi(Build.VERSION_CODES.O)
    fun getMonth(): String {
//        val date = DateTimeFormatter
//            .ofPattern("yyyy.MM.dd")
//            .withZone(ZoneOffset.systemDefault())
//            .format(Instant.now())

        val db = this.writableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM($COLUMN_COUNT) FROM $TABLE_NAME WHERE $COLUMN_DATE ",
            null
        )
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }


}