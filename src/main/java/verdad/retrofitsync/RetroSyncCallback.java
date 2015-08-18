package verdad.retrofitsync;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by bqmackay on 8/17/15.
 */
public class RetroSyncCallback implements Callback<SyncModel> {

    SyncModel model;
    Callback<SyncModel> serverCallback;

    public RetroSyncCallback(SyncModel model, Callback<SyncModel> serverCallback) {
        this.model = model;
        this.serverCallback = serverCallback;
    }

    @Override
    public void success(SyncModel syncModel, Response response) {
        model.isSyncDirty = false;
        model.save();
        serverCallback.success(syncModel, response);
    }

    @Override
    public void failure(RetrofitError error) {
        serverCallback.failure(error);
    }
}
