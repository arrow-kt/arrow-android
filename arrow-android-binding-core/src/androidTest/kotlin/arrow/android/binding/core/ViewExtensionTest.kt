package arrow.android.binding.core

import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import arrow.android.binding.core.test.R
import arrow.fx.coroutines.stream.Stream
import arrow.fx.coroutines.stream.concurrent.Queue
import arrow.fx.coroutines.stream.drain
import arrow.fx.coroutines.stream.toList
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class ViewExtensionTest {

    @get:Rule
    val activityRule = activityScenarioRule<TestActivity>()

    @Test
    fun clickEmitsUnitIntoStream(): Unit = runBlocking {
        val events = activityRule.scenario.captureButtonEvents { clicks() }

        onView(withId(R.id.button)).perform(click())
        onView(withId(R.id.button)).perform(click())

        check(events.take(2).toList() == listOf(Unit, Unit))
    }

    @Test
    fun longClickEmitsUnitIntoStream(): Unit = runBlocking {
        val events = activityRule.scenario.captureButtonEvents { longClicks() }

        onView(withId(R.id.button)).perform(longClick())
        onView(withId(R.id.button)).perform(longClick())

        check(events.take(2).toList() == listOf(Unit, Unit))
    }

}

private suspend fun <T> ActivityScenario<TestActivity>.captureButtonEvents(
    findEvents: Button.() -> Stream<T>
): Stream<T> = captureStream {
    onActivity { activity ->
        activity.setContentView(R.layout.test_activity_button)
        val button = activity.findViewById<Button>(R.id.button)
        activity.lifecycleScope.launchWhenCreated {
            button.findEvents().capture()
        }
    }
}

suspend fun <O> captureStream(block: suspend StreamCaptor<O>.() -> Unit): Stream<O> =
    StreamCaptor<O>().apply { block() }.queue.dequeue()

class StreamCaptor<O>(val queue: Queue<O> = Queue.unsafeUnbounded()) {
    suspend fun Stream<O>.capture(): Unit = effectTap { queue.enqueue1(it) }.drain()
}
