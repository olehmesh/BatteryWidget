package com.olehmesh.asinfo2

import android.content.Intent
import android.widget.RemoteViewsService

class MyService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WidgetAdapter(applicationContext, intent)
    }
}