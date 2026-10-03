// Корневой файл сборки: только объявляет плагины, применяются они в модуле app
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
}
