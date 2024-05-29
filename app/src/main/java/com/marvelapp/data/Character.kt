package com.marvelapp.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

data class Character(
    val id: Int?,
    val name: String?,
    val description: String?,
    val thumbnail: Thumbnail?,
    val comics: List<ComicSummary>?,
    val events: List<EventSummary>?,
    val series: List<SeriesSummary>?
)

