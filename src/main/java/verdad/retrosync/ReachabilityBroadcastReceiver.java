package verdad.retrosync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * Created by bqmackay on 8/18/15.
 */
public class ReachabilityBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            return;
        }

        if (!intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
            try {
                RetroSync.savePendingChanges(new Reachability(context));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
