package com.ergnologi.firebaseservicenotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class MyService extends Service {

    private Session session;
    private String username;
    private int counter = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Instansiasi Session
        session = new Session(MyService.this);
        HashMap<String, String> user = session.getUsername();
        username = user.get(Session.un);
        //Instansiasi Firebase
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("terbaru");
        //Instansiasi Query untuk mengambil data paling baru
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatModels chatList = snapshot.getValue(ChatModels.class);
                //Pengecekan Versi Android dan kesamaan username, serta counter
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !chatList.getUsername().equals(username) && counter > 1) {
                    int imp = NotificationManager.IMPORTANCE_HIGH;
                    //Instansiasi Notifikasi Versi Android O keatas
                    NotificationChannel notificationChannel = new NotificationChannel("ergnologi", "notif", imp);
                    Notification.Builder notifBuilder = new Notification.Builder(getApplicationContext(),
                            "ergnologi").setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("Pesan baru dari " + chatList.getUsername())
                            .setContentText(chatList.getChat())
                            .setContentInfo(chatList.getChat())
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setPriority(Notification.PRIORITY_DEFAULT);
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.createNotificationChannel(notificationChannel);
                    notificationManager.notify(0, notifBuilder.build());
                } else if (!Objects.equals(chatList.getUsername(), username) && counter > 1) {
                    //Instansiasi Notifikasi Versi Android Nougat Kebawah
                    NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentInfo(chatList.getChat())
                            .setContentText(chatList.getChat())
                            .setContentTitle("Pesan baru dari " + chatList.getUsername())
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, notifBuilder.build());
                }
                counter = counter + 1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopSelf();
        try {
            Thread.sleep(100);
        } catch (InterruptedException a) {
            a.printStackTrace();
        }
        super.onTaskRemoved(rootIntent);
    }
}
