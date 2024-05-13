package com.tekskills.er_tekskills.Configration;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.tekskills.er_tekskills.data.util.Constants;

import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.MalformedURLException;
import javax.net.ssl.SSLContext;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;


/**
 * Created by RK
 **/
public class MySocket {
    private String multimedia, DeviceId, Date, Time, Extenstion, Lat, Lon, ContactNo, ccno, CallDuration;
    private String BDMNAME;
    public String share_date, share_time, share_duration, share_contact;
    public static SocketIO socket = null;
    Context myContext;
    TelephonyManager manager;
    DataOutputStream dataOutputStream = null;
    DataInputStream dataInputStream = null;

    SharedPreferences mySharedPreferences;
    SharedPreferences sharePref_callLog;
    SharedPreferences.Editor editor;

    public MySocket(Context c, String mediyaFile, String deviceId, String date, String time,
                    String extenstion, String lat, String lon, String contactNo, String callDuration) {
        multimedia = mediyaFile;
        DeviceId = deviceId;
        Date = date;
        Time = time;
        Extenstion = extenstion;
        Lat = lat;
        Lon = lon;
        ContactNo = contactNo;
        CallDuration = callDuration;
        myContext = c;


        manager = (TelephonyManager) myContext.getSystemService(Context.TELEPHONY_SERVICE);

        mySharedPreferences = c.getSharedPreferences("MyPref", c.MODE_PRIVATE);
        sharePref_callLog = c.getSharedPreferences("CALLLOG", c.MODE_PRIVATE);
        editor = sharePref_callLog.edit();


        GPSService mGPSService = new GPSService(c);
        mGPSService.getLocation();
        BDMNAME = mySharedPreferences.getString("BDM_Name", null);


        if (!mGPSService.isLocationAvailable) {
            // Here you can ask the user to try again, using return; for that
            Toast.makeText(c, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
        } else {
            Lat = String.valueOf(mGPSService.getLatitude());
            Lon = String.valueOf(mGPSService.getLongitude());
        }

    }

    public void connectSocket() {
       // Toast.makeText(myContext, "SOCKET 1", Toast.LENGTH_SHORT).show();
        try {
            SocketIO.setDefaultSSLSocketFactory(SSLContext.getDefault());
           // Toast.makeText(myContext, "SOCKET SSL", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            socket = new SocketIO(Constants.SOCKET_URL);
          //  Toast.makeText(myContext, "SOCKET IP" + socket, Toast.LENGTH_SHORT).show();

            socket.connect(new IOCallback() {

                @Override
                public void onDisconnect() {
                    System.out.println("disconnected");
                    Toast.makeText(myContext, "SOCKET DISCONNECTED", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onConnect() {
                    System.out.println("connected");
                    Toast.makeText(myContext, "SOCKET CONNECTED", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onMessage(String data, IOAcknowledge ioAcknowledge) {
                }

                @Override
                public void onMessage(JSONObject jsonObject, IOAcknowledge ioAcknowledge) {
                }

                @Override
                public void on(String event, IOAcknowledge ioAcknowledge, Object... objects) {
                }

                @Override
                public void onError(SocketIOException socketIOException) {
                    socketIOException.printStackTrace();
                }
            });
            try {
                JSONObject imageObj = new JSONObject();
                /*if (Extenstion.equals("jpg")) {
                    imageObj.put("extension", "JPG");
                    imageObj.put("mmsType", "IMAGE");
                    imageObj.put("Date", Date);
                    imageObj.put("Time", Date + " " + Time);
                    imageObj.put("BDM_Name", BDMNAME);
                    imageObj.put("image", multimedia);
                    imageObj.put("Latitude", Lat);
                    imageObj.put("Longitude", Lon);
                } else*/ /*if (Extenstion.equals("Location")) {
                    imageObj.put("mmsType", "Location");
                    imageObj.put("Date", Date);
                    imageObj.put("Time", Date + " " + Time);
                    imageObj.put("BDM_Name", BDMNAME);
                    imageObj.put("Latitude", Lat);
                    imageObj.put("Longitude", Lon);
                } else*/ if (Extenstion.equals("mp3")) {
                    share_date = sharePref_callLog.getString("date", null);
                    share_time = sharePref_callLog.getString("time", null);
                    share_duration = sharePref_callLog.getString("duration", null);
                    share_contact = sharePref_callLog.getString("mob_number", null);

                    /*share_date = com.tekskills.insight.data.SharedPreferences.getPreferences(myContext,"date");
                    share_time =com.tekskills.insight.data.SharedPreferences.getPreferences(myContext,"time");
                    share_duration = com.tekskills.insight.data.SharedPreferences.getPreferences(myContext,"duration");
                    share_contact = com.tekskills.insight.data.SharedPreferences.getPreferences(myContext,"mob_number");*/

                    Log.d("===values==", "===share_date===" + Date + " " + Time + "==/*Configuration.PHONENO*/==" + "+919900998012"/*Configuration.PHONENO*/
                            + "==share_contact==" + share_contact);


                    Log.d("===values==", "===current values==" + share_date + "==share_time==" + share_time
                            + "==Configuration.CALLDURATION==" + "==number==" + "+919900998012"/*Configuration.PHONENO*/);


                   /* if (share_date == null || share_time == null || share_contact == null) {
                        Log.d("===values==", "===share preff null===");
                        Configuration confi = new Configuration();
                        confi.getCallDetails(myContext);
                        imageObj.put("mmsType", "CallLog");
                        imageObj.put("Date", Date);
                        imageObj.put("Time", Date + " " + Time);
                        imageObj.put("BDM_Name", BDMNAME);
                        imageObj.put("Latitude", Lat);
                        imageObj.put("Longitude", Lon);
                        imageObj.put("CallerPhoneNumber","+919900998012" *//*Configuration.PHONENO*//*);
                        imageObj.put("CallTypeCode", Configuration.CALLTYPE);
                        imageObj.put("CallDuration", Configuration.CALLDURATION);
                        imageObj.put("audio", multimedia);
                        imageObj.put("extension", "mp3" + "");
                        imageObj.put("ContactNumberOfCaller", ContactNo);

                        editor.putString("date", Date);
                        editor.putString("time", Time);
                        editor.putString("duration", Configuration.CALLDURATION);
                        editor.putString("mob_number","+919900998012" *//*Configuration.PHONENO*//*);
                        editor.commit();
                    }else*/  /*if (Configuration.CALLDURATION.equals("0")) {

                        return;

                    } else if (!share_contact.equals("+919900998012"*//*Configuration.PHONENO*//*)) {

                        Log.d("===values==", "===share preff NOT null but number diff===" + ContactNo);
                        Configuration confi = new Configuration();
                        confi.getCallDetails(myContext);
                        imageObj.put("mmsType", "CallLog");
                        imageObj.put("Date", Date);
                        imageObj.put("Time", Date + " " + Time);
                        imageObj.put("BDM_Name", BDMNAME);
                        imageObj.put("Latitude", Lat);
                        imageObj.put("Longitude", Lon);
                        imageObj.put("CallerPhoneNumber","+919900998012" *//*Configuration.PHONENO*//*);
                        imageObj.put("CallTypeCode", Configuration.CALLTYPE);
                        imageObj.put("CallDuration", Configuration.CALLDURATION);
                        imageObj.put("audio", multimedia);
                        imageObj.put("extension", "mp3" + "");
                        imageObj.put("ContactNumberOfCaller", ContactNo);

                        editor.putString("date", Date);
                        editor.putString("time", Time);
                        editor.putString("duration", Configuration.CALLDURATION);
                        editor.putString("mob_number", "+919900998012"*//*Configuration.PHONENO*//*);
                        editor.commit();
                    } else if (!share_date.equals(Date)) {
                        Log.d("===values==", "===Date diff===");
                        Configuration confi = new Configuration();
                        confi.getCallDetails(myContext);
                        imageObj.put("mmsType", "CallLog");
                        imageObj.put("Date", Date);
                        imageObj.put("Time", Date + " " + Time);
                        imageObj.put("BDM_Name", BDMNAME);
                        imageObj.put("Latitude", Lat);
                        imageObj.put("Longitude", Lon);
                        imageObj.put("CallerPhoneNumber","+919900998012" *//*Configuration.PHONENO*//*);
                        imageObj.put("CallTypeCode", Configuration.CALLTYPE);
                        imageObj.put("CallDuration", Configuration.CALLDURATION);
                        imageObj.put("audio", multimedia);
                        imageObj.put("extension", "mp3" + "");
                        imageObj.put("ContactNumberOfCaller", "+919900998012"*//*Configuration.PHONENO*//*);

                        editor.putString("date", Date);
                        editor.putString("time", Time);
                        editor.putString("duration", Configuration.CALLDURATION);
                        editor.putString("mob_number", "+919900998012"*//*Configuration.PHONENO*//*);
                        editor.commit();
                    } else if (!share_time.equals(Time)) {
                        Log.d("===values==", "===Time diff===");

                        String dateStart = share_time.substring(0, 2);
                        String dateStop = Time.substring(0, 2);
                        Log.d("===values==", "===dateStart===" + dateStart + "===dateStop===" + dateStop);

                        if (dateStart.equals(dateStop)) {
                            String min1 = share_time.substring(2, 5);
                            String min2 = Time.substring(2, 5);
                            Log.d("===values==", "===min1===" + min1 + "===min2===" + min2);
                            if (!min1.equals(min2) || !share_duration.equals(Configuration.CALLDURATION)) {

                                Log.d("===values==", "===Minutes are diff");
                                Configuration confi = new Configuration();
                                confi.getCallDetails(myContext);
                                imageObj.put("mmsType", "CallLog");
                                imageObj.put("Date", Date);
                                imageObj.put("Time", Date + " " + Time);
                                imageObj.put("BDM_Name", BDMNAME);
                                imageObj.put("Latitude", Lat);
                                imageObj.put("Longitude", Lon);
                                imageObj.put("CallerPhoneNumber", "+919900998012"*//*Configuration.PHONENO*//*);
                                imageObj.put("CallTypeCode", Configuration.CALLTYPE);
                                imageObj.put("CallDuration", Configuration.CALLDURATION);
                                imageObj.put("audio", multimedia);
                                imageObj.put("extension", "mp3" + "");
                                imageObj.put("ContactNumberOfCaller", "+919900998012"*//*Configuration.PHONENO*//*);

                                editor.putString("date", Date);
                                editor.putString("time", Time);
                                editor.putString("duration", Configuration.CALLDURATION);
                                editor.putString("mob_number", "+919900998012"*//*Configuration.PHONENO*//*);
                                editor.commit();
                            }

                        } else {
                            Log.d("===values==", "===hours are diff");
                            Configuration confi = new Configuration();
                            confi.getCallDetails(myContext);
                            imageObj.put("mmsType", "CallLog");
                            imageObj.put("Date", Date);
                            imageObj.put("Time", Date + " " + Time);
                            imageObj.put("BDM_Name", BDMNAME);
                            imageObj.put("Latitude", Lat);
                            imageObj.put("Longitude", Lon);
                            imageObj.put("CallerPhoneNumber", "+919900998012"*//*Configuration.PHONENO*//*);
                            imageObj.put("CallTypeCode", Configuration.CALLTYPE);
                            imageObj.put("CallDuration", Configuration.CALLDURATION);
                            imageObj.put("audio", multimedia);
                            imageObj.put("extension", "mp3" + "");
                            imageObj.put("ContactNumberOfCaller", "+919900998012"*//*Configuration.PHONENO*//*);

                            editor.putString("date", Date);
                            editor.putString("time", Time);
                            editor.putString("duration", Configuration.CALLDURATION);
                            editor.putString("mob_number", "+919900998012"*//*Configuration.PHONENO*//*);
                            editor.commit();
                        }
                    }else {
                        Log.e("ELSE PART ", "");
                        return;
                    }*/


                } else if (Extenstion.equals("Text")) {
                    imageObj.put("mmsType", "Text");
                    imageObj.put("Date", Date);
                    imageObj.put("Time", Date + " " + Time);
                    imageObj.put("BDM_Name", BDMNAME);
                    imageObj.put("Latitude", Lat);
                    imageObj.put("Longitude", Lon);
                    imageObj.put("Content", multimedia);
                    imageObj.put("MessageSender", ContactNo);
                    //imageObj.put("MessageReceiver", SplashScreenActivity.flag);
                }

                socket.emit("media", imageObj);

            } catch (Exception e) {

                e.printStackTrace();
            }

        } catch (MalformedURLException e1) {

            e1.printStackTrace();
        }
    }
}