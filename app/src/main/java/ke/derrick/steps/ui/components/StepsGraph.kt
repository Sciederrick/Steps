package ke.derrick.steps.ui.components

import android.graphics.Paint
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import ke.derrick.steps.R
import ke.derrick.steps.ui.theme.Blue800
import ke.derrick.steps.utils.convertToTwoDigitNumberString

/**
 * Created by Saurabh
 */
@Composable
fun StepsGraph(
    modifier : Modifier,
    xValues: List<Int>,
    yValues: List<Int>,
    points: List<Float>,
    midpoint: Int = 3,
    verticalStep: Int
) {
    val mContext = LocalContext.current
    val controlPoints1 = mutableListOf<PointF>()
    val controlPoints2 = mutableListOf<PointF>()
    val coordinates = mutableListOf<PointF>()
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            isFakeBoldText = true
            color = ContextCompat.getColor(mContext, R.color.gray_600)
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }
    val textPaintOnFocus = remember(density) {
        Paint().apply {
            isFakeBoldText = true
            color = ContextCompat.getColor(mContext, R.color.blue_800)
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .fillMaxSize()
            .graphicsLayer { alpha = 0.99F }
            .drawWithContent {
                val colors = listOf(Color.Transparent, Color.White)
                drawContent()
                drawRect(
                    brush = Brush.horizontalGradient(startX = 0f, endX = (size.width/3f),
                        colors = colors),
                    blendMode = BlendMode.DstIn
                )
                drawRect(
                    brush = Brush.horizontalGradient(startX = size.width,
                        endX = size.width - (size.width/3f), colors = colors),
                    blendMode = BlendMode.DstIn
                )
            },
        contentAlignment = Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
         ) {
            val xAxisSpace = size.width / (xValues.size - 1)
            val yAxisSpace = size.height / yValues.size
            /** placing x axis labels **/
            for (i in xValues.indices) {
                drawContext.canvas.nativeCanvas.drawText(
                    convertToTwoDigitNumberString(xValues[i]),
                    xAxisSpace * i,
                    size.height - 30,
                    if (i == midpoint) textPaintOnFocus else textPaint
                )
            }

            /** placing our x axis points */
            for (i in points.indices) {
                val x1 = xAxisSpace * (xValues[i] - 1)
                val y1 = size.height - (yAxisSpace * (points[i]/verticalStep.toFloat()))
                coordinates.add(PointF(x1,y1))
            }
            /** calculating the connection points */
            for (i in 1 until coordinates.size) {
                controlPoints1.add(PointF((coordinates[i].x + coordinates[i - 1].x) / 2, coordinates[i - 1].y))
                controlPoints2.add(PointF((coordinates[i].x + coordinates[i - 1].x) / 2, coordinates[i].y))
            }
            /** drawing the path */
            val stroke = Path().apply {
                reset()
                moveTo(coordinates.first().x, coordinates.first().y)
                for (i in 0 until coordinates.size - 1) {
                    cubicTo(
                        controlPoints1[i].x,controlPoints1[i].y,
                        controlPoints2[i].x,controlPoints2[i].y,
                        coordinates[i + 1].x,coordinates[i + 1].y
                    )
                }
            }

            drawPath(
                stroke,
                color = Blue800,
                style = Stroke(
                    width = 10f,
                    cap = StrokeCap.Round
                )
            )

            /** drawing a circle for one point of focus **/
            // TODO: use a dynamic index to draw a hollow circle on the focused coordinate
            drawCircle(
                color = Blue800,
                radius = 30f,
                center = Offset(coordinates[midpoint].x, coordinates[midpoint].y)
            )
            drawCircle(
                color = Color.White,
                radius = 18f,
                center = Offset(coordinates[midpoint].x, coordinates[midpoint].y)
            )
        }
    }
}