package com.example.tpaul.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class CopyMessageService extends Service {
    public CopyMessageService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        android.util.Log.d("LISTABLE", "CopyMessageService called");

        if (intent.hasExtra(Intent.EXTRA_TEXT)) {

            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            com.example.tpaul.myapplication.PasterService.SetItem(text);

        }
        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

}
