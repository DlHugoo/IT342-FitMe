package com.example.myapplication.component



import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomLineChart(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var points = listOf<Float>()  // weight values
    private val paintLine = Paint().apply {
        color = Color.BLUE
        strokeWidth = 6f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }
    private val paintCircle = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    fun setData(weights: List<Float>) {
        points = weights
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (points.size < 2) return

        val spacing = width.toFloat() / (points.size - 1)
        val maxWeight = points.maxOrNull() ?: 0f
        val minWeight = points.minOrNull() ?: 0f
        val range = (maxWeight - minWeight).takeIf { it > 0 } ?: 1f

        val pathPoints = points.mapIndexed { index, weight ->
            val x = spacing * index
            val normalized = (weight - minWeight) / range
            val y = height - (normalized * height)
            x to y
        }

        for (i in 0 until pathPoints.size - 1) {
            val (x1, y1) = pathPoints[i]
            val (x2, y2) = pathPoints[i + 1]
            canvas.drawLine(x1, y1, x2, y2, paintLine)
        }

        for ((x, y) in pathPoints) {
            canvas.drawCircle(x, y, 10f, paintCircle)
        }
    }
}
