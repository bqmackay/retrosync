package verdad.retrosync;

import retrofit.Callback;

/**
 * Created by bqmackay on 8/15/15.
 */
public interface SyncInteractorInterface<T> extends Callback<T>{
    void create(T model, Callback retroSyncCallback);
    void update(T model, Callback retroSyncCallback);
    void delete(T model, Callback retroSyncCallback);
}
