package ru.gpustore.app.util

import androidx.annotation.DrawableRes
import ru.gpustore.app.R

/**
 * Подбирает иллюстрацию видеокарты по производителю.
 * Ссылки на ресурсы не хранятся в базе: их числовые id меняются между сборками.
 */
@DrawableRes
fun brandArt(brand: String): Int = when (brand) {
    "AMD" -> R.drawable.gpu_amd
    "Intel" -> R.drawable.gpu_intel
    else -> R.drawable.gpu_nvidia
}
