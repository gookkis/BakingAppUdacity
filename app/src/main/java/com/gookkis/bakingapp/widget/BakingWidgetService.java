package com.gookkis.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class BakingWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        BakingDataProvider data = new BakingDataProvider(this, intent);
        return data;
    }
}
