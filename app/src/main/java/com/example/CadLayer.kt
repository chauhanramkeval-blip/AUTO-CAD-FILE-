package com.example

import androidx.compose.ui.graphics.Color

data class CadLayer(
    val id: String,
    val name: String,
    val color: Color,
    val isVisible: Boolean = true
)

// Sample layers typical for CAD
val DefaultLayers = listOf(
    CadLayer("0", "0 (Default)", Color.White),
    CadLayer("walls", "Walls", Color(0xFF00FFCC)),
    CadLayer("doors", "Doors", Color.Yellow),
    CadLayer("windows", "Windows", Color.Cyan),
    CadLayer("dimensions", "Dimensions", Color(0xFFFF5555)),
    CadLayer("furniture", "Furniture", Color(0xFFAA55FF))
)
