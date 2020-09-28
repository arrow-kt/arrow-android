package arrow.android.binding.core

import android.widget.Button
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import arrow.android.binding.core.test.R
import org.junit.Rule
import org.junit.Test

class ViewExtensionTest {

    @get:Rule
    val activityRule = activityScenarioRule<TestActivity>()


    @Test
    fun clickEmitsUnitIntoStream(): Unit = testStream {
        activityRule.scenario.withLayout(R.layout.test_activity_button) {
            findViewById<Button>(R.id.button).clicks().capture()
        }
        onView(withId(R.id.button)).perform(click())
        expect(Unit)
        onView(withId(R.id.button)).perform(click())
        expect(Unit)
        expectNothingMore()
    }

    @Test
    fun longClickEmitsUnitIntoStream(): Unit = testStream {
        activityRule.scenario.withLayout(R.layout.test_activity_button) {
            findViewById<Button>(R.id.button).longClicks().capture()
        }
        onView(withId(R.id.button)).perform(longClick())
        expect(Unit)
        onView(withId(R.id.button)).perform(longClick())
        expect(Unit)
        expectNothingMore()
    }
}
