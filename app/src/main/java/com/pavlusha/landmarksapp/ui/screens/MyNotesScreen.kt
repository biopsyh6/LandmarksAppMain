package com.pavlusha.landmarksapp.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pavlusha.domain.model.NoteWithLandmarkDomainModel
import com.pavlusha.landmarksapp.ui.event.MyNotesEvent
import com.pavlusha.landmarksapp.ui.intent.MyNotesIntent
import com.pavlusha.landmarksapp.ui.viewmodel.MyNotesViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNotesScreen(
    rootNavController: NavController,
    viewModel: MyNotesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    var editingNote by remember { mutableStateOf<NoteWithLandmarkDomainModel?>(null) }
    var editText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.onIntent(MyNotesIntent.LoadNotes)

        viewModel.event.collect { event ->
            when (event) {
                is MyNotesEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is MyNotesEvent.NavigateBack -> rootNavController.popBackStack()
                is MyNotesEvent.NavigateToDetails -> {
                    rootNavController.navigate("details_screen/${Uri.encode(event.landmarkId)}/${event.source.name}")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои заметки", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onIntent(MyNotesIntent.OnBackClicked) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.notes.isEmpty()) {
                Text(
                    text = "У вас пока нет заметок",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.notes, key = { it.note.id }) { item ->
                        NoteCard(
                            item = item,
                            onLandmarkClick = {
                                viewModel.onIntent(MyNotesIntent.OnLandmarkClicked(item.landmark.id, item.landmark.source))
                            },
                            onEditClick = {
                                editingNote = item
                                editText = item.note.text
                            },
                            onDeleteClick = {
                                viewModel.onIntent(MyNotesIntent.DeleteNote(item.note.id))
                            }
                        )
                    }
                }
            }
        }

        if (editingNote != null) {
            AlertDialog(
                onDismissRequest = { editingNote = null },
                title = { Text("Редактировать заметку") },
                text = {
                    OutlinedTextField(
                        value = editText,
                        onValueChange = { editText = it },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 6
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (editText.isNotBlank()) {
                            viewModel.onIntent(MyNotesIntent.UpdateNote(editingNote!!.note, editText))
                        }
                        editingNote = null
                    }) {
                        Text("Сохранить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { editingNote = null }) {
                        Text("Отмена", color = Color.Gray)
                    }
                }
            )
        }
    }
}

@Composable
fun NoteCard(
    item: NoteWithLandmarkDomainModel,
    onLandmarkClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.landmark.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onLandmarkClick() }
                    .padding(bottom = 8.dp)
            )

            Text(
                text = item.note.text,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateFormat.format(Date(item.note.updatedAt)),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Row {
                    IconButton(onClick = onEditClick, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Изменить", tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color.Red, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}