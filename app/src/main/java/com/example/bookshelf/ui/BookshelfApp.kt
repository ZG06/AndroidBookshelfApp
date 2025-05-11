@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.bookshelf.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelf.R
import com.example.bookshelf.ui.screens.BookshelfViewModel
import com.example.bookshelf.ui.screens.HomeScreen


@Composable
fun BookshelfApp() {
    Scaffold(
        topBar = { BookshelfTopAppBar() }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val bookshelfViewModel: BookshelfViewModel =
                viewModel(factory = BookshelfViewModel.Factory)
            HomeScreen(
                bookshelfUiState = bookshelfViewModel.bookshelfUiState,
                retryAction = bookshelfViewModel::getThumbnails
            )
        }
    }
}

@Composable
fun BookshelfTopAppBar(
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.height(100.dp),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = TextStyle(fontSize = 28.sp)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF9C3F24)
        )
    )
}