package com.pavlusha.landmarksapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pavlusha.domain.model.LandmarkSource
import com.pavlusha.landmarksapp.ml.Model
import com.pavlusha.landmarksapp.ui.screens.LandmarkDetailsScreen
import com.pavlusha.landmarksapp.ui.screens.LoginScreen
import com.pavlusha.landmarksapp.ui.screens.MainTabsScreen
import com.pavlusha.landmarksapp.ui.screens.RegisterScreen
import com.pavlusha.landmarksapp.ui.screens.ResetPasswordScreen
import com.pavlusha.landmarksapp.ui.theme.LandmarksAppTheme
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


//import com.google.ai.edge.litert.TensorBuffer

//import com.google.ai.edge.litert.DataType
//import com.google.ai.edge.litert.support.tensorbuffer.TensorBuffer

import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MainActivity : ComponentActivity() {

    private var imageBitmap by mutableStateOf<Bitmap?>(null)
    private var classificationResult by mutableStateOf("")

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let { processImage(it) }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                processImage(bitmap)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error loading image", e)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LandmarksAppTheme {
                Main()
//                LandmarkClassifierScreen(
//                    imageBitmap = imageBitmap,
//                    result = classificationResult,
//                    onTakePictureClick = {
//                        if (hasCameraPermission()) {
//                            cameraLauncher.launch(null)
//                        } else {
//                            requestCameraPermission()
//                        }
//                    },
//                    onOpenGalleryClick = {
//                        galleryLauncher.launch("image/*")
//                    }
//                )
            }
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
    }

    private fun processImage(originalBitmap: Bitmap) {
        classificationResult = "Thinking..."
        imageBitmap = originalBitmap

        val dimension = minOf(originalBitmap.width, originalBitmap.height)
        val thumbnail = ThumbnailUtils.extractThumbnail(originalBitmap, dimension, dimension)
        val scaledBitmap = Bitmap.createScaledBitmap(thumbnail, 224, 224, false)

        classifyImage(scaledBitmap)
    }

    private fun classifyImage(scaledBitmap: Bitmap) {
        try {
            val model = Model.newInstance(this)
            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
            byteBuffer.order(ByteOrder.nativeOrder())
            val intValues = IntArray(224 * 224)
            scaledBitmap.getPixels(
                intValues,
                0,
                scaledBitmap.width,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height
            )

            var pixel = 0
            byteBuffer.rewind()
            for (i in 0 until 224) {
                for (j in 0 until 224) {
                    val value = intValues[pixel++]
                    byteBuffer.putFloat(((value shr 16) and 0xFF).toFloat())
                    byteBuffer.putFloat(((value shr 8) and 0xFF).toFloat())
                    byteBuffer.putFloat((value and 0xFF).toFloat())
                }
            }

            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            val confidences = outputFeature0.floatArray

            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            Log.d("ML", "Max confidence: $maxConfidence at pos: $maxPos")

            val classes = arrayOf(
                "bigben", "church_nemiga", "colosseum", "eiffel", "isaac",
                "island_of_tears", "library", "minsk_gates", "mir", "pisa",
                "sphinx", "taj_mahal", "tower_bridge", "townhall"
            )
            classificationResult = classes[maxPos]

            model.close()
        } catch (e: IOException) {
            Log.e("MainActivity", "Error in model inference", e)
            classificationResult = "Ошибка классификации"
        }
    }
}

@Composable
fun LandmarkClassifierScreen(
    imageBitmap: Bitmap?,
    result: String,
    onTakePictureClick: () -> Unit,
    onOpenGalleryClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Image Preview
        Box(
            modifier = Modifier
                .size(320.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = "Здесь появится фото",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Classified as:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = result.ifBlank { "—" },
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = if (result.isNotBlank())
                Color(0xFFC30000) else MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onTakePictureClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Take Picture",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onOpenGalleryClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Open Gallery",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun Main() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = "auth"
    ) {
        navigation(route = "auth", startDestination = "login") {
            composable("login") { LoginScreen(rootNavController) }
            composable("register") { RegisterScreen(rootNavController) }
            composable("reset_password") { ResetPasswordScreen(rootNavController) }
        }

        composable("main_content") {
            MainTabsScreen(rootNavController)
        }

        composable(
            route = "details_screen/{landmarkId}/{source}",
            arguments = listOf(
                navArgument("landmarkId") { type = NavType.StringType },
                navArgument("source") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val rawId = backStackEntry.arguments?.getString("landmarkId") ?: ""
            val landmarkId = Uri.decode(rawId)
            val sourceString = backStackEntry.arguments?.getString("source") ?: ""
            val source = LandmarkSource.valueOf(sourceString)

            LandmarkDetailsScreen(
                landmarkId = landmarkId,
                source = source,
                onNavigateBack = { rootNavController.popBackStack() }
            )
        }

//        composable(
//            route = "landmark_details/{landmarkId}",
//            arguments = listOf(navArgument("landmarkId") { type = NavType.StringType })
//            ) { backStackEntry ->
//            val id = backStackEntry.arguments?.getString("landmarkId") ?: ""
//            LandmarkDetailsScreen(id, rootNavController)
//        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LandmarksAppTheme {
        Greeting("Android")
    }
}