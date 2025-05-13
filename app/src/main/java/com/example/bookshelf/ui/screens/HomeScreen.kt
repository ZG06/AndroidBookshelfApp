package com.example.bookshelf.ui.screens

import android.os.Build
import android.text.Html
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.R
import com.example.bookshelf.model.BookDetailItem
import com.example.bookshelf.ui.theme.BookshelfTheme


@Composable
fun HomeScreen(
    bookshelfUiState: BookshelfUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (bookshelfUiState) {
        is BookshelfUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is BookshelfUiState.Success -> {
            if (bookshelfUiState.details.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_result_found),
                    textAlign = TextAlign.Center
                )
            } else {
                DetailsGridScreen(
                    bookshelfUiState.details,
                    modifier = modifier
                )
            }
        }
        is BookshelfUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun DetailsCard(
    title: String,
    description: String,
    thumbnail: String,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(
            topStart = dimensionResource(R.dimen.padding_medium),
            topEnd = dimensionResource(R.dimen.padding_medium)
        )
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.padding_medium),
                    top = dimensionResource(R.dimen.padding_small),
                    bottom = dimensionResource(R.dimen.padding_small)
                )
                    .fillMaxWidth()
            ) {
                Text(
                    text = title,
                    maxLines = Int.MAX_VALUE,
                    overflow = TextOverflow.Visible,
                    modifier = Modifier.fillMaxWidth(0.85f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                DetailsItemButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded }
                )
            }

            if (expanded) {
                Text(
                    text = parseHtmlToPlainText(description),
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(R.dimen.padding_medium)
                    )
                )
            }

            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(thumbnail)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = stringResource(R.string.book_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun DetailsGridScreen(
    detailsList: List<BookDetailItem>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier.padding(horizontal = 4.dp),
        contentPadding = contentPadding
    ) {
        items(detailsList) { book ->
            DetailsCard(
                title = book.title,
                description = book.description,
                thumbnail = book.thumbnail,
                modifier = modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(100.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
private fun DetailsItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = stringResource(R.string.expand_button_content_description)
        )
    }
}

private fun parseHtmlToPlainText(html: String): String {
    return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    BookshelfTheme {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    BookshelfTheme {
        ErrorScreen({})
    }
}

@Preview(showBackground = true)
@Composable
fun DetailGridScreenPreview() {
    BookshelfTheme {
        val mockData = listOf<String>("https://books.google.com/books/publisher/content?id=S_r1DwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&imgtk=AFLRE719KHdmUZqXphOZnHDz37iw_RRBstLNLmEryOSTSLMsFRlgfNEitl3VNTv9P_ASwyi1dthOKGC9bn_Cr8BslDROQC48YqPGlKb7DlXZO7U5Mxvvi24a7uM94TglNDi4ID3bM1XT&source=gbs_api", "https://books.google.com/books/publisher/content?id=g4JDEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&imgtk=AFLRE71YyTiU3qe-l7tkoNUNs5t_azxtLyb3Yu4_QnQrhUE0qZpu3_E1FFfdhGH-SejGgQeLHLg9UnrpDkRH1oEXIn5G_Ao6oCeNfyju_kfVfuXvyvogoHHUE9-39KUYkPMeV-arCWCS&source=gbs_api")
        (mockData)
    }
}