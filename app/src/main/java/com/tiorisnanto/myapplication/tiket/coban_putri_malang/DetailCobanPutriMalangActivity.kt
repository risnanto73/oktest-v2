package com.tiorisnanto.myapplication.tiket.coban_putri_malang

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.tiorisnanto.myapplication.databinding.ActivityDetailCobanPutriMalangBinding
import com.tiorisnanto.myapplication.tiket.model.TiketPutri
import kotlinx.android.synthetic.main.activity_details.*
import java.io.ByteArrayOutputStream

class DetailCobanPutriMalangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailCobanPutriMalangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCobanPutriMalangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.title = "Detail Coban Putri Malang"

        increaseInteger()
        decreaseInteger()

    }

    private fun decreaseInteger() {

        binding.btnDewasaMin.setOnClickListener {
            val hargaDewasa = 10000
            val hargaAnak = 5000
            if (valueDewasa.text.toString().toInt() > 0) {
                valueDewasa.text = (valueDewasa.text.toString().toInt() - 1).toString()
                binding
                    .txtCountDewasa.text = valueDewasa.text.toString()
                binding.txtHargaDewasa.text =
                    (valueDewasa.text.toString().toInt() * hargaDewasa).toString()
                binding.txtCountTotal.text =
                    (valueDewasa.text.toString().toInt() + valueAnak.text.toString()
                        .toInt()).toString()
                binding.txtHargaTotal.text =
                    (valueDewasa.text.toString().toInt() * hargaDewasa + valueAnak.text.toString()
                        .toInt() * hargaAnak).toString()
            }
        }

        binding.btnAnakMin.setOnClickListener {
            val hargaAnak = 5000
            val hargaDewasa = 10000
            if (valueAnak.text.toString().toInt() > 0) {
                valueAnak.text = (valueAnak.text.toString().toInt() - 1).toString()
                binding.txtCountAnak.text = valueAnak.text.toString()
                binding.txtHargaAnak.text =
                    (valueAnak.text.toString().toInt() * hargaAnak).toString()
                txtCountTotal.text =
                    (valueDewasa.text.toString().toInt() + valueAnak.text.toString()
                        .toInt()).toString()
                binding.txtHargaTotal.text =
                    (valueDewasa.text.toString().toInt() * hargaDewasa + valueAnak.text.toString()
                        .toInt() * hargaAnak).toString()
            }
        }

    }

    private fun increaseInteger() {

        binding.btnDewasaPlus.setOnClickListener {
            val hargaDewasa = 10000
            val hargaAnak = 5000
            if (valueDewasa.text.toString().toInt() >= 0) {
                valueDewasa.text = (valueDewasa.text.toString().toInt() + 1).toString()
                binding
                    .txtCountDewasa.text = valueDewasa.text.toString()
                binding.txtHargaDewasa.text =
                    (valueDewasa.text.toString().toInt() * hargaDewasa).toString()
                binding.txtCountTotal.text =
                    (valueDewasa.text.toString().toInt() + valueAnak.text.toString()
                        .toInt()).toString()
                binding.txtHargaTotal.text =
                    (valueDewasa.text.toString().toInt() * hargaDewasa + valueAnak.text.toString()
                        .toInt() * hargaAnak).toString()
            }
        }

        binding.btnAnakPlus.setOnClickListener {
            val hargaAnak = 5000
            val hargaDewasa = 10000
            if (valueAnak.text.toString().toInt() >= 0) {
                valueAnak.text = (valueAnak.text.toString().toInt() + 1).toString()
                binding.txtCountAnak.text = valueAnak.text.toString()
                binding.txtHargaAnak.text =
                    (valueAnak.text.toString().toInt() * hargaAnak).toString()
                binding.txtCountTotal.text =
                    (valueDewasa.text.toString().toInt() + valueAnak.text.toString()
                        .toInt()).toString()
                binding.txtHargaTotal.text =
                    (valueDewasa.text.toString().toInt() * hargaDewasa + valueAnak.text.toString()
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

        val text = "Tiket Coban Rais Malang Valid pada Tanggal"
        val year = binding.txtYear.text.toString()
        val time = binding.txtTime.text.toString()
        val jam = " jam "
        val hours = binding.txtHour.text.toString()
        val text2 = " dan anda pengunjungan ke "
        val idPengunjung = intent.getStringExtra("id")

        val combine =
            text + time + jam + " " + hours + text2 + idPengunjung + " dengan total pembayaran sebesar Rp." + price + "."
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

        val ref = FirebaseDatabase.getInstance().getReference("pemesanan")
        val pesanId = ref.push().key

        val data = TiketPutri(
            pesanId.toString(),
            date,
            month,
            year,
            hour,
            count,
            price,
            adult,
            child,
            countAdult,
            countChild,
            priceAdult,
            priceChild,
            qrCode
        )

        if (pesanId != null) {
            ref.child(pesanId.toString()).setValue(data)
                .addOnSuccessListener {
                    Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CobanPutriMalangActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                }
        }


    }


    fun update(view: View) {}
    fun delete(view: View) {}
}