package dev.psuchanek.endurancepacecalculator.ui.zones

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.psuchanek.endurancepacecalculator.R
import dev.psuchanek.endurancepacecalculator.adapters.viewholders.ZonesViewHolder
import dev.psuchanek.endurancepacecalculator.launchMainCollectionFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@HiltAndroidTest
@MediumTest
@RunWith(AndroidJUnit4::class)
class ZonesCalculatorFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun inputInBPM_thenSwitchToFTPwithInput_returnBackToLTHR_expectListStillVisible() {
        launchMainCollectionFragment(navController)


        onView(withText(R.string.zones_calculator_tab_label)).perform(click())

        onView(withId(R.id.tiBPM)).perform(ViewActions.typeText("170"), closeSoftKeyboard())

        onView(withId(R.id.dropDownZonesSpinner)).perform(click())
        onView(withText("Functional Threshold Power")).inRoot(RootMatchers.isPlatformPopup())
            .perform(
                click()
            )

        onView(withId(R.id.tiFTP)).perform(ViewActions.typeText("250"), closeSoftKeyboard())
        onView(withId(R.id.zonesRecyclerView)).check(matches(hasChildCount(7)))

        onView(withId(R.id.dropDownZonesSpinner)).perform(click())
        onView(withText("Lactate Threshold Heart Rate")).inRoot(RootMatchers.isPlatformPopup())
            .perform(
                click()
            )

        onView(withId(R.id.zonesRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<ZonesViewHolder>(
                4
            )
        ).check(matches(hasDescendant(withText("5"))))

        onView(withId(R.id.zonesRecyclerView)).check(matches(hasChildCount(5)))

    }


    @Test
    fun openSwimPace_thenChangeValues_switchToLTHR_inputLTHR_switchBackToSwimPace_expectRecyclerViewDisplayingSwimZones() {
        launchMainCollectionFragment(navController)


        onView(withText(R.string.zones_calculator_tab_label)).perform(click())
        onView(withId(R.id.dropDownZonesSpinner)).perform(click())
        onView(withText("Swim Pace")).inRoot(RootMatchers.isPlatformPopup()).perform(
            click()
        )


        onView(withId(R.id.dropDownZonesSpinner)).perform(click())
        onView(withText("Lactate Threshold Heart Rate")).inRoot(RootMatchers.isPlatformPopup())
            .perform(
                click()
            )
        onView(withId(R.id.tiBPM)).perform(ViewActions.typeText("170"), closeSoftKeyboard())

        onView(withId(R.id.zonesRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<ZonesViewHolder>(
                4
            )
        ).check(matches(hasDescendant(withText("5"))))

        onView(withId(R.id.zonesRecyclerView)).check(matches(hasChildCount(5)))


        onView(withId(R.id.dropDownZonesSpinner)).perform(click())

        onView(withText("Swim Pace")).inRoot(RootMatchers.isPlatformPopup()).perform(
            click()
        )

        onView(withId(R.id.zonesRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<ZonesViewHolder>(
                6
            )
        ).check(matches(hasDescendant(withText("7"))))
        onView(withId(R.id.zonesRecyclerView)).check(matches(hasChildCount(7)))

    }

}