package ru.s44khin.messenger.presentation.main.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(
    activity: FragmentActivity,
    private val fragments: Map<Int, Fragment>
) : FragmentStateAdapter(activity) {

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]!!
}