package verdad.retrofitsync;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by bqmackay on 8/17/15.
 */
public class RetroSyncPendingCallback implements Callback<SyncModel> {

    PendingObject pendingObject;
    Callback<SyncModel> serverCallback;

    public RetroSyncPendingCallback(PendingObject pendingObject, Callback<SyncModel> serverCallback) {
        this.pendingObject = pendingObject;
        this.serverCallback = serverCallback;
    }

    @Override
    public void success(SyncModel syncModel, Response response) {
        pendingObject.delete();
        serverCallback.success(syncModel, response);
    }

    @Override
    public void failure(RetrofitError error) {
        pendingObject.save();
        serverCallback.failure(error);
    }
}