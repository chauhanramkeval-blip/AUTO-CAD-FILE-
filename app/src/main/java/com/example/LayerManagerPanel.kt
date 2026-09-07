package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LayerManagerPanel(
    layers: List<CadLayer>,
    onToggleLayer: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        color = DarkGrey,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = "Layer Properties Manager",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Divider(color = DarkCharcoal, thickness = 1.dp)
            
            LazyColumn {
                items(layers) { layer ->
                    LayerRow(
                        layer = layer,
                        onToggle = { isVisible -> onToggleLayer(layer.id, isVisible) }
                    )
                }
            }
        }
    }
}

@Composable
fun LayerRow(
    layer: CadLayer,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!layer.isVisible) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Visibility Icon
        IconButton(
            onClick = { onToggle(!layer.isVisible) },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = if (layer.isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                contentDescription = if (layer.isVisible) "Hide Layer" else "Show Layer",
                tint = if (layer.isVisible) CyanAccent else LightGrey
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Layer Color Swatch
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(layer.color)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Layer Name
        Text(
            text = layer.name,
            color = if (layer.isVisible) Color.White else LightGrey,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
    }
}
