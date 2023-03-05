package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminders.utils.KoinAfter
import com.udacity.project4.locationreminders.utils.MainCoroutineRule
import com.udacity.project4.locationreminders.utils.getOrAwaitValue

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest :KoinAfter(){


    //TODO: provide testing to the SaveReminderView and its live data objects
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private var dataSource = FakeDataSource()
    lateinit var saveReminderViewModel: SaveReminderViewModel

    @Before
    fun setupViewModel() {
        saveReminderViewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), dataSource)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun addReminder_ShowLoadingTest() {
        // When adding a new reminder
        var reminderToSave = ReminderDataItem(
            "Test Reminder",
            "Reminder description",
            null, null, null
        )

        mainCoroutineRule.pauseDispatcher()
        saveReminderViewModel.saveReminder(reminderToSave)


        var isloading = saveReminderViewModel.showLoading.getOrAwaitValue()


        MatcherAssert.assertThat(
            isloading, `is`(true)
        )
        mainCoroutineRule.resumeDispatcher()
        var isloading2 = saveReminderViewModel.showLoading.getOrAwaitValue()

        MatcherAssert.assertThat(
            isloading2, `is`(false)
        )
    }


    @ExperimentalCoroutinesApi
    @Test
    fun addReminder_TitleShowErrorTest() {
        // When adding a new reminder
        var reminderToSave = ReminderDataItem(
            "",
            "Reminder description",
            null, null, null
        )

        saveReminderViewModel.validateEnteredData(reminderToSave)


        var error = saveReminderViewModel.showSnackBarInt.getOrAwaitValue()


        MatcherAssert.assertThat(
            error, CoreMatchers.`is`(R.string.err_enter_title)
        )

    }


    fun addReminder_LocationShowErrorTest() {
        // When adding a new reminder
        var reminderToSave = ReminderDataItem(
            "title",
            "Reminder description",
            null, null, null
        )

        saveReminderViewModel.validateEnteredData(reminderToSave)


        var error = saveReminderViewModel.showSnackBarInt.getOrAwaitValue()


        MatcherAssert.assertThat(
            error, CoreMatchers.`is`(R.string.err_select_location)
        )

    }


//    @After
//    fun cacnelKoin(){
//        stopKoin()
//    }

}