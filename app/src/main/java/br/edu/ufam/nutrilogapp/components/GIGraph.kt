package br.edu.ufam.nutrilogapp.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.edu.ufam.nutrilogapp.data.model.GIDataPoint
import br.edu.ufam.nutrilogapp.ui.theme.PrimaryBlue
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun GIGraph(
    dataPoints: List<GIDataPoint>,
    modifier: Modifier = Modifier
) {
    val entries = dataPoints.mapIndexed { index, point ->
        index.toFloat() to point.value
    }

    val chartEntryModel = entryModelOf(*entries.toTypedArray())

    Chart(
        chart = lineChart(
            lines = listOf(
                lineSpec(
                    lineColor = Color.White,
                    lineThickness = 2.dp
                )
            ),
            spacing = 48.dp,
//            axis = PrimaryBlue
        ),
        model = chartEntryModel,
        startAxis = rememberStartAxis(
            tickLength = 8.dp,
            axis = null,
            tick = null,
            guideline = null
        ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = AxisValueFormatter { value, _ ->
                dataPoints.getOrNull(value.toInt())?.time ?: ""
            },
            tickLength = 8.dp,
            axis = null,
            tick = null,
            guideline = null
        ),
        modifier = modifier,
    )
}

