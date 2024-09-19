package com.example.engsftwgraph.view

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.engsftwgraph.network.logoutUser
import com.example.engsftwgraph.util.UserPreferences
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

@Composable
fun HomeScreen(navController: NavController) {
    var selectedScreen by remember { mutableStateOf("chart1") }

    HomeScaffold(
        navController = navController,
        selectedScreen = selectedScreen,
        onScreenSelected = { screen ->
            selectedScreen = screen
        }
    ) { paddingValues ->
        when (selectedScreen) {
            "chart1" -> Chart1Content(Modifier.padding(paddingValues))
            "chart2" -> Chart2Content(Modifier.padding(paddingValues))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    navController: NavController,
    selectedScreen: String,
    onScreenSelected: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val context = LocalContext.current
    val userModel = UserPreferences.getUserData(context)
    Log.d(TAG, "HomeScaffold: $userModel")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Bem Vindo! ${userModel?.name}") },
                actions = {
                    IconButton(onClick = {
                        logoutUser(context)
                        navController.navigate("login") {
                            popUpTo("home") {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            content(paddingValues)
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Gráfico 1") },
                    label = { Text("Gráfico 1") },
                    selected = selectedScreen == "chart1",
                    onClick = { onScreenSelected("chart1") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Gráfico 2") },
                    label = { Text("Gráfico 2") },
                    selected = selectedScreen == "chart2",
                    onClick = { onScreenSelected("chart2") }
                )
            }
        }
    )
}

@Composable
fun Chart1Content(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Gráfico 1", style = MaterialTheme.typography.bodyMedium)
        RadarChartView()
    }
}

@Composable
fun Chart2Content(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Gráfico 2", style = MaterialTheme.typography.bodyMedium)
        LineChartView()
    }
}

//Gráfico de linha
@Composable
fun LineChartView() {
    AndroidView(
        factory = { context ->
            val chart = LineChart(context)
            chart.description.isEnabled = false
            chart.setNoDataText("No data available")

            val values = listOf(
                Entry(1f, 2f),
                Entry(2f, 4f),
                Entry(3f, 6f),
                Entry(4f, 8f),
                Entry(5f, 10f)
            )

            val dataSet = LineDataSet(values, "Sample Data")
            dataSet.color = Color.Blue.toArgb()
            dataSet.valueTextColor = Color.Black.toArgb()

            val data = LineData(dataSet as ILineDataSet)
            chart.data = data

            chart
        },
        modifier = Modifier.fillMaxSize()
    )
}

//Gráfico de radar
@Composable
fun RadarChartView() {
    AndroidView(
        factory = { context ->
            val chart = RadarChart(context)
            chart.description.isEnabled = false
            chart.isRotationEnabled = false
            chart.setWebLineWidth(1f)
            chart.setWebColor(Color.Black.toArgb())
            chart.setWebAlpha(100)

            val entries = listOf(
                RadarEntry(5f),
                RadarEntry(6f),
                RadarEntry(7f),
                RadarEntry(8f),
                RadarEntry(9f)
            )

            val dataSet = RadarDataSet(entries, "Sample Data")
            dataSet.color = Color.Blue.toArgb()
            dataSet.valueTextColor = Color.Black.toArgb()
            dataSet.valueTextSize = 16f

            val radarData = RadarData(dataSet)
            chart.data = radarData

            chart
        },
        modifier = Modifier.fillMaxSize()
    )
}