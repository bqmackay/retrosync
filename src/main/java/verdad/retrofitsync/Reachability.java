package verdad.retrofitsync;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by bqmackay on 8/12/15.
 */
public class Reachability {

    ConnectivityManager manager;
    public Reachability(Context context) {
        manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public Reachability() {
    }

    public boolean isOnline() {
        NetworkInfo netInfo = manager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
