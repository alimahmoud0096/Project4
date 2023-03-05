package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

//    TODO: Add testing implementation to the RemindersLocalRepository.kt

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var remindersDatabase: RemindersDatabase
    private lateinit var locaDataSource: ReminderDataSource


    @Before
    fun setupDbAndDataSource() {
        remindersDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
        locaDataSource=RemindersLocalRepository(remindersDatabase.reminderDao(),Dispatchers.Main)
    }



    @Test
    fun insertReminder_GetITtByID()= runBlocking{

        var reminderToSave = ReminderDTO(
            "Test Reminder",
            "Reminder description",
            null, null, null
        )

        locaDataSource.saveReminder(reminderToSave)

        var value=locaDataSource.getReminder(reminderToSave.id) as Result.Success

        assertThat(
            value.data, Is.`is`(reminderToSave)
        )
    }

    @Test
    fun deleteAllReminder_GetEmptyReminders()= runBlocking{

        var reminderToSave = ReminderDTO(
            "Test Reminder",
            "Reminder description",
            null, null, null
        )
        locaDataSource.saveReminder(reminderToSave)
        locaDataSource.deleteAllReminders()

        var value=locaDataSource.getReminders() as Result.Success

        assertThat(
            value.data, Is.`is`(emptyList())
        )
    }

    @After
    fun closeDb(){
        remindersDatabase.close()
    }


}