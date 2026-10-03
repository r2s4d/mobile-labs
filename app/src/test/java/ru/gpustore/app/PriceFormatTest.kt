package ru.gpustore.app

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.gpustore.app.util.formatPrice

/** Проверка форматирования цены. Это обычный unit-тест: запускается на компьютере без эмулятора. */
class PriceFormatTest {

    private val nbsp = " "

    @Test
    fun `small price has no separator`() {
        assertEquals("999${nbsp}₽", formatPrice(999))
    }

    @Test
    fun `thousands are separated by a space`() {
        assertEquals("17${nbsp}990${nbsp}₽", formatPrice(17_990))
    }

    @Test
    fun `six digit price`() {
        assertEquals("189${nbsp}990${nbsp}₽", formatPrice(189_990))
    }

    @Test
    fun `millions`() {
        assertEquals("1${nbsp}234${nbsp}567${nbsp}₽", formatPrice(1_234_567))
    }
}
