package com.olehmesh.asinfo2;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Arrays;

public class MyProvider extends AppWidgetProvider {

    final String LOG_TAG = "myLogs";
    final static String ITEM_POSITION = "item_position";
    final String ONCLICK_ACTION = "onclick";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget);

            Intent intentAdapter = new Intent(context, MyService.class);
            intentAdapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
            remoteViews.setRemoteAdapter(R.id.listView, intentAdapter);

            setListClick(remoteViews, context);

            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,
                    R.id.listView);

        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));

        }

    void setListClick(RemoteViews rv, Context context) {
        Intent listClickIntent = new Intent(context, MyProvider.class);
        listClickIntent.setAction(ONCLICK_ACTION);
        PendingIntent listClickPIntent = PendingIntent.getBroadcast(context, 0,
                listClickIntent, 0);
        rv.setPendingIntentTemplate(R.id.listView, listClickPIntent);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equalsIgnoreCase(ONCLICK_ACTION)) {
            ComponentName thisAppWidget = new ComponentName(
                    context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID : ids) {
                onUpdate(context, appWidgetManager, new int[]{appWidgetID});

            }
        }
    }

    }
