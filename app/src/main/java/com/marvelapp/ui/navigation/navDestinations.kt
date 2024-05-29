package com.marvelapp.ui.navigation

import kotlinx.serialization.Serializable


@Serializable
object Home

@Serializable
object Splash

@Serializable
data class Detail (val characterId: Int)