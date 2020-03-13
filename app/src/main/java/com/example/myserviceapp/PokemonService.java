package com.example.myserviceapp;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class PokemonService extends Service {

    private Pokemon[] _pokemon = {new Pokemon("Pikachu", R.drawable.pikachu)
            , new Pokemon("Dragonite", R.drawable.dragonite)
            , new Pokemon("Bulbasaur", R.drawable.bulbasaur)};
    private static final int POKEMON_NOTIFICATION = 1;
    private static final String CHANNEL_ID = "pokemon";
    private int favorite;
    private int notificationId = 0;
    public PokemonService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(POKEMON_NOTIFICATION, getNotification());
    }

    @Override
    public void sendBroadcast(Intent intent) {
        super.sendBroadcast(intent);
    }

    private Notification getNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Pokemon")
                .setChannelId(CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle("New favorite Pokemon!!")
                .setContentText(_pokemon[favorite].name)
                .setSmallIcon(_pokemon[favorite].image);
        return builder.build();
    }

    private void sendNotification(){
        Notification n = getNotification();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, n);
        //notificationId++;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Handler handler = new Handler();
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                favorite = (favorite + 1 + new Random().nextInt(2)) % 3;
                //sendNotification();
                sendNotification();
                //sendBroadcast();
                Intent intent = new Intent();
                intent.setAction("com.wtf.BROADCAST");
                intent.putExtra("pokename", _pokemon[favorite].name);
                intent.putExtra("pokeimage", _pokemon[favorite].image);
                sendBroadcast(intent);
                //updateWidget();
                int FIVE_SECONDS = 5000;
                handler.postDelayed(this, FIVE_SECONDS);
            }
        });

        return START_NOT_STICKY;
    }
}
