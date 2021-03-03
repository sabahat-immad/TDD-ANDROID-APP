package petros.efthymiou.groovy.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

open class BaseUntTest {
    @get:Rule
    val coroutineTestRule = MainCoroutineScopeRule()

    //for livedata
    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
}