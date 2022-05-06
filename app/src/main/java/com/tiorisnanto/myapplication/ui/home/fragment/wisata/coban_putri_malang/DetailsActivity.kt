package com.tiorisnanto.myapplication.ui.home.fragment.wisata.coban_putri_malang

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintJobInfo
import android.print.PrintManager
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.core.view.size
import androidx.print.PrintHelper
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfWriter
import com.tiorisnanto.myapplication.R
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.fragment_view_pager.*
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.text.NumberFormat
import java.util.*


class DetailsActivity : AppCompatActivity() {

    private val dbHandler = DBHelper(this, null)
    lateinit var modifyId: String

    lateinit var dateTex: TextClock
    lateinit var hourText: TextClock
    lateinit var monthText: TextClock
    lateinit var imgCoder: ImageView
    lateinit var btnPrintPDF: Button
    lateinit var btnPlusDewasa: Button
    lateinit var btnMinDewasa: Button
    lateinit var valueDewasa: TextView
    lateinit var textCountDewasa: TextView
    lateinit var textHargaDewasa: TextView
    lateinit var btnPlusAnak: Button
    lateinit var btnMinAnak: Button
    lateinit var valueAnak: TextView
    lateinit var textCountAnak: TextView
    lateinit var textHargaAnak: TextView
    lateinit var textCountTotal: TextView
    lateinit var textHargaTotal: TextView

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

//        nameEditText = findViewById(R.id.name)
//        ageEditText = findViewById(R.id.age)
//        emailEditText = findViewById(R.id.email)
        dateTex = findViewById(R.id.date)
        hourText = findViewById(R.id.hour)
        monthText = findViewById(R.id.month)
        imgCoder = findViewById(R.id.imgQrCode)
        btnPrintPDF = findViewById(R.id.btnPrint)
        btnPlusDewasa = findViewById(R.id.btnDewasaPlus)
        btnMinDewasa = findViewById(R.id.btnDewasaMin)
        valueDewasa = findViewById(R.id.valueDewasa)
        textCountDewasa = findViewById(R.id.txtCountDewasa)
        textHargaDewasa = findViewById(R.id.txtHargaDewasa)
        btnPlusAnak = findViewById(R.id.btnAnakPlus)
        btnMinAnak = findViewById(R.id.btnAnakMin)
        valueAnak = findViewById(R.id.valueAnak)
        textCountAnak = findViewById(R.id.txtCountAnak)
        textHargaAnak = findViewById(R.id.txtHargaAnak)
        textCountTotal = findViewById(R.id.txtCountTotal)
        textHargaTotal = findViewById(R.id.txtHargaTotal)

        increaseInteger()

        decreaseInteger()

        /* Check  if activity opened from List Item Click */
        if (intent.hasExtra("id")) {
            modifyId = intent.getStringExtra("id")!!

            txtTime.setText(intent.getStringExtra("time"))
            txtHour.setText(intent.getStringExtra("hours"))

            dateTex.setText(intent.getStringExtra("date"))
            hourText.setText(intent.getStringExtra("hour"))
            monthText.setText(intent.getStringExtra("month"))
            textCountTotal.setText(intent.getStringExtra("count"))
            textHargaTotal.setText(intent.getStringExtra("price"))
            valueDewasa.setText(intent.getStringExtra("adult"))
            valueAnak.setText(intent.getStringExtra("child"))
            textCountDewasa.setText(intent.getStringExtra("count_adult"))
            textCountAnak.setText(intent.getStringExtra("count_child"))
            textHargaDewasa.setText(intent.getStringExtra("adult_price"))
            textHargaAnak.setText(intent.getStringExtra("child_price"))

            btnPrintPDF.setOnClickListener {

                btnReset.visibility = View.GONE
                btnPrintPDF.visibility = View.GONE

                val text = "Tiket Coban Putri Malang Valid pada Tanggal "
                val time = txtTime.text.toString()
                val jam = " jam "
                val hour = txtHour.text.toString()
                val month = month.text.toString()
                val text2 = " dan anda pengunjungan ke "
                val idPengunjung = intent.getStringExtra("id")

                val combine = text + time + jam + " " + hour + text2 + idPengunjung
                val qrCodeWriter = QRCodeWriter()
                try {
                    val bitMatrix =
                        qrCodeWriter.encode(combine, BarcodeFormat.QR_CODE, 700, 700)
                    val width = bitMatrix.width
                    val height = bitMatrix.height
                    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            bmp.setPixel(
                                x,
                                y,
                                if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
                            )
                        }
                    }

                    val stream = ByteArrayOutputStream()
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val byteArray = stream.toByteArray()
                    val imgOk: ImageView = findViewById(R.id.imgQrCode)
                    imgOk.setImageBitmap(bmp)

                    doWebPrintJob(byteArray)
                    //doPhotoPrint(byteArray)
                } catch (e: WriterException) {
                    e.printStackTrace()
                }


            }

            findViewById<Button>(R.id.btnAdd).visibility = View.GONE
            tilTime.visibility = View.GONE
            txtTime.visibility = View.GONE
            tilDewasa.visibility = View.GONE
            tilAnak.visibility = View.GONE
        } else {
            findViewById<Button>(R.id.btnUpdate).visibility = View.GONE
            findViewById<Button>(R.id.btnDelete).visibility = View.GONE
            findViewById<Button>(R.id.btnPrint).visibility = View.GONE
            findViewById<Button>(R.id.btnReset).visibility = View.GONE
        }


    }

    private var mWebView: WebView? = null

    private fun doWebPrintJob(byteArray: ByteArray) {
        val webView = WebView(this)
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) =
                false

            override fun onPageFinished(view: WebView, url: String) {
                Log.i(ContentValues.TAG, "page finished loading $url")
                createWebPrintJob(view)
                mWebView = null
            }
        }

        // Generate an HTML document on the fly:
        val text = "Tiket Pantai Malang Valid pada Tanggal "
        val time = txtTime.text.toString()
        val jam = " jam "
        val hours = txtHour.text.toString()
        val text2 = " dan anda pengunjungan ke "
        val idPengunjung = intent.getStringExtra("id")
        val combine = text + time + jam + " " + hours + text2 + idPengunjung
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(combine, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }

        val stream = ByteArrayOutputStream()

        val view = findViewById<View>(R.id.activity_details) as RelativeLayout
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bm = view.drawingCache
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        val printHelper = PrintHelper(this)
        printHelper.scaleMode = PrintHelper.SCALE_MODE_FIT
        val bitmap = printHelper.printBitmap("Tiket Coban Putri Malang", bm)

        val htmlDocument =
            Base64.encodeToString(
                byteArray,
                Base64.DEFAULT,
            )

        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null)

        mWebView = webView

    }

    private fun createWebPrintJob(webView: WebView) {
        (this?.getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->

            val jobName = "${getString(R.string.app_name)} Document"

            // Get a print adapter instance
            val printAdapter = webView.createPrintDocumentAdapter(jobName)
            val printAttributes = PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A3)
                .build()

            printManager.print(
                jobName,
                printAdapter,
                printAttributes
            )
        }
    }

//    private fun createBitmapFromView(context: Context, view: View): Bitmap? {
//        val displayMetrics = DisplayMetrics()
//        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
//        view.layoutParams =
//            ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
//        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
//        view.buildDrawingCache()
//        val bitmap =
//            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        view.draw(canvas)
//        return bitmap
//    }

    private fun doPhotoPrint(byteArray: ByteArray) {
        val view = findViewById<View>(R.id.activity_details) as RelativeLayout
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bm = view.drawingCache
        val printHelper = PrintHelper(this)
        printHelper.orientation = 1
        printHelper.scaleMode = PrintHelper.SCALE_MODE_FIT
        printHelper.printBitmap("Tiket", bm)
    }
//


    private fun decreaseInteger() {

        btnMinDewasa.setOnClickListener {
            val hargaDewasa = 10000
            val hargaAnak = 5000
            if (valueDewasa.text.toString().toInt() > 0) {
                valueDewasa.text = (valueDewasa.text.toString().toInt() - 1).toString()

                textCountDewasa.text = valueDewasa.text.toString()

                textHargaDewasa.text =
                    (valueDewasa.text.toString().toInt() * hargaDewasa).toString()

                textCountTotal.text =
                    (valueDewasa.text.toString().toInt() + valueAnak.text.toString()
                        .toInt()).toString()

                textHargaTotal.text =
                    (valueDewasa.text.toString().toInt() * hargaDewasa + valueAnak.text.toString()
                        .toInt() * hargaAnak).toString()

            }
        }

        btnMinAnak.setOnClickListener {
            val hargaAnak = 5000
            val hargaDewasa = 10000
            if (valueAnak.text.toString().toInt() > 0) {

                valueAnak.text = (valueAnak.text.toString().toInt() - 1).toString()

                textCountAnak.text = valueAnak.text.toString()

                textHargaAnak.text = (valueAnak.text.toString().toInt() * hargaAnak).toString()

                textCountTotal.text =
                    (valueDewasa.text.toString().toInt() + valueAnak.text.toString()
                        .toInt()).toString()

                textHargaTotal.text =
                    (valueDewasa.text.toString().toInt() * hargaDewasa + valueAnak.text.toString()
                        .toInt() * hargaAnak).toString()
            }
        }

    }

    private fun increaseInteger() {

        btnPlusDewasa.setOnClickListener {
            val hargaDewasa = 10000
            val hargaAnak = 5000
            if (valueDewasa.text.toString().toInt() >= 0) {
                valueDewasa.text = (valueDewasa.text.toString().toInt() + 1).toString()
                textCountDewasa.text = valueDewasa.text.toString()

                textHargaDewasa.text =
                    (valueDewasa.text.toString().toInt() * hargaDewasa).toString()

                textCountTotal.text =
                    (valueDewasa.text.toString().toInt() + valueAnak.text.toString()
                        .toInt()).toString()

                textHargaTotal.text =
                    (valueDewasa.text.toString().toInt() * hargaDewasa + valueAnak.text.toString()
                        .toInt() * hargaAnak).toString()

            }
        }

        btnPlusAnak.setOnClickListener {
            val hargaAnak = 5000
            val hargaDewasa = 10000
            if (valueAnak.text.toString().toInt() >= 0) {
                valueAnak.text = (valueAnak.text.toString().toInt() + 1).toString()

                textCountAnak.text = valueAnak.text.toString()

                textHargaAnak.text = (valueAnak.text.toString().toInt() * hargaAnak).toString()

                textCountTotal.text =
                    (valueDewasa.text.toString().toInt() + valueAnak.text.toString()
                        .toInt()).toString()

                textHargaTotal.text =
                    (valueDewasa.text.toString().toInt() * hargaDewasa + valueAnak.text.toString()
                        .toInt() * hargaAnak).toString()
            }
        }

    }

    fun add(v: View) {

        val date = dateTex.text.toString()
        val hour = hourText.text.toString()
        val month = monthText.text.toString()
        val count = textCountTotal.text.toString()
        val price = textHargaTotal.text.toString()
        val adult = valueDewasa.text.toString()
        val child = valueAnak.text.toString()
        val countAdult = textCountDewasa.text.toString()
        val countChild = textCountAnak.text.toString()
        val priceAdult = textHargaDewasa.text.toString()
        val priceChild = textHargaAnak.text.toString()


        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(date, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }

        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        imgCoder.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size))

        val qrCode = byteArray

        dbHandler.insertRow(
            date,
            hour,
            month,
            qrCode,
            count,
            price,
            adult,
            child,
            countAdult,
            countChild,
            priceAdult,
            priceChild
        )
        Toast.makeText(this, "Ticket Coban Putri Malang telah ditambahkan", Toast.LENGTH_SHORT)
            .show()
        finish()
    }


    fun update(v: View) {
//        val name = nameEditText.text.toString()
//        val age = ageEditText.text.toString()
//        val email = emailEditText.text.toString()
        val date = dateTex.text.toString()
        val hour = hourText.text.toString()
        val month = monthText.text.toString()
        val count = textCountTotal.text.toString()
        val price = textHargaTotal.text.toString()
        val adult = valueDewasa.text.toString()
        val child = valueAnak.text.toString()
        val countAdult = textCountDewasa.text.toString()
        val countChild = textCountAnak.text.toString()
        val priceAdult = textHargaDewasa.text.toString()
        val priceChild = textHargaAnak.text.toString()

        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(date, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }

        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        imgCoder.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size))


        val qrCode = byteArray

        dbHandler.updateRow(
            modifyId,
            date,
            hour,
            month,
            qrCode,
            count,
            price,
            adult,
            child,
            countAdult,
            countChild,
            priceAdult,
            priceChild
        )
        Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun delete(v: View) {
        dbHandler.deleteRow(modifyId)
        Toast.makeText(this, "Data Deleted", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun formatRupiah(number: Double): String? {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(number)
    }

}