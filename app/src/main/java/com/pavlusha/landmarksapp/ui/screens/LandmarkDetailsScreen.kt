package com.pavlusha.landmarksapp.ui.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.LandmarkSource
import com.pavlusha.domain.model.WikipediaInfoDomainModel
import com.pavlusha.landmarksapp.R
import com.pavlusha.landmarksapp.ui.event.LandmarkDetailsEvent
import com.pavlusha.landmarksapp.ui.intent.LandmarkDetailsIntent
import com.pavlusha.landmarksapp.ui.state.DetailsTab
import com.pavlusha.landmarksapp.ui.viewmodel.LandmarkDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandmarkDetailsScreen(
    landmarkId: String,
    source: LandmarkSource,
    onNavigateBack: () -> Unit,
    viewModel: LandmarkDetailsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    var showNoteDialog by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf("") }

    LaunchedEffect(landmarkId) {
        viewModel.onIntent(LandmarkDetailsIntent.LoadLandmark(landmarkId, source))
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is LandmarkDetailsEvent.NavigateBack -> onNavigateBack()
                is LandmarkDetailsEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        context.resources.getString(event.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.landmark?.name ?: "Загрузка...") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onIntent(LandmarkDetailsIntent.OnBackClicked) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (state.landmark != null) {
                        IconButton(onClick = { viewModel.onIntent(LandmarkDetailsIntent.OnToggleFavorite) }) {
                            Icon(
                                imageVector = if (state.landmark!!.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Избранное",
                                tint = if (state.landmark!!.isFavorite) colorResource(id = R.color.red) else colorResource(
                                    id = R.color.dark_gray
                                )
                            )
                        }
                    }
                }
            )
        },

        floatingActionButton = {
            if (state.landmark != null) {
                FloatingActionButton(
                    onClick = { showNoteDialog = true },
                    containerColor = colorResource(id = R.color.red),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.AddComment, contentDescription = "Добавить заметку")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = colorResource(id = R.color.red)
                )
            } else if (state.error != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Ошибка загрузки", color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        viewModel.onIntent(
                            LandmarkDetailsIntent.LoadLandmark(
                                landmarkId,
                                source
                            )
                        )
                    }) {
                        Text("Повторить")
                    }
                }
            } else if (state.landmark != null) {
                Column(modifier = Modifier.fillMaxSize()) {
                    TabRow(
                        selectedTabIndex = state.selectedTab.ordinal,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = colorResource(id = R.color.black)
                    ) {
                        Tab(
                            selected = state.selectedTab == DetailsTab.STATIC_INFO,
                            onClick = {
                                viewModel.onIntent(
                                    LandmarkDetailsIntent.OnTabSelected(
                                        DetailsTab.STATIC_INFO
                                    )
                                )
                            },
                            text = { Text("Информация") }
                        )
                        Tab(
                            selected = state.selectedTab == DetailsTab.AR_MODE,
                            onClick = {
                                viewModel.onIntent(
                                    LandmarkDetailsIntent.OnTabSelected(
                                        DetailsTab.AR_MODE
                                    )
                                )
                            },
                            text = { Text("AR Режим") }
                        )
                    }

                    Crossfade(targetState = state.selectedTab, label = "Tab Transition") { tab ->
                        when (tab) {
                            DetailsTab.STATIC_INFO -> StaticInfoContent(
                                landmark = state.landmark!!,
                                wikiInfo = state.wikipediaInfo,
                                isWikiLoading = state.isWikiLoading
                            )
                            DetailsTab.AR_MODE -> ArModeContent(landmark = state.landmark!!)
                        }
                    }
                }
            }
        }
        if (showNoteDialog) {
            AlertDialog(
                onDismissRequest = {
                    showNoteDialog = false
                    noteText = ""
                },
                title = { Text("Новая заметка") },
                text = {
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Напишите свои впечатления...") },
                        minLines = 3,
                        maxLines = 6
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (noteText.isNotBlank()) {
                                viewModel.onIntent(LandmarkDetailsIntent.OnSaveNoteClicked(noteText))
                                showNoteDialog = false
                                noteText = ""
                            }
                        }
                    ) {
                        Text("Сохранить", color = colorResource(id = R.color.red))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showNoteDialog = false
                            noteText = ""
                        }
                    ) {
                        Text("Отмена", color = Color.Gray)
                    }
                }
            )
        }
    }
}

@Composable
fun StaticInfoContent(
    landmark: LandmarkDomainModel,
    wikiInfo: WikipediaInfoDomainModel?,
    isWikiLoading: Boolean
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        val displayImageUrl = wikiInfo?.thumbnailUrl
            ?: wikiInfo?.originalImageUrl
            ?: landmark.mainImageUrl
            ?: landmark.thumbnailUrl

        if (!displayImageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(displayImageUrl)
                    .crossfade(true)
                    .addHeader("User-Agent", "LandmarkARApp/1.0 (pavelbrutalll@gmail.com)")
                    .listener(
                        onStart = {
                            Log.d("COIL_DEBUG", "Начали загрузку картинки: $displayImageUrl")
                        },
                        onSuccess = { _, _ ->
                            Log.d("COIL_DEBUG", "УСПЕХ! Картинка загружена.")
                        },
                        onError = { _, result ->
                            Log.e("COIL_DEBUG", "ОШИБКА загрузки картинки: ${result.throwable.message}")
                            result.throwable.printStackTrace()
                        }
                    )
                    .build(),
                contentDescription = landmark.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = colorResource(id = R.color.light_gray)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = landmark.category.name,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Описание",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (isWikiLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = colorResource(id = R.color.red)
                )
            }
        } else if (wikiInfo != null) {
            Text(
                text = wikiInfo.extract,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (wikiInfo.mobileArticleUrl != null) {
                TextButton(
                    onClick = { uriHandler.openUri(wikiInfo.mobileArticleUrl!!) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Читать подробнее", color = colorResource(id = R.color.red))
                }
            }
        } else {
            Text(
                text = landmark.description.ifEmpty { landmark.shortDescription },
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (landmark.tags.isNotEmpty()) {
            Text(
                text = "Теги: ${landmark.tags.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ArModeContent(landmark: LandmarkDomainModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.ViewInAr,
                contentDescription = "AR",
                tint = Color.White,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (landmark.currentModel3dPath != null) {
                Text(
                    text = "Загрузка 3D модели...",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Путь: ${landmark.currentModel3dPath}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Text(
                    text = "3D модель не найдена для этого объекта",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}