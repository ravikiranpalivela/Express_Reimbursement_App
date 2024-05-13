package com.tekskills.er_tekskills.presentation.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.presentation.br.AlarmReceiver
import com.tekskills.er_tekskills.data.model.CategoryInfo
import com.tekskills.er_tekskills.data.model.TaskInfo
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

open class ParentFragment : Fragment() {

    fun deleteTask(viewModel: MainActivityViewModel, taskInfo: TaskInfo, categoryInfo : CategoryInfo){
        CoroutineScope(Dispatchers.Main).launch {
            if(viewModel.getCountOfCategory(categoryInfo.categoryInformation)==1) {
                viewModel.deleteTaskAndCategory(taskInfo, categoryInfo)
            }else {
                viewModel.deleteTask(taskInfo)
            }
            val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(requireContext(), AlarmReceiver::class.java)
            intent.putExtra("task_info", taskInfo)
            val pendingIntent = PendingIntent.getBroadcast(requireContext(), taskInfo.id, intent, PendingIntent.FLAG_IMMUTABLE)
            alarmManager.cancel(pendingIntent)
        }
    }

//    fun updateTaskStatus(viewModel: MainActivityViewModel, taskInfo: TaskInfo) {
//        viewModel.updateTaskStatus(taskInfo)
//        lifecycleScope.launch(Dispatchers.IO) {
//            val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//            val intent = Intent(requireContext(), AlarmReceiver::class.java)
//            intent.putExtra("task_info", taskInfo)
//            val pendingIntent = PendingIntent.getBroadcast(requireContext(), taskInfo.id, intent, PendingIntent.FLAG_IMMUTABLE)
//
//            if(taskInfo.status){
//                alarmManager.cancel(pendingIntent)
//            }else {
//                val date = Date()
//                if(taskInfo.date > date && taskInfo.date.seconds == 5){
//                    val mainActivityIntent = Intent(requireContext(), MainActivity::class.java)
//                    val basicPendingIntent = PendingIntent.getActivity(requireContext(), taskInfo.id, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)
//                    val clockInfo = AlarmManager.AlarmClockInfo(taskInfo.date.time, basicPendingIntent)
//                    alarmManager.setAlarmClock(clockInfo, pendingIntent)
//                }
//            }
//        }
//    }

}