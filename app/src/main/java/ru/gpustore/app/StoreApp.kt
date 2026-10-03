package ru.gpustore.app

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.gpustore.app.data.StoreDatabase
import ru.gpustore.app.data.StoreRepository

/**
 * Класс приложения: создается один раз при запуске процесса, раньше любой Activity.
 * Здесь живут база данных и репозиторий, общие для всех экранов.
 */
class StoreApp : Application() {

    // lazy: база создается при первом обращении, а не при старте процесса
    private val database by lazy { StoreDatabase.create(this) }
    val repository by lazy { StoreRepository(database.storeDao()) }

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // Заполняем каталог при первом запуске, в фоновом потоке, чтобы не тормозить интерфейс
        appScope.launch { repository.seedIfEmpty() }
    }
}
