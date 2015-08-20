package verdad.retrosync;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by bqmackay on 8/17/15.
 */
public class RetroSyncCallback implements Callback<SyncModel> {

    SyncModel model;
    private PendingObject pendingObject;
    Callback<SyncModel> serverCallback;

    public RetroSyncCallback(SyncModel model, PendingObject pendingObject, Callback<SyncModel> serverCallback) {
        this.model = model;
        this.pendingObject = pendingObject;
        this.serverCallback = serverCallback;
    }

    public RetroSyncCallback(SyncModel model, PendingObject pendingObject) {
        this.model = model;
        this.pendingObject = pendingObject;
    }

    @Override
    public void success(SyncModel syncModel, Response response) {
        pendingObject.delete();
        if (serverCallback != null) serverCallback.success(syncModel, response);
    }

    @Override
    public void failure(RetrofitError error) {
        pendingObject.save();
        if (serverCallback != null) serverCallback.failure(error);
    }
}
