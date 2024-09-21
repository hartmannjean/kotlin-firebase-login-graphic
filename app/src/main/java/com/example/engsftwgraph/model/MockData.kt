package com.example.engsftwgraph.model

import com.github.mikephil.charting.data.Entry

data class ProductData(
    val productType: String,
    val points: Int
)

val chartData = listOf(
    Entry(1f, 300f),
    Entry(2f, 350f),
    Entry(3f, 400f),
    Entry(4f, 450f),
    Entry(5f, 300f),
    Entry(6f, 200f),
    Entry(7f, 400f),
    Entry(8f, 500f),
    Entry(9f, 450f),
    Entry(10f, 600f)
)

val productDataList = listOf(
    ProductData("Produto A", 120),
    ProductData("Produto B", 80),
    ProductData("Produto C", 150),
    ProductData("Produto D", 60),
    ProductData("Produto E", 200)
)

data class ProductMonthData(
    val month: String,
    val productPoints: Map<String, Float>
)

val productMonthData = listOf(
    ProductMonthData("Jan", mapOf(
        "Produto A" to 120f,
        "Produto B" to 80f,
        "Produto C" to 150f,
        "Produto D" to 60f,
        "Produto E" to 200f
    )),
    ProductMonthData("Fev", mapOf(
        "Produto A" to 140f,
        "Produto B" to 70f,
        "Produto C" to 160f,
        "Produto D" to 90f,
        "Produto E" to 210f
    )),
    ProductMonthData("Mar", mapOf(
        "Produto A" to 130f,
        "Produto B" to 75f,
        "Produto C" to 170f,
        "Produto D" to 80f,
        "Produto E" to 220f
    )),
    ProductMonthData("Abr", mapOf(
        "Produto A" to 160f,
        "Produto B" to 90f,
        "Produto C" to 180f,
        "Produto D" to 70f,
        "Produto E" to 230f
    )),
    ProductMonthData("Mai", mapOf(
        "Produto A" to 180f,
        "Produto B" to 100f,
        "Produto C" to 190f,
        "Produto D" to 60f,
        "Produto E" to 240f
    ))
)


