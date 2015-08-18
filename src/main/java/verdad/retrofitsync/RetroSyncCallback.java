package verdad.retrofitsync;

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

    @Override
    public void success(SyncModel syncModel, Response response) {
        model.isSyncDirty = false;
        model.save();
        pendingObject.delete();
        serverCallback.success(syncModel, response);
    }

    @Override
    public void failure(RetrofitError error) {
        pendingObject.save();
        serverCallback.failure(error);
    }
}
