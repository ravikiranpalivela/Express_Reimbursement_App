package com.tekskills.er_tekskills.domain

import androidx.lifecycle.LiveData
import com.tekskills.er_tekskills.data.model.CategoryInfo
import com.tekskills.er_tekskills.data.model.NoOfTaskForEachCategory
import com.tekskills.er_tekskills.data.model.TaskCategoryInfo
import com.tekskills.er_tekskills.data.model.TaskInfo
import java.util.*

interface TaskCategoryRepository {
    suspend fun updateTaskStatus(task: TaskInfo) : Int
    suspend fun deleteTask(task: TaskInfo)
    suspend fun insertTaskAndCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo)
    suspend fun deleteTaskAndCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo)
    suspend fun updateTaskAndAddDeleteCategory(taskInfo: TaskInfo, categoryInfoAdd: CategoryInfo, categoryInfoDelete: CategoryInfo)
    suspend fun updateTaskAndAddCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo)
    fun getUncompletedTask(): LiveData<List<TaskCategoryInfo>>
    fun getCompletedTask(): LiveData<List<TaskCategoryInfo>>
    fun getUncompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>>
    fun getCompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>>
    fun getNoOfTaskForEachCategory(): LiveData<List<NoOfTaskForEachCategory>>
    fun getCategories(): LiveData<List<CategoryInfo>>
    suspend fun getCountOfCategory(category: String) : Int
    suspend fun getActiveAlarms(currentTime : Date) : List<TaskInfo>

    suspend fun insertOrUpdateTaskInfo(taskInfo: TaskInfo)

    suspend fun getRangeItems(
        destinationLatitude: Double,
        destinationLongitude: Double,
        radius: Double
    ): List<TaskInfo>

    suspend fun getOutsideRangeItems(
        destinationLatitude: Double,
        destinationLongitude: Double,
        radius: Double
    ): List<TaskInfo>

}