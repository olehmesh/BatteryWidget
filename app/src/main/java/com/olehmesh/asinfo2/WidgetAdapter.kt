package com.olehmesh.asinfo2

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.widget.RemoteViews
import android.widget.RemoteViewsService.RemoteViewsFactory
import java.util.*

class WidgetAdapter internal constructor(var context: Context, intent: Intent?) :
    RemoteViewsFactory {
    var list: ArrayList<String>? = null
    override fun onCreate() {
        list = ArrayList()
    }

    override fun getCount(): Int {
        return list!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(
            context.packageName,
            R.layout.list_item
        )
        remoteViews.setTextViewText(R.id.itemText, list!![position])
        val clickIntent = Intent()
        clickIntent.putExtra(MyProvider.ITEM_POSITION, position)
        remoteViews.setOnClickFillInIntent(R.id.itemText, clickIntent)
        return remoteViews
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDataSetChanged() {
        list!!.clear()
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val mHealth = intent!!.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
        list!!.add("Battery health: " + convertHealth(mHealth))
        val mTemp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
        val temp = mTemp / 10.0f
        list!!.add("Temperature: $temp °C")
        val mVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        val tempTurn = mVoltage / 1000.0f
        list!!.add("Voltage: $tempTurn V")
        val current = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
        val currTurn = current / 1000.0f
        list!!.add("Current: $currTurn mA")
    }

    private fun convertHealth(health: Int): String {
        val result: String
        result = when (health) {
            BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over voltage"
            BatteryManager.BATTERY_HEALTH_UNKNOWN -> "Unknown"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Unspecified failure"
            else -> "Unknown"
        }
        return result
    }

    override fun onDestroy() {}

}