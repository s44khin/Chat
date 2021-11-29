package ru.s44khin.messenger.presentation.main.navigation

import android.view.MenuItem
import androidx.core.view.forEachIndexed
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

class Mediator(
    private val bottomNavigationView: BottomNavigationView,
    private val viewPager2: ViewPager2,
    private val config: ((BottomNavigationView, ViewPager2) -> Unit)? = null
) {
    private val map = mutableMapOf<MenuItem, Int>()

    init {
        bottomNavigationView.menu.forEachIndexed { index, item -> map[item] = index }
    }

    fun attach() {
        config?.invoke(bottomNavigationView, viewPager2)

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationView.selectedItemId =
                    bottomNavigationView.menu.getItem(position).itemId
            }
        })

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            viewPager2.currentItem = map[menuItem] ?: error("Error")
            true
        }
    }
}