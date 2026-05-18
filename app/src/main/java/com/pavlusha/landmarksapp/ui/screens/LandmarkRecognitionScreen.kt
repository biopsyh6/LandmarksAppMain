package com.pavlusha.landmarksapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.ar.core.ArCoreApk
import com.pavlusha.landmarksapp.ui.screens.recognition.ARModelViewer
import com.pavlusha.landmarksapp.ui.screens.recognition.CameraScanner
import com.pavlusha.landmarksapp.ui.state.RecognitionMode
import com.pavlusha.landmarksapp.ui.viewmodel.RecognitionViewModel
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberModelInstance
import io.github.sceneview.rememberModelLoader
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandmarkRecognitionScreen(
    onNavigateBack: () -> Unit,
    viewModel: RecognitionViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    var isArSupported by remember { mutableStateOf<Boolean?>(null) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
            if (!granted) {
                Toast.makeText(context, "Для распознавания и AR нужна камера", Toast.LENGTH_LONG)
                    .show()
                onNavigateBack()
            }
        }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }

        val arCore = ArCoreApk.getInstance()
        var availability = arCore.checkAvailability(context)

        while (availability.isUnknown) {
            delay(200)
            availability = arCore.checkAvailability(context)
        }

        isArSupported = availability.isSupported
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (state.mode) {
                            RecognitionMode.AR_VIEW -> "AR Режим"
                            RecognitionMode.SIMPLE_3D_VIEW -> "3D Просмотр"
                            else -> "Наведите на объект"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                },
                actions = {
                    if (state.mode == RecognitionMode.AR_VIEW || state.mode == RecognitionMode.SIMPLE_3D_VIEW) {
                        IconButton(onClick = { viewModel.setMode(RecognitionMode.SCANNING) }) {
                            Icon(Icons.Default.Close, "Закрыть")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (hasCameraPermission) {
                Crossfade(targetState = state.mode) { mode ->
                    when (mode) {
                        RecognitionMode.SCANNING, RecognitionMode.FOUND_UI -> {
                            CameraScanner(
                                onFrameCaptured = { imageProxy ->
                                    viewModel.analyzeFrame(imageProxy)
                                }
                            )
                        }

                        RecognitionMode.AR_VIEW -> {
                            val modelUrl = state.recognizedLandmark?.remoteModel3dPath
                                ?: state.recognizedLandmark?.currentModel3dPath

                            if (!modelUrl.isNullOrBlank()) {
                                ARModelViewer(modelUrl = modelUrl)
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Для этого объекта нет 3D модели", color = Color.White)
                                }
                            }
                        }

                        RecognitionMode.SIMPLE_3D_VIEW -> {
                            val modelUrl = state.recognizedLandmark?.remoteModel3dPath
                                ?: state.recognizedLandmark?.currentModel3dPath

                            if (!modelUrl.isNullOrBlank()) {
                                Simple3DViewer(modelUrl = modelUrl)
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Для этого объекта нет 3D модели", color = Color.White)
                                }
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black))
            }

            if (state.mode == RecognitionMode.SCANNING) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.Center)
                        .background(Color.Transparent, shape = RoundedCornerShape(16.dp))
                ) {
                    // Здесь можно нарисовать квадрат из 4 уголков (Canvas)
                    Text(
                        "Сканирование...",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }

            if (state.mode == RecognitionMode.FOUND_UI && state.recognizedLandmark != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                        .align(Alignment.BottomCenter),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.recognizedLandmark!!.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.recognizedLandmark!!.shortDescription,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (isArSupported != false) {
                            Button(
                                onClick = { viewModel.setMode(RecognitionMode.AR_VIEW) },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = isArSupported == true
                            ) {
                                Text(if (isArSupported == null) "Проверка AR..." else "Посмотреть в AR")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        OutlinedButton(
                            onClick = { viewModel.setMode(RecognitionMode.SIMPLE_3D_VIEW) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Посмотреть 3D модель")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Simple3DViewer(modelUrl: String) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)
    val modelInstance = rememberModelInstance(modelLoader = modelLoader, fileLocation = modelUrl)

    val cameraNode = rememberCameraNode(engine).apply {
        position = Position(x = 0.0f, y = 0.0f, z = 3.0f)
    }

    SceneView(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        engine = engine,
        modelLoader = modelLoader,
        environmentLoader = environmentLoader,
        cameraNode = cameraNode
    ) {
        modelInstance?.let { instance ->
            ModelNode(
                modelInstance = instance,
                scaleToUnits = 0.6f,
                autoAnimate = true
            )
        }
    }
}