package verdad.retrosync;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.activeandroid.query.Select;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by bqmackay on 8/12/15.
 */
public class RetroSync {

    private Reachability reachability;

    public RetroSync(Context context) {
        this.reachability = new Reachability(context);
    }

    public RetroSync(Reachability reachability) {
        this.reachability = reachability;
    }

    /**
     * Save will do three things:
     * (1) save a pending network call to the database
     * (2) check for network connectivity
     * (3) if connectivity is good, send the REST call and delete the pending network call upon a successful call
     *
     * If the network connectivity is bad, the service's failure method is called with a network error message attached.
     *
     * @param model - object you want to save
     * @param service - the class that makes the REST call
     * @param verb - create, update, or delete
     */
    public void save(SyncModel model, SyncInteractorInterface service, String verb) {
        // - create pending object
        PendingObject pendingObject = new PendingObject();
        pendingObject.serviceName = service.getClass().getName();
        pendingObject.serviceMethod = verb;
        pendingObject.className = model.getClass().getName();
        pendingObject.json = getActiveAndroidGson().toJson(model);
        Log.i("RetroSync", "Saving initial pending object");
        pendingObject.save();
        // - save to database
        model.save();
        if (!saveToServer(model, service, pendingObject, verb)) {
            service.failure(RetrofitError.networkError("", new IOException("No network connectivity")));
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
                callService(model, service, object.serviceMethod, new RetroSyncCallback(model, object));
            }
        }
    }

    public static void savePendingChanges(Context context) throws ClassNotFoundException {
        savePendingChanges(new Reachability(context));
    }

    private boolean saveToServer(SyncModel model, SyncInteractorInterface service, PendingObject pendingObject, String verb) {
        if (reachability.isOnline()) {
            callService(model, service, verb, new RetroSyncCallback(model, pendingObject, service));
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
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }
}