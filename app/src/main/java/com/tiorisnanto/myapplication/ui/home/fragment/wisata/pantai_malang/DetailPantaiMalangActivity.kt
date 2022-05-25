package com.tiorisnanto.myapplication.ui.home.fragment.wisata.pantai_malang

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.print.PrintHelper
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.tiorisnanto.myapplication.R
import com.tiorisnanto.myapplication.databinding.ActivityDetailPantaiMalangBinding
import kotlinx.android.synthetic.main.activity_details.*
import java.io.ByteArrayOutputStream

class DetailPantaiMalangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPantaiMalangBinding
    private val dbHandler = DBHelperPantaiMalang(this, null)
    lateinit var modifyId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPantaiMalangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        increaseInteger()
        decreaseInteger()

        if (intent.hasExtra("id")) {
            modifyId = intent.getStringExtra("id")!!

            binding.txtTime.setText(intent.getStringExtra("time"))
            binding.txtHour.setText(intent.getStringExtra("hours"))
            binding.date.setText(intent.getStringExtra("date"))
            binding.month.setText(intent.getStringExtra("month"))

            binding.hour.setText(intent.getStringExtra("hour"))
            binding.txtCountTotal.setText(intent.getStringExtra("count"))
            binding.txtHargaTotal.setText(intent.getStringExtra("price"))
            binding.valueDewasa.setText(intent.getStringExtra("adult"))
            binding.valueAnak.setText(intent.getStringExtra("child"))
            binding.txtCountDewasa.setText(intent.getStringExtra("count_adult"))
            binding.txtCountAnak.setText(intent.getStringExtra("count_child"))
            binding.txtHargaDewasa.setText(intent.getStringExtra("adult_price"))
            binding.txtHargaAnak.setText(intent.getStringExtra("child_price"))

            binding.btnPrint.setOnClickListener {
                btnReset.visibility = View.GONE
                btnPrint.visibility = View.GONE

                val text = "Tiket Pantai Malang Valid pada Tanggal "
                val time = binding.txtTime.text.toString()
                val jam = " jam "
                val hour = binding.txtHour.text.toString()
                val text2 = " dan anda pengunjungan ke "
                val idPengunjung = intent.getStringExtra("id")

                val jumlah = binding.txtCountTotal.setText(intent.getStringExtra("count"))
                val combine = text + time + jam + " " + hour + text2 + idPengunjung
                val qrCodeWriter = QRCodeWriter()

//                make a loop for multiply the Qrcode by val jumlah

                try {
                    for (i in 1..jumlah.toString().toInt()) {
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
                        binding.imgQrCode.setImageBitmap(bmp)

//                    doPhotoPrint(byteArray)
//
                        doWebViewPrint()
                    }
                } catch (e: WriterException) {
                    e.printStackTrace()
                }
            }

            binding.btnAdd.visibility = View.GONE
            tilTime.visibility = View.GONE
            txtTime.visibility = View.GONE
            tilDewasa.visibility = View.GONE
            tilAnak.visibility = View.GONE

        } else {
            binding.btnUpdate.visibility = View.GONE
            binding.btnDelete.visibility = View.GONE
            binding.btnPrint.visibility = View.GONE
            binding.btnReset.visibility = View.GONE
            binding.tilTime.visibility = View.GONE
            binding.txtTime.visibility = View.GONE
        }

    }

    private var mWebView: WebView? = null

    private fun doWebViewPrint() {
        // Create a WebView object specifically for printing
        val webView = WebView(this)
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) =
                false

            override fun onPageFinished(view: WebView, url: String) {
                Log.i(TAG, "page finished loading $url")
                val adult = binding.valueDewasa.text.toString()
                val child = binding.valueAnak.text.toString()
                val count = binding.txtCountTotal.text.toString()
                if (adult.isNotEmpty()) {
                    for (i in 1..adult.toInt()) {
                        createWebPrintJob(view)
                    }
                } else if (child.isNotEmpty()) {
                    for (i in 1..child.toInt()) {
                        createWebPrintJob(view)
                    }
                }
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

        val view = findViewById<View>(R.id.linear_detail_pantai_malang) as RelativeLayout
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bm = view.drawingCache
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream)

        val byteArray = stream.toByteArray()
        val printHelper = PrintHelper(this)
        printHelper.scaleMode = PrintHelper.SCALE_MODE_FIT
        val bitmap = printHelper.printBitmap("Tiket Pantai Malang", bm)

        val htmlDocument =
            "<img src='data:image/png;base64, " + Base64.encodeToString(
                byteArray,
                Base64.DEFAULT,
            ) + "'/>"
//


//        val htmlDocument = "<BIG>ahaha<BR>bxabxajbx<BIG>BIG<BR><BIG><BOLD>" +
//                "string <SMALL> text<BR><LEFT>Left aligned<BR><CENTER>" +
//                "Center aligned<BR><UNDERLINE>underline text<BR><QR>1234<BR>" +
//                "<CENTER>QR: 12345678<BR>Line<BR><LINE><BR>Double Line<BR><DLINE><BR><CUT>"
        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null)

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView
    }

    private fun createWebPrintJob(webView: WebView) {
        // Get a PrintManager instance
//        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        (this?.getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->

            val jobName = "${getString(R.string.app_name)} Document"
            val count = binding.txtCountTotal.text.toString()
            // Get a print adapter instance
            val printAdapter = webView.createPrintDocumentAdapter(jobName)
            val printAttributes = PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A3)
//


//                .setResolution(PrintAttributes.Resolution("", "", 600, 600))
//                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build()

            // Create a print job with name and adapter instance
            printManager.print(
                jobName,
                printAdapter,
                printAttributes
            )
        }
    }


    private fun doPhotoPrint(byteArray: ByteArray) {
        val view = findViewById<View>(R.id.rel_print) as RelativeLayout
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bm = view.drawingCache
        val printHelper = PrintHelper(this)

        printHelper.scaleMode = PrintHelper.SCALE_MODE_FIT
        printHelper.printBitmap("Tiket Pantai Malang", bm)

    }

    private fun decreaseInteger() {
        binding.btnDewasaMin.setOnClickListener {
            val hargaDewasa = 10000
            val hargaAnak = 5000
            if (binding.valueDewasa.text.toString().toInt() > 0) {
                binding.valueDewasa.text =
                    (binding.valueDewasa.text.toString().toInt() - 1).toString()
                binding.txtCountDewasa.text = binding.valueDewasa.text.toString()
                binding.txtHargaDewasa.text =
                    (binding.valueDewasa.text.toString().toInt() * hargaDewasa).toString()
                binding.txtCountTotal.text =
                    (binding.valueDewasa.text.toString().toInt() + valueAnak.text.toString()
                        .toInt()).toString()
                binding.txtHargaTotal.text =
                    (binding.valueDewasa.text.toString()
                        .toInt() * hargaDewasa + valueAnak.text.toString()
                        .toInt() * hargaAnak).toString()
            }
        }

        binding.btnAnakMin.setOnClickListener {
            val hargaAnak = 5000
            val hargaDewasa = 10000
            if (binding.valueAnak.text.toString().toInt() > 0) {
                binding.valueAnak.text = (binding.valueAnak.text.toString().toInt() - 1).toString()
                binding.txtCountAnak.text = binding.valueAnak.text.toString()
                binding.txtHargaAnak.text =
                    (binding.valueAnak.text.toString().toInt() * hargaAnak).toString()
                txtCountTotal.text =
                    (binding.valueDewasa.text.toString().toInt() + binding.valueAnak.text.toString()
                        .toInt()).toString()
                binding.txtHargaTotal.text =
                    (binding.valueDewasa.text.toString()
                        .toInt() * hargaDewasa + binding.valueAnak.text.toString()
                        .toInt() * hargaAnak).toString()
            }
        }
    }

    private fun increaseInteger() {
        binding.btnDewasaPlus.setOnClickListener {
            val hargaDewasa = 10000
            val hargaAnak = 5000
            if (binding.valueDewasa.text.toString().toInt() >= 0) {
                binding.valueDewasa.text =
                    (binding.valueDewasa.text.toString().toInt() + 1).toString()
                binding.txtCountDewasa.text = binding.valueDewasa.text.toString()
                binding.txtHargaDewasa.text =
                    (binding.valueDewasa.text.toString().toInt() * hargaDewasa).toString()
                binding.txtCountTotal.text =
                    (binding.valueDewasa.text.toString().toInt() + binding.valueAnak.text.toString()
                        .toInt()).toString()
                binding.txtHargaTotal.text =
                    (binding.valueDewasa.text.toString()
                        .toInt() * hargaDewasa + binding.valueAnak.text.toString()
                        .toInt() * hargaAnak).toString()
            }
        }

        binding.btnAnakPlus.setOnClickListener {
            val hargaAnak = 5000
            val hargaDewasa = 10000
            if (binding.valueAnak.text.toString().toInt() >= 0) {
                binding.valueAnak.text = (binding.valueAnak.text.toString().toInt() + 1).toString()
                binding.txtCountAnak.text = binding.valueAnak.text.toString()
                binding.txtHargaAnak.text =
                    (binding.valueAnak.text.toString().toInt() * hargaAnak).toString()
                binding.txtCountTotal.text =
                    (binding.valueDewasa.text.toString().toInt() + binding.valueAnak.text.toString()
                        .toInt()).toString()
                binding.txtHargaTotal.text =
                    (binding.valueDewasa.text.toString()
                        .toInt() * hargaDewasa + binding.valueAnak.text.toString()
                        .toInt() * hargaAnak).toString()
            }
        }
    }

    fun add(view: View) {
        val date = binding.date.text.toString()
        val month = binding.month.text.toString()
        val hour = binding.hour.text.toString()
        val count = binding.txtCountTotal.text.toString()
        val price = binding.txtHargaTotal.text.toString()
        val adult = binding.valueDewasa.text.toString()
        val child = binding.valueAnak.text.toString()
        val countAdult = binding.txtCountDewasa.text.toString()
        val countChild = binding.txtCountAnak.text.toString()
        val priceAdult = binding.txtHargaDewasa.text.toString()
        val priceChild = binding.txtHargaAnak.text.toString()

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
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        binding.imgQrCode.setImageBitmap(
            BitmapFactory.decodeByteArray(
                byteArray,
                0,
                byteArray.size
            )
        )


        val qrCode = byteArray
        dbHandler.insertRow(
            date,
            month,
            hour,
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
        Toast.makeText(this, "Ticket Pantai Malang Telah ditambahkan !", Toast.LENGTH_LONG).show()
        finish()
    }

    fun update(view: View) {
        val date = binding.date.text.toString()
        val month = binding.month.text.toString()
        val hour = binding.hour.text.toString()
        val count = binding.txtCountTotal.text.toString()
        val price = binding.txtHargaTotal.text.toString()
        val adult = binding.valueDewasa.text.toString()
        val child = binding.valueAnak.text.toString()
        val countAdult = binding.txtCountDewasa.text.toString()
        val countChild = binding.txtCountAnak.text.toString()
        val priceAdult = binding.txtHargaDewasa.text.toString()
        val priceChild = binding.txtHargaAnak.text.toString()

        val text = "Tiket Coban Putri Malang Valid pada Tanggal "
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
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        binding.imgQrCode.setImageBitmap(
            BitmapFactory.decodeByteArray(
                byteArray,
                0,
                byteArray.size
            )
        )

        val qrCode = byteArray

        dbHandler.updateRow(
            modifyId,
            date,
            month,
            hour,
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

    fun delete(view: View) {
        dbHandler.deleteRow(modifyId)
        Toast.makeText(this, "Data Deleted", Toast.LENGTH_SHORT).show()
        finish()
    }

}