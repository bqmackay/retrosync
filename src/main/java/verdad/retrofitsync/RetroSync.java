package verdad.retrofitsync;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.activeandroid.query.Select;

import java.util.List;

import retrofit.Callback;

/**
 * Created by bqmackay on 8/12/15.
 */
public class RetroSync {

    private Reachability reachability;

    public RetroSync(Reachability reachability) {
        this.reachability = reachability;
    }

    public void save(SyncModel model, SyncInteractorInterface service, String verb) {
        if (!saveToServer(model, service, verb)) {
            // - create pending object
            PendingObject pendingObject = new PendingObject();
            pendingObject.serviceName = service.getClass().getName();
            pendingObject.verb = verb;
            pendingObject.className = model.getClass().getName();
            pendingObject.json = getActiveAndroidGson().toJson(model);
            // - save to database
            pendingObject.save();
        }
    }

    public static void savePendingChanges(Reachability reachability) throws ClassNotFoundException {
        if (reachability.isOnline()) {
            // - get all pending objects
            List<PendingObject> pendingObjects =  new Select().from(PendingObject.class).execute();

            for (PendingObject object : pendingObjects) {
                // - turn each pending object into their actual object
                Class<?> modelClass = Class.forName(object.className);
                SyncModel model = (SyncModel) new Gson().fromJson(object.json, modelClass);

                Class<?> serviceClass = Class.forName(object.serviceName);
                SyncInteractorInterface service = (SyncInteractorInterface) new Gson().fromJson(object.json, serviceClass);
                // - call function specified in app
                callService(model, service, object.verb, new RetroSyncPendingCallback(object, service));
            }
        }
    }

    private boolean saveToServer(SyncModel model, SyncInteractorInterface service, String verb) {
        model.isSyncDirty = true;
        model.save();
        if (reachability.isOnline()) {
            callService(model, service, verb, new RetroSyncCallback(model, service));
            return true;
        }
        return false;
    }

    private static void callService(SyncModel model, SyncInteractorInterface service, String verb, Callback<SyncModel> callback) {
        switch (verb) {
            case PendingObject.RETROFIT_SYNC_CREATE:
                service.create(model, callback);
                break;
            case PendingObject.RETROFIT_SYNC_UPDATE:
                service.update(model, callback);
                break;
//            case RETROFIT_SYNC_READ:
//                service.read();
//                break;
            case PendingObject.RETROFIT_SYNC_DELETE:
                service.delete(model, callback);
                break;
        }
    }
    private static Gson getActiveAndroidGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }
}