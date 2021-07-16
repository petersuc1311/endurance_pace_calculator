package dev.psuchanek.endurancepacecalculator.ui.splits

import android.view.View
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.android.material.slider.Slider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.psuchanek.endurancepacecalculator.R
import dev.psuchanek.endurancepacecalculator.adapters.EnduranceListAdapter
import dev.psuchanek.endurancepacecalculator.adapters.ZonesListAdapter
import dev.psuchanek.endurancepacecalculator.adapters.viewholders.SplitsViewHolder
import dev.psuchanek.endurancepacecalculator.launchMainCollectionFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@HiltAndroidTest
@MediumTest
@RunWith(AndroidJUnit4::class)
class SplitsCalculatorFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun switchToThreeKilometerDistance_thenSwitchToFourHundredMeterFrequency_expectRecyclerViewToUpdate() {
        launchMainCollectionFragment(navController)

        onView(withText(R.string.splits_calculator_tab_label)).perform(click())
        onView(withId(R.id.splitsRecyclerView))
            .perform(RecyclerViewActions.scrollToPosition<SplitsViewHolder>(1))
            .check(matches(hasDescendant(withText("0.1"))))

        onView(withId(R.id.dropDownSplitDistanceSpinner)).perform(click())
        onView(withText("5 km")).inRoot(RootMatchers.isPlatformPopup()).perform(
            click()
        )

        onView(withId(R.id.dropDownFrequencySpinner)).perform(click())
        onView(withText("Split each 1km")).inRoot(RootMatchers.isPlatformPopup()).perform(
            click()
        )

        onView(withId(R.id.splitsRecyclerView))
            .perform(RecyclerViewActions.scrollToPosition<SplitsViewHolder>(1))
            .check(matches(hasDescendant(withText("1.0"))))


    }
}