package com.example.domain.model.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MainItem(
    @StringRes val title: String,
    @DrawableRes val image: Int
)
