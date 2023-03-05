package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest : AutoCloseKoinTest() {

//    TODO: test the navigation of the fragments.
//    TODO: test the displayed data on the UI.
//    TODO: add testing for the error messages.


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var repository: ReminderDataSource
    private lateinit var appContext: Application

    @Before
    fun initialize() {
        repository = FakeDataSource()
        stopKoin()//stop the original app koin
        appContext = getApplicationContext()
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single {
                SaveReminderViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { LocalDB.createRemindersDao(appContext) }
        }
        //declare a new koin module
        startKoin {
            modules(listOf(myModule))
        }
        //Get our real repository
        repository = get()

        runBlocking {
            repository.deleteAllReminders()
        }

    }

    @Test
    fun getReminders_DisplayIt() {
        runBlocking {

            var reminderToSave = ReminderDTO(
                "Test Reminder",
                "Reminder description",
                "Location Name", 7.654545, 8.64565
            )
            repository.saveReminder(reminderToSave)
            val scenario =
                launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
            val navController = mock(NavController::class.java)
            scenario.onFragment {
                it.view?.let { it1 -> Navigation.setViewNavController(it1, navController) }
            }
            onView(withId(R.id.reminderssRecyclerView))
                .perform(
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        ViewMatchers.hasDescendant(ViewMatchers.withText(reminderToSave.title)),
                        click()
                    )
                )
        }
    }


    @Test
    fun clickActionBtn_NavigateToReminders() {

        val scenario =
            launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            it.view?.let { it1 -> Navigation.setViewNavController(it1, navController) }
        }
        onView(withId(R.id.addReminderFAB))
            .perform(
                click()
            )

        verify(navController).navigate(ReminderListFragmentDirections.toSaveReminder())

    }

    @Test
    fun emptyList_DisplayNoItems() {
        runBlocking {
            repository.deleteAllReminders()
            val scenario =
                launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
            val navController = mock(NavController::class.java)
            scenario.onFragment {
                it.view?.let { it1 -> Navigation.setViewNavController(it1, navController) }
            }
            onView(withId(R.id.noDataTextView))
                .check(
                    ViewAssertions.matches(ViewMatchers.isDisplayed())
                )

        }
    }
}