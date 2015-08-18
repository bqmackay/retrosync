package verdad.retrofitsync;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by bqmackay on 8/12/15.
 */
public class MockInteractor implements SyncInteractorInterface<TestObject> {

    @Override
    public void success(TestObject testObject, Response response) {

    }

    @Override
    public void failure(RetrofitError error) {

    }

    @Override
    public void create(TestObject model, Callback retroSyncCallback) {
        retroSyncCallback.success(null, null);
    }

    @Override
    public void update(TestObject model, Callback retroSyncCallback) {

    }

    @Override
    public void delete(TestObject model, Callback retroSyncCallback) {

    }
}
