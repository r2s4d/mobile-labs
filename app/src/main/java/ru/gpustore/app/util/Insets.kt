package ru.gpustore.app.util

import android.graphics.Color
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Начиная с Android 15 приложение рисуется под системными панелями (статус-бар, навигация).
 * Эта функция добавляет к корневому View отступы на высоту панелей,
 * чтобы содержимое не оказывалось под ними.
 */
fun View.applySystemBarsPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val bars: Insets = windowInsets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        )
        view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
        WindowInsetsCompat.CONSUMED
    }
}

/**
 * Включает отрисовку под системными панелями и принудительно задает светлые значки на них.
 * Приложение всегда темное, поэтому значки статус-бара должны быть светлыми
 * независимо от того, какая тема включена в системе.
 */
fun ComponentActivity.enableDarkEdgeToEdge() {
    enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
    )
}
