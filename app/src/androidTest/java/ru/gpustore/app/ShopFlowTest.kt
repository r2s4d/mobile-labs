package ru.gpustore.app

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.gpustore.app.ui.MenuActivity

/**
 * Сквозной тест на эмуляторе: меню, каталог, карточка, корзина.
 * Проверяет, что переходы между Activity и работа с базой данных связаны правильно.
 */
@RunWith(AndroidJUnit4::class)
class ShopFlowTest {

    @Before
    fun cleanCart() = runBlocking {
        val app = ApplicationProvider.getApplicationContext<Context>() as StoreApp
        app.repository.seedIfEmpty()
        app.repository.clearCart()
    }

    /** Данные приходят из базы асинхронно, поэтому проверку нужно повторять до 5 секунд */
    private fun eventually(check: () -> Unit) {
        val deadline = System.currentTimeMillis() + 5_000
        while (true) {
            try {
                check()
                return
            } catch (e: Throwable) {
                if (System.currentTimeMillis() > deadline) throw e
                if (e !is NoMatchingViewException && e !is AssertionError && e !is RuntimeException) throw e
                Thread.sleep(100)
            }
        }
    }

    @Test
    fun addToCartFromCatalogShowsItemInCart() {
        ActivityScenario.launch(MenuActivity::class.java).use {
            onView(withText("Каталог")).perform(click())

            // Самая дорогая карта стоит первой в списке
            eventually { onView(withText("GeForce RTX 4090")).check(matches(isDisplayed())) }
            onView(withText("GeForce RTX 4090")).perform(click())

            eventually { onView(withText("В корзину")).check(matches(isDisplayed())) }
            onView(withText("В корзину")).perform(click())
            eventually { onView(withText("В корзине: 1")).check(matches(isDisplayed())) }

            // Возвращаемся в меню: карточка, каталог, меню
            pressBack()
            pressBack()
            eventually { onView(withText("Товаров в корзине: 1")).check(matches(isDisplayed())) }

            onView(withText("Корзина и избранное")).perform(click())
            eventually { onView(withText("GeForce RTX 4090")).check(matches(isDisplayed())) }
            // Цена одна и в строке, и в итоге, поэтому проверяем только наличие панели с итогом
            onView(withText("Итого")).check(matches(isDisplayed()))
        }
    }
}
