package com.tiorisnanto.myapplication.ui.home.fragment.note

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_NAME " +
                    "($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_DATE TEXT,$COLUMN_IMAGE BYTEARRAY, $COLUMN_COUNT TEXT, $COLUMN_PRICE TEXT, $COLUMN_ADULT TEXT, $COLUMN_CHILD TEXT, $COLUMN_COUNT_ADULT TEXT, $COLUMN_COUNT_CHILD TEXT, $COLUMN_ADULT_PRICE TEXT, $COLUMN_CHILD_PRICE TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertRow(date: String, image: ByteArray, count: String, price: String, adult: String, child: String, countAdult: String, countChild: String, adultPrice: String, childPrice: String) {
        val values = ContentValues()
//        values.put(COLUMN_NAME, name)
//        values.put(COLUMN_AGE, age)
//        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_DATE, date)
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
//        name: String,
//        age: String,
//        email: String,
        date: String,
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
//        values.put(COLUMN_NAME, name)
//        values.put(COLUMN_AGE, age)
//        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_DATE, date)
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



    fun totalPendapatan(): String {
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT SUM($COLUMN_PRICE) FROM $TABLE_NAME WHERE $COLUMN_DATE", null)
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }


    fun totalPengunjung(): String? {
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT SUM($COLUMN_COUNT) FROM $TABLE_NAME WHERE $COLUMN_DATE", null)
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }

    fun totalPengunjungAnak(): String {
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT SUM($COLUMN_COUNT_CHILD) FROM $TABLE_NAME WHERE $COLUMN_DATE", null)
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }

    fun totalPengunjungDewasa(): String? {
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT SUM($COLUMN_COUNT_ADULT) FROM $TABLE_NAME WHERE $COLUMN_DATE", null)
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }

    fun totalPendapatan1(dateFormat: Date): Any? {
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT SUM($COLUMN_PRICE) FROM $TABLE_NAME WHERE $COLUMN_DATE = '$dateFormat'", null)
        cursor?.moveToFirst()
        val total = cursor?.getInt(0)
        cursor?.close()
        return total.toString()
    }


    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "myDBfile.db"
        const val TABLE_NAME = "users"

        const val COLUMN_ID = "id"
//        const val COLUMN_NAME = "name"
//        const val COLUMN_AGE = "age"
//        const val COLUMN_EMAIL = "email"
        const val COLUMN_DATE = "date"
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


}