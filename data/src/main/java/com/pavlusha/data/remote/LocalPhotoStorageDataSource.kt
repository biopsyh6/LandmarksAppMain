package com.pavlusha.data.remote

import android.content.Context
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class LocalPhotoStorageDataSource(
    private val context: Context
) {
    suspend fun savePhoto(imageBytes: ByteArray): String = withContext(Dispatchers.IO) {
        val filename = "AR_Landmark_${System.currentTimeMillis()}.jpg"

        val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(directory, filename)

        val fos = FileOutputStream(file)
        fos.use {
            it.write(imageBytes)
            it.flush()
        }

        return@withContext file.absolutePath
    }
}