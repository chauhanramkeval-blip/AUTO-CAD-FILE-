package com.example

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun CADViewerScreen(layers: List<CadLayer> = emptyList()) {
    // Transformation state for pan and zoom
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.1f, 50f)
                    offset += pan
                }
            }
    ) {
        // Apply the transformation matrix to the canvas
        withTransform({
            translate(offset.x, offset.y)
            scale(scale, scale)
        }) {
            // 1. Draw Grid (Infinite feel based on view bounds, but simplified here)
            val gridSize = 100f
            val gridColor = Color.White.copy(alpha = 0.1f)
            val rows = (size.height / gridSize).toInt() * 5
            val cols = (size.width / gridSize).toInt() * 5
            
            // Just draw some static grid lines for context
            for (i in -cols..cols) {
                val x = i * gridSize
                drawLine(
                    color = gridColor,
                    start = Offset(x, -size.height * 5),
                    end = Offset(x, size.height * 5),
                    strokeWidth = 1f / scale // Keep grid lines visually thin regardless of zoom
                )
            }
            for (i in -rows..rows) {
                val y = i * gridSize
                drawLine(
                    color = gridColor,
                    start = Offset(-size.width * 5, y),
                    end = Offset(size.width * 5, y),
                    strokeWidth = 1f / scale
                )
            }

            // 2. Draw Mock CAD Entities (e.g. a floor plan outline)
            val isWallsVisible = layers.find { it.id == "walls" }?.isVisible != false
            val isDoorsVisible = layers.find { it.id == "doors" }?.isVisible != false

            if (isWallsVisible) {
                val cadLineColor = layers.find { it.id == "walls" }?.color ?: Color(0xFF00FFCC) // Cyan fallback
                val cadStroke = Stroke(width = 2f / scale)

                // Outline of a house
                drawRect(
                    color = cadLineColor,
                    topLeft = Offset(200f, 200f),
                    size = Size(400f, 300f),
                    style = cadStroke
                )
                // Inner walls
                drawLine(
                    color = cadLineColor,
                    start = Offset(400f, 200f),
                    end = Offset(400f, 400f),
                    strokeWidth = 2f / scale
                )
                drawLine(
                    color = cadLineColor,
                    start = Offset(200f, 350f),
                    end = Offset(300f, 350f),
                    strokeWidth = 2f / scale
                )
            }

            if (isDoorsVisible) {
                // Door swing arc (mock)
                drawArc(
                    color = layers.find { it.id == "doors" }?.color ?: Color.Yellow,
                    startAngle = 180f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(250f, 300f),
                    size = Size(100f, 100f),
                    style = Stroke(width = 2f / scale)
                )
            }
        }
    }
}
