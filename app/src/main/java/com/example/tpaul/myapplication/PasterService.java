package com.example.tpaul.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class PasterService extends AccessibilityService {
    public static PasterService instance;
    public static String currentItem = "";

    public static void SetItem(String item) {
        currentItem = item;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(instance, "SetItem called with: " + item, duration);
        toast.show();

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

        if (currentItem == "")
            return;
        int duration = Toast.LENGTH_SHORT;
        Toast toast;
        //toast = Toast.makeText(this, "currentItem: " + currentItem, duration);
        //toast.show();

        AccessibilityNodeInfo source = accessibilityEvent.getSource();

        if (source != null) {

            AccessibilityNodeInfo rowNode = getRootInActiveWindow();
            if (rowNode != null) {
                toast = Toast.makeText(this, "Found children: " + rowNode.getChildCount(), duration);
                toast.show();

                for (int i = 0; i < rowNode.getChildCount(); i++) {
                    AccessibilityNodeInfo edit = rowNode.getChild(i);
                    if (edit.isEditable() && edit.isFocused()) {
                        toast = Toast.makeText(this, "Found editable: " + edit.getText(), duration);
                        toast.show();

                        Bundle b = new Bundle();
                        b.putString(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, currentItem);
                        edit.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, b);
                        currentItem = "";
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;

        ListItemNotification notification = new ListItemNotification();
        notification.notify(this, "1", "PasterService started", 1);

    }
    @Override
    public boolean onUnbind(Intent intent) {
        instance = null;
        return super.onUnbind(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

}