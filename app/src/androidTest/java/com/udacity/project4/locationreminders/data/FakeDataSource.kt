package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var remindersList: MutableList<ReminderDTO>? = mutableListOf()) :
    ReminderDataSource {

//    TODO: Create a fake data source to act as a double to the real data source

    fun setReturnError(returenError:Boolean){
        if(returenError){
            remindersList=null
        }else{
            remindersList= mutableListOf()
        }
    }
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        remindersList?.let { return Result.Success(it) }
        return Result.Error("No reminder found")
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        remindersList?.let { it.add(reminder) }
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        var reminder = remindersList?.firstOrNull { it.id.equals(id) }
        reminder?.let {
            return Result.Success(reminder)
        }
        return Result.Error("No reminder found")
    }

    override suspend fun deleteAllReminders() {
        remindersList?.clear()
    }


}