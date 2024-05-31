package com.marvelapp.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersService {

    @GET("/v1/public/characters")
    suspend fun fetchCharacter(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): RemoteResult


    @GET("/v1/public/characters/{characterId}")
    suspend fun fetchCharacterById(@Path("characterId") characterId: Int): RemoteResult

    @GET("/v1/public/comics/{comicId}")
    suspend fun getComicDetails(@Path("comicId") comicId: Int): ComicDataWrapper

    @GET("/v1/public/series/{serieId}")
    suspend fun getSerieDetails(@Path("serieId") serieId: Int): SerieDataWrapper

    @GET("/v1/public/events/{eventId}")
    suspend fun getEventDetails(@Path("eventId") eventId: Int): EventDataWrapper
}
