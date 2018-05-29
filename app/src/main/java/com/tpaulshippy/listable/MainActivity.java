package com.tpaulshippy.listable;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent startListNavigator = new Intent(this, ListNavigatorService.class);
        startListNavigator.putExtra(Intent.EXTRA_TEXT,
                "Item1" +System.lineSeparator() +
                "Item2" + System.lineSeparator() +
                "Item3" + System.lineSeparator() +
                "Item4" + System.lineSeparator() +
                "Item5");
        startService(startListNavigator);

        GetIntent();
    }

    public void showNotification(View view)
    {
        notify("1");
    }

    private void notify(String text)
    {
        ListItemNotification notification = new ListItemNotification();
        notification.notify(this, "1", text, 1);

    }

    private void GetIntent() {
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }
        else {
            // Handle other intents, such as being started from the home screen
        }
    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            TextView tv = (TextView)this.findViewById(R.id.textView);
            tv.setText(sharedText);
            Intent startListNavigator = new Intent(this, ListNavigatorService.class);
            startListNavigator.putExtra(Intent.EXTRA_TEXT,
                    sharedText);
            startService(startListNavigator);


        }
    }
}
