package com.example.bookfinder20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookfinder20.ui.theme.BookFinder20Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookFinder20Theme {
                val repository = BookRepository()
                val viewModel = ViewModelProvider(
                    this,
                    ViewModelFactory(repository)
                )[BookViewModel::class.java]
                BookSearchScreen(viewModel)
            }
        }
    }
}

@Composable
fun BookSearchScreen(viewModel: BookViewModel) {
    val uiState by viewModel.uiState.observeAsState(BookSearchUiState())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (uiState.selectedBook == null) {
            // Search and List UI
            TextField(
                value = uiState.searchQuery,
                onValueChange = { newQuery ->
                    viewModel.updateSearchQuery(newQuery)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true,
                label = { Text("Search Books...") }
            )

            Button(onClick = {
                viewModel.searchBooks(uiState.searchQuery)
            }) {
                Text("Search")
            }

            LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                items(uiState.books) { book ->
                    val imageUrl = book.volumeInfo.imageLinks?.thumbnail ?: ""
                    val secureImageUrl = imageUrl.replace("http://", "https://")

                    val imageRequest = ImageRequest.Builder(LocalContext.current)
                        .data(secureImageUrl)
                        .crossfade(true) // Smooth transition for loading images
                        .build()

                    BookItemView(book, imageRequest) {
                        viewModel.selectBook(it)
                    }
                }
            }
        } else {
            BookDetailScreen(
                book = uiState.selectedBook!!,
                onBack = { viewModel.unselectBook() }
            )
        }
    }
}


@Composable
fun BookItemView(book: Item, imageRequest: ImageRequest, onBookClick: (Item) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onBookClick(book) }
    ) {

        // book cover
        AsyncImage(
            model = imageRequest,
            contentDescription = book.volumeInfo.title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(80.dp)
                .padding(end = 8.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = book.volumeInfo.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1, // Limit to a single line
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = book.volumeInfo.authors?.joinToString(", ") ?: "Author Not Listed",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
        }
    }
}


@Composable
fun BookDetailScreen(book: Item, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Back Button at the top
        Button(onClick = onBack, modifier = Modifier.padding(bottom = 16.dp)) {
            Text("Back")
        }

        val imageUrl = book.volumeInfo.imageLinks?.thumbnail ?: ""
        val secureImageUrl = imageUrl.replace("http://", "https://")

        val imageRequest = ImageRequest.Builder(LocalContext.current)
            .data(secureImageUrl)
            .crossfade(true) // Smooth transition for loading images
            .build()

        // centered book cover
        AsyncImage(
            model = imageRequest,
            contentDescription = book.volumeInfo.title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(100.dp),
            alignment = Alignment.Center,
        )

        // Book Title
        Text(
            text = book.volumeInfo.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Authors
        Text(
            text = "Author(s): ${book.volumeInfo.authors?.joinToString(", ") ?: "Not Listed"}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Publication Date
        book.volumeInfo.publishedDate?.let { date ->
            Text(
                text = "Published Date: $date",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Description
        book.volumeInfo.description?.let { description ->
            Text(
                text = "Book Description: $description",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
