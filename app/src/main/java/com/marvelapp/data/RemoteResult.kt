package com.marvelapp.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.serializersModuleOf

@Serializable
data class RemoteResult(
    val data: Data
)

@Serializable
data class Data(
    val results: List<RemoteCharacter>
)

@Serializable
data class RemoteCharacter(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail?,
    val comics: ComicList?,
    val events: EventList?,
    val series: SeriesList?
)

@Serializable
data class ComicList(
    val items: List<ComicSummary>?
)

@Serializable
data class EventList(
    val items: List<EventSummary>?
)

@Serializable
data class SeriesList(
    val items: List<SeriesSummary>?
)

@Serializable
data class EventSummary(
    val resourceURI: String?,
    val name: String?
)

@Serializable
data class SeriesSummary(
    val resourceURI: String?,
    val name: String?
)

@Serializable
data class ComicSummary(
    val resourceURI: String?,
    val name: String?
)

@Serializable
data class ComicDataWrapper(
    val data: ComicDataContainer
)

@Serializable
data class ComicDataContainer(
    val results: List<Comic>
)

@Serializable
data class Comic(
    val id: Int,
    val title: String,
    val thumbnail: Thumbnail?
)

@Serializable
data class EventDataWrapper(
    val data: EventDataContainer
)

@Serializable
data class EventDataContainer(
    val results: List<Event>
)

@Serializable
data class Event(
    val id: Int,
    val title: String,
    val thumbnail: Thumbnail?
)

@Serializable
data class SerieDataWrapper(
    val data: SerieDataContainer
)

@Serializable
data class SerieDataContainer(
    val results: List<Serie>
)

@Serializable
data class Serie(
    val id: Int,
    val title: String,
    val thumbnail: Thumbnail?
)

@Serializable
data class Thumbnail(
    @SerialName("path") val path: String,
    @SerialName("extension") val extension: String
)