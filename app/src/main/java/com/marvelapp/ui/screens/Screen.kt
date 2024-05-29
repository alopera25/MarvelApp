package com.marvelapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.marvelapp.ui.theme.MarvelAppTheme

@Composable
fun Screen(content: @Composable () -> Unit) {
    MarvelAppTheme {
         Surface(
         modifier = Modifier.fillMaxSize(),
         color = MaterialTheme.colorScheme.background,
         content = content
        )
    }
}