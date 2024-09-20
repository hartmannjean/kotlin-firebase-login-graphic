package com.example.engsftwgraph.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.engsftwgraph.model.ProductData
import com.example.engsftwgraph.model.productDataList
import com.example.engsftwgraph.network.logoutUser
import com.example.engsftwgraph.ui.theme.Purple40
import com.example.engsftwgraph.viewModel.HomeViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import com.example.engsftwgraph.R
import com.example.engsftwgraph.model.chartData
import com.example.engsftwgraph.navigation.AppRoutes
import com.example.engsftwgraph.util.formatAccountNumber


@Composable
fun HomeScreen(
    navController: NavController, viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    var selectedScreen by remember { mutableStateOf("chart1") }
    var pointsBalance by remember { mutableIntStateOf(0) }
    var errorMessage by remember { mutableStateOf("") }
    var lineChartEntries by remember { mutableStateOf(emptyList<Entry>()) }

    var userName by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.calculatePointsBalance(
            onBalanceCalculated = { balance ->
                pointsBalance = balance

            },
            onError = { error ->
                errorMessage = error
            }
        )
        viewModel.getUserData(context) { user ->
            userName = user?.name ?: ""
            accountNumber = user?.accountNumber ?: ""
        }
    }

    HomeScaffold(
        context = context,
        navController = navController,
        selectedScreen = selectedScreen,
        onScreenSelected = { screen -> selectedScreen = screen },
        userName = userName,
        accountNumber = accountNumber,
        pointsBalance = pointsBalance,
        errorMessage = errorMessage,
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
    context: Context,
    navController: NavController,
    selectedScreen: String,
    onScreenSelected: (String) -> Unit,
    userName: String,
    accountNumber: String,
    pointsBalance: Int,
    errorMessage: String,
    content: @Composable (PaddingValues) -> Unit
) {
    var isBalanceVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = "Bem Vindo, $userName",
                        color = Purple40
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        logoutUser(context)
                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(AppRoutes.HOME) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {}
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.elevatedCardElevation(4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.wallet),
                            contentDescription = "Conta",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Conta: ${formatAccountNumber(accountNumber)}", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.elevatedCardElevation(4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.balance),
                            contentDescription = "Saldo",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBalanceVisible) "Saldo de Pontos: $pointsBalance" else "Saldo de Pontos: ****",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = { isBalanceVisible = !isBalanceVisible }) {
                            Icon(
                                painter = painterResource(id = if (isBalanceVisible) R.drawable.eye_opened else R.drawable.eye_closed),
                                contentDescription = if (isBalanceVisible) "Ocultar saldo" else "Mostrar saldo",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = Color.Red)
                }

                content(paddingValues)
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.linechart),
                            contentDescription = "Gráfico de linha",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Gráfico de linha") },
                    selected = selectedScreen == "chart1",
                    onClick = { onScreenSelected("chart1") }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.barchart),
                            contentDescription = "Grafico de barra",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Grafico de barra") },
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
        Text(
            text = "Gráfico de movimentação da carteira de pontos.",
            style = MaterialTheme.typography.bodyMedium
        )


        LineChartView(entries = chartData)
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
        Text(
            text = "Gráfico para comparar quantidade de pontos por tipo de produto.",
            style = MaterialTheme.typography.bodyMedium
        )


        BarChartView(productData = productDataList)
    }
}


@Composable
fun LineChartView(entries: List<Entry>) {
    AndroidView(
        factory = { context ->
            val chart = LineChart(context)
            chart.description.isEnabled = false
            chart.setNoDataText("No data available")

            if (entries.isNotEmpty()) {
                val dataSet = LineDataSet(entries, "Movimentação de Pontos")
                dataSet.color = Purple40.toArgb()
                dataSet.valueTextColor = Color.Black.toArgb()

                val data = LineData(dataSet)
                chart.data = data

                val xMax = entries.maxOf { it.x }
                val xMin = entries.minOf { it.x }
                chart.xAxis.axisMinimum = xMin
                chart.xAxis.axisMaximum = xMax
            }

            chart.invalidate()
            chart
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun BarChartView(productData: List<ProductData>) {
    AndroidView(
        factory = { context ->
            val barChart = BarChart(context)

            // Configuração do gráfico
            val entries = productData.mapIndexed { index, product ->
                BarEntry(index.toFloat(), product.points.toFloat())
            }

            val dataSet = BarDataSet(entries, "Quantidade de Pontos")
            dataSet.color = Purple40.toArgb()
            val data = BarData(dataSet)

            barChart.data = data
            barChart.description.isEnabled = false

            barChart.axisLeft.axisMinimum = 0f
            barChart.xAxis.valueFormatter =
                IndexAxisValueFormatter(productData.map { it.productType })
            barChart.xAxis.granularity = 1f
            barChart.invalidate()
            barChart
        },
        modifier = Modifier.fillMaxSize()
    )
}
