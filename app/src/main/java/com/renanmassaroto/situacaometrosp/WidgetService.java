package com.renanmassaroto.situacaometrosp;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.renanmassaroto.situacaometrosp.widgets.WidgetRemoteViewsFactory;

/**
 * Created by renancardosomassaroto on 9/10/15.
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new WidgetRemoteViewsFactory(this.getApplicationContext(), intent));
    }
}
