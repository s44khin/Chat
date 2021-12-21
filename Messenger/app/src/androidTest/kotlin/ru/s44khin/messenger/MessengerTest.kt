package ru.s44khin.messenger

import android.os.Debug
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.s44khin.messenger.presentation.main.MainActivity

@RunWith(AndroidJUnit4::class)
class MessengerTest {

    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ru.s44khin.messenger", appContext.packageName)
    }

    @Test
    fun checkTextButton() {
        onView(withId(R.id.newStream)).perform(click())
        onView(withId(R.id.button)).check(matches(withText(R.string.create)))
    }

    @Test
    fun checkReopenAndText() {

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        device.pressHome()
        device.pressRecentApps()
        device.pressRecentApps()

        onView(withId(R.id.newStream)).check(matches(withText(R.string.create)))

        Debug.waitForDebugger()
    }
}