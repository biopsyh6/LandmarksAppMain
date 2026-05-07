package com.pavlusha.data.remote

import com.pavlusha.data.model.remote.WikipediaSummaryRemoteModel
import com.pavlusha.data.remote.api.IWikipediaApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WikipediaDataSource(
    private val apiService: IWikipediaApi
) {
    suspend fun getArticleSummary(searchQuery: String): WikipediaSummaryRemoteModel? = withContext(
        Dispatchers.IO) {
        try {
            val response = apiService.getArticleSummary(title = searchQuery)

            if (response.isSuccessful) {
                return@withContext response.body()
            }

            return@withContext null
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }
}