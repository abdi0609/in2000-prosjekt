package com.example.stromkalkulator.ui.components

import android.graphics.Typeface
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stromkalkulator.R
import com.example.stromkalkulator.ui.theme.rememberChartStyle
import com.example.stromkalkulator.viewmodels.HomeViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.endAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.edges.rememberFadingEdges
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.legend.verticalLegend
import com.patrykandpatrick.vico.compose.legend.verticalLegendItem
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.*
import com.patrykandpatrick.vico.core.entry.composed.plus



private const val COLOR_1_CODE = 0xffb983ff
private const val COLOR_2_CODE = 0xff91b1fd

private val color1 = Color(COLOR_1_CODE)
private val color2 = Color(COLOR_2_CODE)
private val chartColors = listOf(color1, color2)

private val legendItemLabelTextSize = 12.sp
private val legendItemIconSize = 8.dp
private val legendItemIconPaddingValue = 10.dp
private val legendItemSpacing = 4.dp
private val legendTopPaddingValue = 8.dp
private val legendPadding = dimensionsOf(top = legendTopPaddingValue)

@Composable
fun PriceTemperatureGraph(viewModel: HomeViewModel) {
    val state = viewModel.homeStateFlow.collectAsState()
    val chartEntryModel1 = ChartEntryModelProducer(state.value.presentablePrices)
    val chartEntryModel2 = ChartEntryModelProducer(state.value.presentableTemperatures)
    val composedModel = chartEntryModel1 + chartEntryModel2

    Surface(
        Modifier
            .padding(25.dp)
            .fillMaxSize()
    ) {
        ProvideChartStyle(rememberChartStyle(listOf(color1), listOf(color2))) {
            val priceChart = columnChart(
                mergeMode = ColumnChart.MergeMode.Stack,
                targetVerticalAxisPosition = AxisPosition.Vertical.Start,
            )
            val tempChart = lineChart(
                targetVerticalAxisPosition = AxisPosition.Vertical.End,

                )
            Chart(
                chart = remember(priceChart, tempChart) {priceChart + tempChart},
                chartModelProducer = composedModel,
                startAxis = startAxis(
                    guideline = null,
                    horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
                    labelRotationDegrees = 20F,
                    maxLabelCount = 5,
                    valueFormatter = { value, _ ->
                        "${"%.2f".format(value)}kr"
                    }
                ),
                bottomAxis = bottomAxis(
                    labelRotationDegrees = 0F,
                ),
                endAxis = endAxis(
                    guideline = null,
                    horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
                    labelRotationDegrees = 20F,
                    maxLabelCount = 5,
                    valueFormatter = { value, _ ->
                        "${"%.1f".format(value)}°"
                    }
                ),
                legend = rememberLegend(),
                fadingEdges = rememberFadingEdges()
            )
        }
    }
}

@Composable
private fun rememberLegend() = verticalLegend(
    items = chartColors.mapIndexed { index, chartColor ->
        verticalLegendItem(
            icon = shapeComponent(Shapes.pillShape, chartColor),
            label = textComponent(
                color = currentChartStyle.axis.axisLabelColor,
                textSize = legendItemLabelTextSize,
                typeface = Typeface.MONOSPACE,
            ),
            labelText =
                if (index==1) stringResource(R.string.temperature_chart_label_text)
                else stringResource(R.string.price_chart_label_text),
        )
    },
    iconSize = legendItemIconSize,
    iconPadding = legendItemIconPaddingValue,
    spacing = legendItemSpacing,
    padding = legendPadding,
)

@Composable
@Preview
fun GraphTestScreen() {
    Scaffold(Modifier.fillMaxSize()) {
        Column(
            Modifier.padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

        ) {
            //PriceTemperatureGraph(listOf(), listOf())
        }
    }
}
