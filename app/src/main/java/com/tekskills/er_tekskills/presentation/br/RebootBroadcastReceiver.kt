package com.tekskills.er_tekskills.presentation.br

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tekskills.er_tekskills.data.model.TaskInfo
//import com.tekskills.er_tekskills.data.repository.TaskCategoryRepositoryImpl
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
//
//@AndroidEntryPoint
//class RebootBroadcastReceiver : BroadcastReceiver(){
//    @Inject
////    lateinit var repository: TaskCategoryRepositoryImpl
////    override fun onReceive(context: Context?, p1: Intent?) {
////        val time = Date()
////        CoroutineScope(Main).launch {
//////            val list = repository.getActiveAlarms(time)
//////            for(taskInfo in list) setAlarm(taskInfo, context)
////        }
////    }
//
////    private fun setAlarm(taskInfo: TaskInfo, context: Context?){
////        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
////        val intent = Intent(context, AlarmReceiver::class.java)
////        intent.putExtra("task_info", taskInfo)
////        val pendingIntent = PendingIntent.getBroadcast(context, taskInfo.id, intent, PendingIntent.FLAG_IMMUTABLE)
////        val mainActivityIntent = Intent(context, MainActivity::class.java)
////        val basicPendingIntent = PendingIntent.getActivity(context, taskInfo.id, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)
////        val clockInfo = AlarmManager.AlarmClockInfo(taskInfo.date.time, basicPendingIntent)
//////        alarmManager.setAlarmClock(clockInfo, pendingIntent)
////    }
//
//}