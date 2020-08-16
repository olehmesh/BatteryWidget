package com.olehmesh.asinfo2

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import java.util.*

class MyProvider : AppWidgetProvider() {
    val LOG_TAG = "myLogs"
    val ONCLICK_ACTION = "onclick"
    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val remoteViews = RemoteViews(
            context.packageName,
            R.layout.widget
        )
        val intentAdapter = Intent(context, MyService::class.java)
        intentAdapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds)
        remoteViews.setRemoteAdapter(R.id.listView, intentAdapter)
        setListClick(remoteViews, context)
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews)
        appWidgetManager.notifyAppWidgetViewDataChanged(
            appWidgetIds,
            R.id.listView
        )
        Log.d(LOG_TAG, "onUpdate " + appWidgetIds.contentToString())
    }

    private fun setListClick(rv: RemoteViews, context: Context?) {
        val listClickIntent = Intent(context, MyProvider::class.java)
        listClickIntent.action = ONCLICK_ACTION
        val listClickPIntent = PendingIntent.getBroadcast(
            context, 0,
            listClickIntent, 0
        )
        rv.setPendingIntentTemplate(R.id.listView, listClickPIntent)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (Objects.requireNonNull(intent.action).equals(ONCLICK_ACTION, ignoreCase = true)) {
            val thisAppWidget = ComponentName(
                context.packageName, javaClass.name
            )
            val appWidgetManager = AppWidgetManager
                .getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(thisAppWidget)
            for (appWidgetID in ids) {
                onUpdate(context, appWidgetManager, intArrayOf(appWidgetID))
            }
        }
    }

    companion object {
        const val ITEM_POSITION = "item_position"
    }
}