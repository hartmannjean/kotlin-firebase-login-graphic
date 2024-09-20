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
