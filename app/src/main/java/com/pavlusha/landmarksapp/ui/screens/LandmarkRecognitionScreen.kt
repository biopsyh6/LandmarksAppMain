package com.pavlusha.landmarksapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.ar.core.ArCoreApk
import com.pavlusha.landmarksapp.R
import com.pavlusha.landmarksapp.ui.screens.common.formatDistance
import com.pavlusha.landmarksapp.ui.screens.recognition.ARModelViewer
import com.pavlusha.landmarksapp.ui.screens.recognition.CameraScanner
import com.pavlusha.landmarksapp.ui.state.RecognitionMode
import com.pavlusha.landmarksapp.ui.viewmodel.RecognitionViewModel
import com.yandex.mapkit.MapKitFactory
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberModelInstance
import io.github.sceneview.rememberModelLoader
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel


fun findSurfaceView(view: View): SurfaceView? {
    if (view is SurfaceView) return view
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val found = findSurfaceView(view.getChildAt(i))
            if (found != null) return found
        }
    }
    return null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandmarkRecognitionScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetails: (String, String) -> Unit,
    viewModel: RecognitionViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val localView = LocalView.current

    val lifecycleOwner = LocalLifecycleOwner.current

    var isCapturing by remember { mutableStateOf(false) }

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

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> MapKitFactory.getInstance().onStart()
                Lifecycle.Event.ON_STOP -> MapKitFactory.getInstance().onStop()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
                        IconButton(
                            onClick = { viewModel.setMode(RecognitionMode.SCANNING) }
                        ) {
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

                            Box(modifier = Modifier.fillMaxSize()) {
                                if (!modelUrl.isNullOrBlank()) {
                                    ARModelViewer(
                                        modelUrl = modelUrl,
                                        annotations = state.arContent?.textAnnotations
                                            ?: emptyList(),
                                        modelScale = state.arContent?.modelScale ?: 0.5f,
                                        heightOffset = state.arContent?.heightOffset ?: 0f,
                                        rotationDegrees = state.arContent?.rotationDegrees ?: 0f
                                    )
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

                                if (state.recognizedLandmark != null && !isCapturing) {
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                                            .align(Alignment.TopCenter),
                                        shape = RoundedCornerShape(16.dp),
                                        color = Color(0xB3000000),
                                        shadowElevation = 6.dp
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(
                                                horizontal = 20.dp,
                                                vertical = 14.dp
                                            ),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = state.recognizedLandmark!!.name,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(16.dp))
                                            Text(
                                                text = formatDistance(state.distanceMeters),
                                                color = colorResource(id = R.color.white),
                                                fontWeight = FontWeight.Black,
                                                fontSize = 18.sp
                                            )
                                        }
                                    }
                                }

                                if (state.navigationBearing != null && !isCapturing) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(bottom = 120.dp),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        Surface(
                                            modifier = Modifier.size(70.dp),
                                            shape = CircleShape,
                                            color = Color.Black.copy(alpha = 0.5f)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.outline_arrow_upward_24),
                                                    contentDescription = "Направление",
                                                    tint = colorResource(id = R.color.red),
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .graphicsLayer(
                                                            rotationZ = state.navigationBearing!!
                                                        )
                                                )
                                            }
                                        }
                                    }
                                }

                                if (!modelUrl.isNullOrBlank()) {

                                    FloatingActionButton(
                                        onClick = {
                                            if (isCapturing) return@FloatingActionButton
                                            isCapturing = true

                                            val surfaceView = findSurfaceView(localView.rootView)

                                            if (surfaceView != null) {
                                                val bitmap = Bitmap.createBitmap(
                                                    surfaceView.width,
                                                    surfaceView.height,
                                                    Bitmap.Config.ARGB_8888
                                                )

                                                PixelCopy.request(
                                                    surfaceView,
                                                    bitmap,
                                                    { copyResult ->
                                                        isCapturing = false
                                                        if (copyResult == PixelCopy.SUCCESS) {
                                                            state.recognizedLandmark?.id?.let { id ->
                                                                viewModel.saveARPhoto(
                                                                    id,
                                                                    bitmap
                                                                ) { isSuccess ->
                                                                    if (isSuccess) {
                                                                        Toast.makeText(
                                                                            context,
                                                                            "Фото сохранено в AR Галерею!",
                                                                            Toast.LENGTH_SHORT
                                                                        ).show()
                                                                    } else {
                                                                        Toast.makeText(
                                                                            context,
                                                                            "Ошибка при сохранении",
                                                                            Toast.LENGTH_SHORT
                                                                        ).show()
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            Toast.makeText(
                                                                context,
                                                                "Ошибка захвата AR сцены",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    },
                                                    Handler(Looper.getMainLooper())
                                                )
                                            } else {
                                                isCapturing = false
                                                Toast.makeText(
                                                    context,
                                                    "AR слой не найден",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        },
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(bottom = 32.dp),
                                        containerColor = Color.White,
                                        contentColor = Color.Black
                                    ) {
                                        if (isCapturing) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp),
                                                color = Color.Black,
                                                strokeWidth = 2.dp
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.PhotoCamera,
                                                contentDescription = "Сделать снимок",
                                                modifier = Modifier.size(28.dp)
                                            )
                                        }
                                    }
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                )
            }

            if (state.mode == RecognitionMode.SCANNING) {
                val infiniteTransition = rememberInfiniteTransition(label = "scanner_transition")
                val alpha by infiniteTransition.animateFloat(
                    initialValue = 0.3f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scanner_alpha"
                )

                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.Center)
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val cornerLength = 32.dp.toPx()
                        val strokeWidth = 4.dp.toPx()
                        val color = Color.White.copy(alpha = alpha)

                        // Левый верхний угол
                        drawLine(
                            color,
                            Offset(0f, 0f),
                            Offset(cornerLength, 0f),
                            strokeWidth,
                            StrokeCap.Round
                        )
                        drawLine(
                            color,
                            Offset(0f, 0f),
                            Offset(0f, cornerLength),
                            strokeWidth,
                            StrokeCap.Round
                        )

                        // Правый верхний угол
                        drawLine(
                            color,
                            Offset(size.width, 0f),
                            Offset(size.width - cornerLength, 0f),
                            strokeWidth,
                            StrokeCap.Round
                        )
                        drawLine(
                            color,
                            Offset(size.width, 0f),
                            Offset(size.width, cornerLength),
                            strokeWidth,
                            StrokeCap.Round
                        )

                        // Левый нижний угол
                        drawLine(
                            color,
                            Offset(0f, size.height),
                            Offset(cornerLength, size.height),
                            strokeWidth,
                            StrokeCap.Round
                        )
                        drawLine(
                            color,
                            Offset(0f, size.height),
                            Offset(0f, size.height - cornerLength),
                            strokeWidth,
                            StrokeCap.Round
                        )

                        // Правый нижний угол
                        drawLine(
                            color,
                            Offset(size.width, size.height),
                            Offset(size.width - cornerLength, size.height),
                            strokeWidth,
                            StrokeCap.Round
                        )
                        drawLine(
                            color,
                            Offset(size.width, size.height),
                            Offset(size.width, size.height - cornerLength),
                            strokeWidth,
                            StrokeCap.Round
                        )
                    }

                    Text(
                        text = "Поиск объекта...",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 40.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
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

                        TextButton(
                            onClick = {
                                onNavigateToDetails(
                                    state.recognizedLandmark!!.id,
                                    state.recognizedLandmark!!.source.name
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Подробнее об объекте")
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