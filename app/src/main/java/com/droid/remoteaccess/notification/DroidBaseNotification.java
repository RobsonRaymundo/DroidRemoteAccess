package com.droid.remoteaccess.notification;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

/**
 * Created by Robson on 16/02/2016.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class DroidBaseNotification extends NotificationListenerService {
    public DroidBaseNotification() {
        super();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationPosted(sbn, rankingMap);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationRemoved(sbn, rankingMap);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }

    @Override
    public void onNotificationRankingUpdate(RankingMap rankingMap) {
        super.onNotificationRankingUpdate(rankingMap);
    }

    @Override
    public void onListenerHintsChanged(int hints) {
        super.onListenerHintsChanged(hints);
    }

    @Override
    public void onInterruptionFilterChanged(int interruptionFilter) {
        super.onInterruptionFilterChanged(interruptionFilter);
    }

    @Override
    public StatusBarNotification[] getActiveNotifications() {
        return super.getActiveNotifications();
    }

    @Override
    public StatusBarNotification[] getActiveNotifications(String[] keys) {
        return super.getActiveNotifications(keys);
    }

    @Override
    public RankingMap getCurrentRanking() {
        return super.getCurrentRanking();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
