package arrow.android.binding.core

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.test.core.app.ActivityScenario

class TestActivity : AppCompatActivity()

suspend fun ActivityScenario<TestActivity>.withLayout(
    @LayoutRes layout: Int,
    whenCreated: suspend TestActivity.() -> Unit,
) {
    onActivity { activity ->
        activity.setContentView(layout)
        activity.lifecycleScope.launchWhenCreated {
            activity.whenCreated()
        }
    }
}
