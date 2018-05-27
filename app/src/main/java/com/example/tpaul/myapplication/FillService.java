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

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class FillService extends AccessibilityService {
    public static FillService instance;
    public static String currentItem = "";
    private static String _logTag = "LISTABLE";

    public static void SetItem(String item) {
        currentItem = item;
        android.util.Log.d(_logTag, "currentItem set to: " + currentItem);

    }

    interface NodeCondition {
        boolean check(AccessibilityNodeInfo n);
    }

    class WindowIdCondition implements NodeCondition {
        private int id;

        public WindowIdCondition(int id) {
            this.id = id;
        }

        @Override
        public boolean check(AccessibilityNodeInfo n) {
            return n.getWindowId() == id;
        }
    }

    private class EditTextCondition implements NodeCondition {
        @Override
        public boolean check(AccessibilityNodeInfo n) {
            //it seems like n.Editable is not a good check as this is false for some fields which are actually editable, at least in tests with Chrome.
            return (n.getClassName() != null) && (n.getClassName().toString().toLowerCase().contains("edittext"));
        }
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        android.util.Log.d(_logTag, "OnAccEvent");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            android.util.Log.d(_logTag, "AndroidVersion not supported");
            return;
        }

        handleAccessibilityEvent(event);

/*
        android.util.Log.d(_logTag, "accessibilityEvent hit, currentItem: " + currentItem);

        AccessibilityNodeInfo source = event.getSource();

        if (source != null) {

            AccessibilityNodeInfo rowNode = getRootInActiveWindow();
            if (rowNode != null) {

                for (int i = 0; i < rowNode.getChildCount(); i++) {
                    AccessibilityNodeInfo edit = rowNode.getChild(i);
                    if (edit.isEditable() && edit.isFocused()) {

                        Bundle b = new Bundle();
                        b.putString(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, currentItem);
                        edit.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, b);
                        currentItem = "";
                        return;
                    }
                }
            }
        }
        */
    }

    @Override
    public void onCreate() {

        android.util.Log.d(_logTag, "FillService onCreate");
        super.onCreate();
    }

    private void handleAccessibilityEvent(AccessibilityEvent event) {
        try {
            android.util.Log.d(_logTag, "currentItem: " + currentItem);

            if (currentItem == "")
                return;

            android.util.Log.d(_logTag, "event: " + event.getEventType());

            //if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
            CharSequence packageName = event.getPackageName();
            android.util.Log.d(_logTag, "event: " + event.getEventType() + ", package = " + packageName);
            //if (isLauncherPackage(event.getPackageName())) {
            //    android.util.Log.d(_logTag, "return.");
            //    return; //avoid that the notification is cancelled when pulling down notif drawer
            //} else {
            //    android.util.Log.d(_logTag, "event package is no launcher");
            //}
            AccessibilityNodeInfo root = getRootInActiveWindow();

            int eventWindowId = event.getWindowId();
            if ((ExistsNodeOrChildren(root, new WindowIdCondition(eventWindowId)))) {
                boolean cancelNotification = true;

                if (ExistsNodeOrChildren(root, new EditTextCondition())) {

                    List<AccessibilityNodeInfo> allEditTexts = new ArrayList<>();
                    GetNodeOrChildren(root, new EditTextCondition(), allEditTexts);

                    AccessibilityNodeInfo firstEdit = null;
                    for (int i = 0; i < allEditTexts.size(); i++) {
                        if (allEditTexts.get(i).isPassword() == false) {
                            firstEdit = allEditTexts.get(i);
                            android.util.Log.d(_logTag, "setting firstEdit = " + firstEdit.getText() + " ");
                            break;
                        } else break;
                    }

                    fillDataInTextField(firstEdit, currentItem);
                }

            }
            //if (cancelNotification) {
            //((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(autoFillNotificationId);
            //android.util.Log.d(_logTag, "Cancel notif");
            //}
            //}


        } catch (Exception e) {
            android.util.Log.e(_logTag, (e.toString() == null) ? "(null)" : e.toString());

        }
    }

    private void fillDataInTextField(AccessibilityNodeInfo edit, String value) {
        if ((value == null) || (edit == null))
            return;
        Bundle b = new Bundle();
        b.putString(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, value);
        edit.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, b);
    }

    private void GetNodeOrChildren(AccessibilityNodeInfo n, NodeCondition condition, List<AccessibilityNodeInfo> result) {
        if (n != null) {
            if (condition.check(n))
                result.add(n);
            for (int i = 0; i < n.getChildCount(); i++) {
                GetNodeOrChildren(n.getChild(i), condition, result);
            }
        }
    }

    private boolean ExistsNodeOrChildren(AccessibilityNodeInfo n, NodeCondition condition) {
        if (n == null) return false;
        if (condition.check(n))
            return true;
        for (int i = 0; i < n.getChildCount(); i++) {
            if (ExistsNodeOrChildren(n.getChild(i), condition))
                return true;
        }
        return false;
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;

        android.util.Log.d(_logTag, "FillService onServiceConnected");

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

        android.util.Log.d(_logTag, "FillService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

}