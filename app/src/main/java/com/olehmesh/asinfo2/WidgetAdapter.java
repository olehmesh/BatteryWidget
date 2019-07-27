package com.olehmesh.asinfo2;

import java.util.ArrayList;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class WidgetAdapter implements RemoteViewsFactory {

    ArrayList<String> list;
    Context context;

    WidgetAdapter(Context ctx, Intent intent) {
        context = ctx;
    }

    @Override
    public void onCreate() {
        list = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.list_item);
        remoteViews.setTextViewText(R.id.itemText, list.get(position));

        Intent clickIntent = new Intent();
        clickIntent.putExtra(MyProvider.ITEM_POSITION, position);
        remoteViews.setOnClickFillInIntent(R.id.itemText, clickIntent);

        return remoteViews;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDataSetChanged() {

        list.clear();

        BatteryManager bm = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int mHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);

        list.add("Battery health: " + convertHealth(mHealth));

        int mTemp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        float temp = mTemp / 10.0f;

        list.add("Temperature: " + temp + " °C");

        int mVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        float tempTurn = mVoltage / 1000.0f;
        list.add("Voltage: " + tempTurn + " V");

        int current = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
        float currTurn = current / 1000.0f;
        list.add("Current: " + currTurn + " mA");

    }

    private String convertHealth(int health){
        String result;
        switch(health){
            case BatteryManager.BATTERY_HEALTH_COLD:
                result = "Cold";
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                result = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                result = "Good";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                result = "Overheat";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                result = "Over voltage";
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                result = "Unknown";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                result = "Unspecified failure";
                break;
            default:
                result = "Unknown";
        }

        return result;
    }

    @Override
    public void onDestroy() {

    }

}
