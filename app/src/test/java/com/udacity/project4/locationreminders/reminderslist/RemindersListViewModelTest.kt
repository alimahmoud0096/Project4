package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.utils.KoinAfter
import com.udacity.project4.locationreminders.utils.MainCoroutineRule
import com.udacity.project4.locationreminders.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest : KoinAfter(){

    //TODO: provide testing to the RemindersListViewModel and its live data objects

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private var dataSource = FakeDataSource()
    lateinit var remindersListViewModel: RemindersListViewModel

    @Before
    fun setupViewModel() {
        remindersListViewModel =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), dataSource)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun addReminders_EmptyRetrieveEvent() {
        //retrive the saved data
        remindersListViewModel.loadReminders()

        var reminders = remindersListViewModel.remindersList.getOrAwaitValue()

        MatcherAssert.assertThat(
            reminders, CoreMatchers.`is`(emptyList())
        )

    }

    @ExperimentalCoroutinesApi
    @Test
    fun addReminder_RetrieveItEvent() {
        // When adding a new reminder
        var reminderToSave = ReminderDTO(
            "Test Reminder",
            "Reminder description",
            null, null, null
        )
        runBlockingTest {
            dataSource.saveReminder(reminderToSave)
        }
        //retrive the saved data
        remindersListViewModel.loadReminders()

        var reminders = remindersListViewModel.remindersList.getOrAwaitValue()
        val dataList = ArrayList<ReminderDTO>()
        dataList.addAll((reminders).map { reminder ->
            ReminderDTO(
                reminder.title,
                reminder.description,
                reminder.location,
                reminder.latitude,
                reminder.longitude,
                reminder.id
            )
        })

        MatcherAssert.assertThat(
            dataList, CoreMatchers.`is`(dataSource.remindersList)
        )

    }

    @Test
    fun getReminders_ShowLoadingTest() {
        mainCoroutineRule.pauseDispatcher()
        remindersListViewModel.loadReminders()
        var value = remindersListViewModel.showLoading.getOrAwaitValue()

        MatcherAssert.assertThat(
            value, CoreMatchers.`is`(true)
        )
        mainCoroutineRule.resumeDispatcher()
        var value2 = remindersListViewModel.showLoading.getOrAwaitValue()

        MatcherAssert.assertThat(
            value2, CoreMatchers.`is`(false)
        )
    }


    @Test
    fun getReminders_ShowErrorTest() {
        dataSource.setReturnError(true)
        remindersListViewModel.loadReminders()
        var value = remindersListViewModel.showSnackBar.getOrAwaitValue()

        MatcherAssert.assertThat(
            value, CoreMatchers.`is`("No reminder found")
        )

    }


}