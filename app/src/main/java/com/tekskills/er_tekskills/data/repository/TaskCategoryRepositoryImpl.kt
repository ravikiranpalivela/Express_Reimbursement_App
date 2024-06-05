package com.tekskills.er_tekskills.data.repository

import androidx.lifecycle.LiveData
import com.tekskills.er_tekskills.data.db.TaskCategoryDao
import com.tekskills.er_tekskills.domain.TaskCategoryRepository
import com.tekskills.er_tekskills.data.model.CategoryInfo
import com.tekskills.er_tekskills.data.model.NoOfTaskForEachCategory
import com.tekskills.er_tekskills.data.model.TaskCategoryInfo
import com.tekskills.er_tekskills.data.model.TaskInfo
import com.tekskills.er_tekskills.utils.AppUtil.isWithinOutsideRange
import com.tekskills.er_tekskills.utils.AppUtil.isWithinRange
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class TaskCategoryRepositoryImpl @Inject constructor(private val taskCategoryDao: TaskCategoryDao) :
    TaskCategoryRepository {

    override suspend fun updateTaskStatus(task: TaskInfo): Int {
        return taskCategoryDao.updateTaskStatus(task)
    }

    override suspend fun deleteTask(task: TaskInfo) {
        taskCategoryDao.deleteTask(task)
    }

    override suspend fun insertTaskAndCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo) {
        taskCategoryDao.insertTaskAndCategory(taskInfo, categoryInfo)
    }

    override suspend fun deleteTaskAndCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo) {
        taskCategoryDao.deleteTaskAndCategory(taskInfo, categoryInfo)
    }

    override suspend fun updateTaskAndAddDeleteCategory(
        taskInfo: TaskInfo,
        categoryInfoAdd: CategoryInfo,
        categoryInfoDelete: CategoryInfo
    ) {
        taskCategoryDao.updateTaskAndAddDeleteCategory(
            taskInfo,
            categoryInfoAdd,
            categoryInfoDelete
        )
    }

    override suspend fun updateTaskAndAddCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo) {
        taskCategoryDao.updateTaskAndAddCategory(taskInfo, categoryInfo)
    }

    override fun getUncompletedTask(): LiveData<List<TaskCategoryInfo>> =
        taskCategoryDao.getUncompletedTask()

    override fun getCompletedTask(): LiveData<List<TaskCategoryInfo>> =
        taskCategoryDao.getCompletedTask()

    override fun getUncompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>> =
        taskCategoryDao.getUncompletedTaskOfCategory(category)

    override fun getCompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>> =
        taskCategoryDao.getCompletedTaskOfCategory(category)

    override fun getNoOfTaskForEachCategory(): LiveData<List<NoOfTaskForEachCategory>> =
        taskCategoryDao.getNoOfTaskForEachCategory()

    override fun getCategories(): LiveData<List<CategoryInfo>> = taskCategoryDao.getCategories()
    override suspend fun getCountOfCategory(category: String): Int =
        taskCategoryDao.getCountOfCategory(category)

    override suspend fun getActiveAlarms(currentTime: Date): List<TaskInfo> {
        var list: List<TaskInfo>
        coroutineScope {
            list = withContext(IO) { taskCategoryDao.getActiveAlarms(currentTime) }
        }
        return list
    }

    override suspend fun insertOrUpdateTaskInfo(taskInfo: TaskInfo) {
        val existingTask = taskCategoryDao.getTaskInfoById(taskInfo.TaskID, taskInfo.id)
        if (existingTask == null) {
            taskCategoryDao.insertTask(taskInfo)
        } else {
            taskCategoryDao.updateTaskStatus(taskInfo)
        }
    }

    override suspend fun getRangeItems(
        destinationLatitude: Double,
        destinationLongitude: Double,
        radius: Double
    ): List<TaskInfo> {
        val allTaskInfo = taskCategoryDao.getAllTaskInfo()
        return allTaskInfo.filter {
            isWithinRange(
                destinationLatitude,
                destinationLongitude,
                it.destinationLatitude.toDouble(),
                it.destinationLongitude.toDouble(),
                1000F
            )
//            < radius
        }
    }

    override suspend fun getOutsideRangeItems(
        destinationLatitude: Double,
        destinationLongitude: Double,
        radius: Double
    ): List<TaskInfo> {
        val allTaskInfo = taskCategoryDao.getAllTaskInfo()
        return allTaskInfo.filter {
            isWithinOutsideRange(
                destinationLatitude,
                destinationLongitude,
                it.destinationLatitude.toDouble(),
                it.destinationLongitude.toDouble(), radius.toFloat()
            )
//            > radius
        }
    }

    private fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val radius = 6371 // Radius of the Earth in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radius * c
    }
}