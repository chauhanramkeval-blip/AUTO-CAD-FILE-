package com.example

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CadViewerApp()
            }
        }
    }
}

@Composable
fun CadViewerApp() {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    
    var activeCategory by remember { mutableStateOf(CadCategory.MEASURE) }
    var activeTool by remember { mutableStateOf<CadTool?>(null) }
    
    // Layer management state
    var layers by remember { mutableStateOf(DefaultLayers) }
    var isLayerManagerVisible by remember { mutableStateOf(false) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            selectedFileUri = it
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Opened: ${it.lastPathSegment ?: "Unknown File"}")
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            DualTierToolbar(
                activeCategory = activeCategory,
                onCategorySelected = { 
                    activeCategory = it 
                    isLayerManagerVisible = it == CadCategory.LAYER
                },
                activeTool = activeTool,
                onToolSelected = { 
                    activeTool = it
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Tool Selected: ${it.title}")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    // application/acad is a common fallback, but often standard file pickers
                    // rely on extensions for CAD files if they aren't registered with specific mime types.
                    // We can use a combination of known CAD mime types and broad matching
                    // to ensure they show up in the picker.
                    filePickerLauncher.launch(
                        arrayOf(
                            "application/acad",
                            "application/x-autocad",
                            "application/dxf",
                            "image/vnd.dwg",
                            "image/vnd.dxf",
                            "*/*" // Fallback to allow any file if the system doesn't map CAD types well
                        )
                    ) 
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(Icons.Filled.FolderOpen, contentDescription = "Open CAD File")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF1E1E1E)) // Dark theme background for CAD viewer
        ) {
            if (selectedFileUri == null) {
                Text(
                    text = "Tap the folder icon to open a DWG/DXF file.",
                    color = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            // Boilerplate for CAD Viewer Canvas with Pan and Zoom Gestures
            CADViewerScreen(layers = layers)
            
            // Layer Manager Panel Overlay
            if (isLayerManagerVisible) {
                LayerManagerPanel(
                    layers = layers,
                    onToggleLayer = { layerId, isVisible ->
                        layers = layers.map { 
                            if (it.id == layerId) it.copy(isVisible = isVisible) else it 
                        }
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
    }
}
