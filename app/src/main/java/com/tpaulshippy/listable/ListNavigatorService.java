package com.tpaulshippy.listable;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ListNavigatorService extends Service {
    public ListNavigatorService() {
    }

    private static String _logTag = "LISTABLE";
    private int _currentIndex = 0;
    private ArrayList<String> _items = new ArrayList<String>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        android.util.Log.d(_logTag, "ListNavigator onBind");

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        android.util.Log.d(_logTag, "ListNavigator onStartCommand");

        if (intent.hasExtra(Intent.EXTRA_TEXT)) {

            String text = intent.getStringExtra(Intent.EXTRA_TEXT);

            if (text.contains(System.lineSeparator())) {
                SetItems(text);
                _currentIndex = 0;
            }
            if (_items.size() > 0) {
                if (_items.size() > _currentIndex) {
                    String currentItem = _items.get(_currentIndex);

                    if (_currentIndex > 0) {
                        String previousItem = _items.get(_currentIndex - 1);
                        FillService.SetItem(previousItem);
                    }
                    ListItemNotification notification = new ListItemNotification();
                    notification.notify(this, "1", currentItem, _currentIndex);

                    _currentIndex++;

                }
            }
        }

//        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    private void SetItems(String text) {
        _items = new ArrayList<String>(Arrays.asList(text.split(System.lineSeparator())));
    }


}
