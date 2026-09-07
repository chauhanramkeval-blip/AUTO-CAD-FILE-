package com.example

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DataSaverOff
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.Loupe
import androidx.compose.material.icons.filled.Polyline
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Square
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class CadCategory(val title: String, val icon: ImageVector) {
    ANNOTATION("Annotation", Icons.Filled.Chat),
    DRAW("Draw", Icons.Filled.Create),
    EDIT("Edit", Icons.Filled.Square),
    LAYER("Layer", Icons.Filled.Layers),
    MEASURE("Measure", Icons.Filled.Straighten),
    DIMENSION("Dimension", Icons.Filled.LinearScale)
}

enum class CadTool(val title: String, val icon: ImageVector, val category: CadCategory) {
    ARC_MEASURE("Arc", Icons.Filled.DonutLarge, CadCategory.MEASURE),
    LINEAR_DIST("Linear", Icons.Filled.Timeline, CadCategory.MEASURE),
    FREEHAND("Freehand", Icons.Filled.Draw, CadCategory.MEASURE),
    RADIUS("Radius", Icons.Filled.RadioButtonUnchecked, CadCategory.MEASURE),
    LOUPE("Loupe", Icons.Filled.Loupe, CadCategory.MEASURE),
    ANGULAR("Angular", Icons.Filled.DataSaverOff, CadCategory.MEASURE),
    POLYLINE("Polyline", Icons.Filled.Polyline, CadCategory.MEASURE),
    
    // Sample tools for other categories to demonstrate state switching
    DRAW_LINE("Line", Icons.Filled.Timeline, CadCategory.DRAW),
    DRAW_RECT("Rectangle", Icons.Filled.Square, CadCategory.DRAW),
    
    DIM_LINEAR("Dim Linear", Icons.Filled.LinearScale, CadCategory.DIMENSION),
    DIM_ALIGNED("Dim Aligned", Icons.Filled.Architecture, CadCategory.DIMENSION)
}

val DarkCharcoal = Color(0xFF1E2124)
val DarkGrey = Color(0xFF2E3236)
val CyanAccent = Color(0xFF38BDF8)
val LightGrey = Color(0xFFA0A0A0)

@Composable
fun DualTierToolbar(
    activeCategory: CadCategory,
    onCategorySelected: (CadCategory) -> Unit,
    activeTool: CadTool?,
    onToolSelected: (CadTool) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Floating Contextual Sub-Toolbar
        AnimatedContent(
            targetState = activeCategory,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            label = "SubToolbarAnimation"
        ) { category ->
            val tools = CadTool.entries.filter { it.category == category }
            if (tools.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    tools.forEach { tool ->
                        val isSelected = tool == activeTool
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(DarkGrey)
                                .border(
                                    width = if (isSelected) 2.dp else 0.dp,
                                    color = if (isSelected) CyanAccent else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { onToolSelected(tool) }
                        ) {
                            Icon(
                                imageVector = tool.icon,
                                contentDescription = tool.title,
                                tint = if (isSelected) CyanAccent else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            } else {
                // Spacer prevents layout jumping when selecting an empty category
                Spacer(modifier = Modifier.height(66.dp))
            }
        }

        // Primary Bottom Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkCharcoal)
                .padding(vertical = 12.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CadCategory.entries.forEach { category ->
                val isSelected = category == activeCategory
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onCategorySelected(category) }
                        .padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.title,
                        tint = if (isSelected) CyanAccent else LightGrey,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = category.title,
                        fontSize = 11.sp,
                        color = if (isSelected) CyanAccent else LightGrey
                    )
                }
            }
        }
    }
}
