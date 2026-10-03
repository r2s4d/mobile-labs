package ru.gpustore.app.ui

import android.app.Activity
import ru.gpustore.app.StoreApp
import ru.gpustore.app.data.StoreRepository

/** Короткий доступ к репозиторию из любой Activity: он создается один раз в классе приложения */
val Activity.repository: StoreRepository
    get() = (application as StoreApp).repository
