package com.pavlusha.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ThumbnailUtils
import android.util.Log
import com.pavlusha.data.ml.Model
import com.pavlusha.domain.model.RecognitionResultDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MLRecognitionDataSource(
    private val context: Context
) {
    private var model: Model? = null

    private val classes = arrayOf(
        "bigben", "church_nemiga", "colosseum", "eiffel", "isaac",
        "island_of_tears", "library", "minsk_gates", "mir", "pisa",
        "sphinx", "taj_mahal", "tower_bridge", "townhall"
    )

    init {
        try {
            model = Model.newInstance(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun recognizeImage(
        imageBytes: ByteArray,
        rotationDegrees: Int
    ): RecognitionResultDomainModel? = withContext(Dispatchers.Default) {
        try {
            if (model == null) throw Exception("Model is not initialized")

            val originalBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                ?: throw Exception("Could not decode image bytes")

            val rotatedBitmap = if (rotationDegrees != 0) {
                val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                Bitmap.createBitmap(
                    originalBitmap, 0, 0,
                    originalBitmap.width, originalBitmap.height,
                    matrix, true
                )
            } else {
                originalBitmap
            }

            val dimension = minOf(rotatedBitmap.width, rotatedBitmap.height)
            val thumbnail = ThumbnailUtils.extractThumbnail(rotatedBitmap, dimension, dimension)
            val scaledBitmap = Bitmap.createScaledBitmap(thumbnail, 224, 224, false)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
            byteBuffer.order(ByteOrder.nativeOrder())
            val intValues = IntArray(224 * 224)

            scaledBitmap.getPixels(intValues, 0, 224, 0, 0, 224, 224)

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

            val outputs = model!!.process(inputFeature0)
            val logits = outputs.outputFeature0AsTensorBuffer.floatArray
//            val confidences = outputs.outputFeature0AsTensorBuffer.floatArray
            var maxLogit = Float.NEGATIVE_INFINITY
            for (logit in logits) {
                if (logit > maxLogit) maxLogit = logit
            }

            var sumExp = 0f
            val probabilities = FloatArray(logits.size)
            for (i in logits.indices) {
                val exp = kotlin.math.exp((logits[i] - maxLogit).toDouble()).toFloat()
                probabilities[i] = exp
                sumExp += exp
            }

            for (i in probabilities.indices) {
                probabilities[i] = probabilities[i] / sumExp
            }

            var maxPos = 0
            var maxConfidence = 0f
            for (i in probabilities.indices) {
                if (probabilities[i] > maxConfidence) {
                    maxConfidence = probabilities[i]
                    maxPos = i
                }
            }

            Log.d("ML_RECOGNITION", "Recognized: ${classes[maxPos]} with TRUE confidence: $maxConfidence (Logit: ${logits[maxPos]})")

            if (maxConfidence < 0.85f) {
                return@withContext null
            }

            return@withContext RecognitionResultDomainModel(
                objectClassId = maxPos.toString(),
                landmarkId = null,
                className = classes[maxPos],
                confidence = maxConfidence
            )

        } catch (e: Exception) {
            Log.e("ML_RECOGNITION", "Exception in recognizeImage", e)
            throw e
        }
    }

    suspend fun loadNewModel(modelPath: String) = withContext(Dispatchers.IO) {

    }

    fun close() {
        model?.close()
    }
}