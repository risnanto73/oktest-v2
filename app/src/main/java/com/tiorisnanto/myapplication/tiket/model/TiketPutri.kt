package com.tiorisnanto.myapplication.tiket.model

data class TiketPutri(
    val id: String? = null,
    val year: String? = null,
    val month: String? = null,
    val day: String? = null,
    val hour: String? = null,
    val image: String? = null,
    val adult: String? = null,
    val child: String? = null,
    val countAdult: String? = null,
    val countChild: String? = null,
    val priceAdult: String? = null,
    val priceChild: String? = null,
    val count: String? = null,
    val qrCode: ByteArray
)