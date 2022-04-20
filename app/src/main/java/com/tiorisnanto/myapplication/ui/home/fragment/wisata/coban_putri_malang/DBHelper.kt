package com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_putri_malang

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "CobanPutriMalang.db"
        const val TABLE_NAME = "CobanPutriMalang"
        const val COLUMN_ID = "id"
        const val COLUMN_DATE = "date"
        const val COLUMN_MONTH = "month"
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
                    "($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_DATE TEXT, $COLUMN_MONTH TEXT,$COLUMN_HOUR TEXT,$COLUMN_IMAGE BYTEARRAY, $COLUMN_COUNT TEXT, $COLUMN_PRICE TEXT, $COLUMN_ADULT TEXT, $COLUMN_CHILD TEXT, $COLUMN_COUNT_ADULT TEXT, $COLUMN_COUNT_CHILD TEXT, $COLUMN_ADULT_PRICE TEXT, $COLUMN_CHILD_PRICE TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertRow(
        date: String,
        hour: String,
        month: String,
        image: ByteArray,
        count: String,
        price: String,
        adult: String,
        child: String,
        countAdult: String,
        countChild: String,
        adultPrice: String,
        childPrice: String
    ) {
        val values = ContentValues()
        values.put(COLUMN_DATE, date)
        values.put(COLUMN_MONTH, month)
        values.put(COLUMN_HOUR, hour)
        values.put(COLUMN_IMAGE, image)
        values.put(COLUMN_COUNT, count)
        values.put(COLUMN_PRICE, price)
        values.put(COLUMN_ADULT, adult)
        values.put(COLUMN_CHILD, child)
        values.put(COLUMN_COUNT_ADULT, countAdult)
        values.put(COLUMN_COUNT_CHILD, countChild)
        values.put(COLUMN_ADULT_PRICE, adultPrice)
        values.put(COLUMN_CHILD_PRICE, childPrice)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateRow(
        row_id: String,
        date: String,
        month: String,
        hour: String,
        image: ByteArray,
        count: String,
        price: String,
        adult: String,
        child: String,
        countAdult: String,
        countChild: String,
        adultPrice: String,
        childPrice: String
    ) {
        val values = ContentValues()
        values.put(COLUMN_DATE, date)
        values.put(COLUMN_HOUR, hour)
        values.put(COLUMN_MONTH, hour)
        values.put(COLUMN_IMAGE, image)
        values.put(COLUMN_COUNT, count)
        values.put(COLUMN_PRICE, price)
        values.put(COLUMN_ADULT, adult)
        values.put(COLUMN_CHILD, child)
        values.put(COLUMN_COUNT_ADULT, countAdult)
        values.put(COLUMN_COUNT_CHILD, countChild)
        values.put(COLUMN_ADULT_PRICE, adultPrice)
        values.put(COLUMN_CHILD_PRICE, childPrice)

        val db = this.writableDatabase
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(row_id))
        db.close()
    }

    fun deleteRow(row_id: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(row_id))
        db.close()
    }

    fun getAllRow(): Cursor? {
        val db = this.readableDatabase
        //return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        return db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC", null)
    }

    //DATA UNTUK KE DASHBOARD

    fun totalPendapatan(): String {
        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter
                .ofPattern("yyyy.MM.dd")
                .withZone(ZoneOffset.systemDefault())
                .format(Instant.now())
        } else {
            val date = SimpleDateFormat("yyyy.MM.dd")
            date.format(Date())
        }

        val db = this.readableDatabase
        val cursor: Cursor? =
            db.rawQuery(
                "SELECT SUM($COLUMN_PRICE) FROM $TABLE_NAME WHERE $COLUMN_DATE = '$date'",
                null
            )
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }


    fun totalPengunjung(): String {
        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter
                .ofPattern("yyyy.MM.dd")
                .withZone(ZoneOffset.systemDefault())
                .format(Instant.now())
        } else {
            val date = SimpleDateFormat("yyyy.MM.dd")
            date.format(Date())
        }

        val db = this.readableDatabase
        val cursor: Cursor? =
            db.rawQuery(
                "SELECT SUM($COLUMN_COUNT) FROM $TABLE_NAME WHERE $COLUMN_DATE= '$date'",
                null
            )
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }


    fun totalPengunjungAnak(): String {
        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter
                .ofPattern("yyyy.MM.dd")
                .withZone(ZoneOffset.systemDefault())
                .format(Instant.now())
        } else {
            val date = SimpleDateFormat("yyyy.MM.dd")
            date.format(Date())
        }
        val db = this.readableDatabase
        val cursor: Cursor? =
            db.rawQuery(
                "SELECT SUM($COLUMN_COUNT_CHILD) FROM $TABLE_NAME WHERE $COLUMN_DATE='$date'",
                null
            )
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }


    fun totalPengunjungDewasa(): String {
        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter
                .ofPattern("yyyy.MM.dd")
                .withZone(ZoneOffset.systemDefault())
                .format(Instant.now())
        } else {
            val date = SimpleDateFormat("yyyy.MM.dd")
            date.format(Date())
        }
        val db = this.readableDatabase
        val cursor: Cursor? =
            db.rawQuery(
                "SELECT SUM($COLUMN_COUNT_ADULT) FROM $TABLE_NAME WHERE $COLUMN_DATE ='$date'",
                null
            )
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }

    //DATA UNTUK KE BASE WISATA

    fun totalMonth(): String {
        val month = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter
                .ofPattern("MM")
                .withZone(ZoneOffset.systemDefault())
                .format(Instant.now())
        } else {
            val date = SimpleDateFormat("MM")
            date.format(Date())
        }
        val db = this.readableDatabase
        val cursor: Cursor? =
            db.rawQuery(
                "SELECT SUM($COLUMN_COUNT) FROM $TABLE_NAME WHERE $COLUMN_MONTH ='$month'",
                null
            )
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        //give a condition when val month = 1 its will change to januari
        val monthName = when (month) {
            "01" -> "Januari"
            "02" -> "Februari"
            "03" -> "Maret"
            "04" -> "April"
            "05" -> "Mei"
            "06" -> "Juni"
            "07" -> "Juli"
            "08" -> "Agustus"
            "09" -> "September"
            "10" -> "Oktober"
            "11" -> "November"
            "12" -> "Desember"
            else -> "tidak ada data untuk bulan ini"
        }
        cursor?.close()
        return monthName
    }


    fun totalPengunjungMonth(): String {
        val month = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter
                .ofPattern("MM")
                .withZone(ZoneOffset.systemDefault())
                .format(Instant.now())
        } else {
            val date = SimpleDateFormat("MM")
            date.format(Date())
        }
        val db = this.readableDatabase
        val cursor: Cursor? =
            db.rawQuery(
                "SELECT SUM($COLUMN_COUNT) FROM $TABLE_NAME WHERE $COLUMN_MONTH ='$month'",
                null
            )
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }


    fun totalPendapatanMonth(): String {
        val month = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter
                .ofPattern("MM")
                .withZone(ZoneOffset.systemDefault())
                .format(Instant.now())
        } else {
            val date = SimpleDateFormat("MM")
            date.format(Date())
        }
        val db = this.readableDatabase
        val cursor: Cursor? =
            db.rawQuery(
                "SELECT SUM($COLUMN_PRICE) FROM $TABLE_NAME WHERE $COLUMN_MONTH ='$month'",
                null
            )
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }

//    //get pendapatan per bulan
//
//    fun getPendapatanMonth(): String {
//        val month = DateTimeFormatter
//            .ofPattern("MM")
//            .withZone(ZoneOffset.systemDefault())
//            .format(Instant.now())
//
//        val db = this.writableDatabase
//        val cursor = db.rawQuery(
//            "SELECT SUM(${DBHelper.COLUMN_PRICE}) FROM ${DBHelper.TABLE_NAME} WHERE ${DBHelper.COLUMN_MONTH} = '$month'",
//            null
//        )
//        cursor?.moveToFirst()
//        val total = cursor?.getInt(0)
//        val monthName = when (month) {
//            "01" -> "Januari"
//            "02" -> "Februari"
//            "03" -> "Maret"
//            "04" -> "April"
//            "05" -> "Mei"
//            "06" -> "Juni"
//            "07" -> "Juli"
//            "08" -> "Agustus"
//            "09" -> "September"
//            "10" -> "Oktober"
//            "11" -> "November"
//            "12" -> "Desember"
//            else -> "tidak ada data untuk bulan ini"
//        }
//        cursor?.close()
//        return " Pendapatan dari bulan " + monthName + "sejumlah" + total.toString()
//        cursor?.close()
//        return total.toString()
//    }
//
//    //get pengunjung by month
//
//    fun getPengunjungMonth(): String {
//
//        val month = DateTimeFormatter
//            .ofPattern("MM")
//            .withZone(ZoneOffset.systemDefault())
//            .format(Instant.now())
//
//        val db = this.writableDatabase
//        val cursor = db.rawQuery(
//            "SELECT SUM(${DBHelper.COLUMN_COUNT}) FROM ${DBHelper.TABLE_NAME} WHERE ${DBHelper.COLUMN_MONTH} = '$month'",
//            null
//        )
//        cursor?.moveToFirst()
//        val total = cursor?.getInt(0)
//        val monthName = when (month) {
//            "01" -> "Januari"
//            "02" -> "Februari"
//            "03" -> "Maret"
//            "04" -> "April"
//            "05" -> "Mei"
//            "06" -> "Juni"
//            "07" -> "Juli"
//            "08" -> "Agustus"
//            "09" -> "September"
//            "10" -> "Oktober"
//            "11" -> "November"
//            "12" -> "Desember"
//            else -> "tidak ada data untuk bulan ini"
//        }
//        cursor?.close()
//        return total.toString() + " pengunjung dari bulan " + monthName
//        cursor?.close()
//        return total.toString()
//    }




}