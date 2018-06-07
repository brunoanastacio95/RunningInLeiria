package pt.ipleiria.markmyrhythm.Model;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import pt.ipleiria.markmyrhythm.Activitty.NewChallengeActivity;
import pt.ipleiria.markmyrhythm.R;

public class NotificationAlarm extends BroadcastReceiver
{
    public static volatile NotificationAlarm instance = null;
    private static String channelId = "2";
    private static NotificationCompat.Builder mBuilder;

    synchronized public static NotificationAlarm getInstance() {
        if(instance == null) {
            instance = new NotificationAlarm();
        }
        return instance;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "NOTIFICATION FIRED !!!", Toast.LENGTH_SHORT).show();
        Log.i("Notification Fired", "Notification Fired");
        this.createNotification(context);
    }

    public void cancelNotification(Context context)
    {
        Intent intent = new Intent(context, NotificationAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(sender);
        }
    }

    /**
     * Schedule Alarm in the specified time after the current time
     * @param context Application Context
     * @param //hours Hours after the current time is going to ring the Alarm
     */
    public void scheduleAlarm(Context context)
    {
        System.out.println("FUCKING: " + Singleton.getInstance().getLastActivityHour());
        int hours = 5;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MILLISECOND, hours*1000);
       // calendar.add(Calendar.MILLISECOND, 5000); // Hour-DAY
        // Actual time plus y + hour in milliseconds
        // long millis = System.currentTimeMillis() + (hours * 1000) ; //* 60 * 60
        Intent intentAlarm = new Intent(context, NotificationAlarm.class);
        // Get the Alarm Service.
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(alarmManager != null){
            // Set the alarm for a particular time.
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), hours*1000, PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT) );
        }
        Log.i("Notification Scheduled", "Notification Scheduled");
        Toast.makeText(context, "Notification Scheduled !!!", Toast.LENGTH_SHORT).show();
    }


    public void createNotification(Context context){
        createNotificationChannel(context);

        mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_run)
                .setContentTitle("Running in Leiria")
                .setContentText("A semana passada fez exercício às "+ Singleton.getInstance().getLastActivityHour() + " horas faça hoje também.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("A semana passada fez exercício às "+  Singleton.getInstance().getLastActivityHour() + " horas faça hoje também."))
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        int notificationId = NotificationID.getID();
        notificationManager.notify(notificationId, mBuilder.build());

        cancelNotification(context);
    }

    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Running in Leiria APP Notifications"; //context.getString(R.string.channel_name);
            String description = "Notifications description"; //context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}