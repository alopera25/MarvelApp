package com.marvelapp.ui.screens.detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.marvelapp.R
import com.marvelapp.data.Character
import com.marvelapp.data.Comic
import com.marvelapp.data.ComicSummary
import com.marvelapp.data.Event
import com.marvelapp.data.EventSummary
import com.marvelapp.data.Serie
import com.marvelapp.data.SeriesSummary
import com.marvelapp.ui.screens.Screen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun DetailScreen(
    vm: DetailViewModel,
    onBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state by vm.state.collectAsState()
    val scrollState = rememberScrollState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(it)
            vm.onMessageShown()
        }
    }

    Screen {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Details") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(id = R.string.back)
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { vm.onFavoriteClicked() }) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(id = R.string.favorite)
                    )
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            contentWindowInsets = WindowInsets.safeDrawing
        ) { padding ->/*
            if (state.loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }*/
            state.character?.let { character ->
                CharacterDetail(character, vm, scrollState, padding,sharedTransitionScope,animatedVisibilityScope)
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CharacterDetail(
    character: Character,
    vm: DetailViewModel,
    scrollState: ScrollState,
    padding: PaddingValues,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope){
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            val imageUrl = character.thumbnail?.let { "${it.path}.${it.extension}" }
            AsyncImage(
                model = imageUrl,
                contentDescription = character.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.small)
                    .parallaxLayoutModifier(
                        scrollState,
                        rate = 2
                    )
                    .sharedElement(
                        rememberSharedContentState(key = "image-${character.id}"),
                        animatedVisibilityScope
                    )
            )
            Text(
                text = "Name:",
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = character.name!!,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
            )
            Text(
                text = "Description:",
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleLarge
            )
            val description = if (character.description.isNullOrEmpty()) {
                "No description available"
            } else {
                character.description
            }
            Text(
                text = description,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
            )

            Text(
                text = "Comics: ${character.comics?.size ?: 0}",
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleLarge
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(character.comics ?: emptyList()) { comic ->
                    ComicItem(comic, vm)
                }
            }

            Text(
                text = "Series: ${character.series?.size ?: 0}",
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleLarge
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(character.series ?: emptyList()) { serie ->
                    SerieItem(serie, vm)
                }
            }

            Text(
                text = "Events: ${character.events?.size ?: 0}",
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleLarge
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(character.events ?: emptyList()) { event ->
                    EventItem(event, vm)
                }
            }
        }
    }

}

@Composable
fun ComicItem(comicSummary: ComicSummary, vm: DetailViewModel) {
    val comic by produceState<Comic?>(null, comicSummary) {
        value = vm.fetchComicDetails(comicSummary.resourceURI?.substringAfterLast("/")?.toInt() ?: 0)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(125.dp)
            .padding(4.dp)
    ) {
        val imageUrl = comic?.thumbnail?.let { "${it.path}.${it.extension}" }
        AsyncImage(
            model = imageUrl,
            contentDescription = comic?.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(MaterialTheme.shapes.small)
        )
        Text(
            text = comic?.title ?: "Loading...",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun SerieItem(serieSummary: SeriesSummary, vm: DetailViewModel) {
    val serie by produceState<Serie?>(null, serieSummary) {
        value =
            vm.fetchSerieDetails(serieSummary.resourceURI?.substringAfterLast("/")?.toInt() ?: 0)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(125.dp)
            .padding(4.dp)
    ) {
        val imageUrl = serie?.thumbnail?.let { "${it.path}.${it.extension}" }
        AsyncImage(
            model = imageUrl,
            contentDescription = serie?.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(MaterialTheme.shapes.small)
        )
        Text(
            text = serie?.title ?: "Loading...",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun EventItem(eventSummary: EventSummary, vm: DetailViewModel) {
    val event by produceState<Event?>(null, eventSummary) {
        value =
            vm.fetchEventDetails(eventSummary.resourceURI?.substringAfterLast("/")?.toInt() ?: 0)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(125.dp)
            .padding(4.dp)
    ) {
        val imageUrl = event?.thumbnail?.let { "${it.path}.${it.extension}" }
        AsyncImage(
            model = imageUrl,
            contentDescription = event?.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(MaterialTheme.shapes.small)
        )
        Text(
            text = event?.title ?: "Loading...",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

fun Modifier.parallaxLayoutModifier(scrollState: ScrollState, rate: Int) =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val height = if (rate > 0) scrollState.value / rate else scrollState.value
        layout(placeable.width, placeable.height) {
            placeable.place(0, height)
        }
    }
