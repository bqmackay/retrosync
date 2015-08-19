package verdad.retrosync;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by bqmackay on 8/12/15.
 */
public class Reachability {

    ConnectivityManager manager;
    public Reachability(Context context) {
        manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    //FOR TESTING
    public Reachability() {
    }

    public boolean isOnline() {
        if (manager != null) {
            NetworkInfo netInfo = manager.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        Log.e("Reachability", "Don't use the empty constructor for Reachability; it's for testing. Use Reachability(Context context) instead.");
        return false;
    }
}
