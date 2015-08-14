package verdad.retrofitsync;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by bqmackay on 8/12/15.
 */
public class RetrofitSync {

    private Reachability reachability;

    public RetrofitSync(Reachability reachability) {
        this.reachability = reachability;
    }

    public void save(SyncModel model) {
        if (!saveToServer(model)) {
            // - create pending object
            PendingObject pendingObject = new PendingObject();
            pendingObject.call = model.serverId == 0 ? PendingObject.RETROFIT_SYNC_CREATE : PendingObject.RETROFIT_SYNC_UPDATE;
            pendingObject.className = model.getClass().getName();
            pendingObject.json = getActiveAndroidGson().toJson(model);
            // - save to database
            pendingObject.save();
        }
    }

    private boolean saveToServer(SyncModel model) {
        model.isSyncDirty = true;
        model.save();
        if (reachability.isOnline()) {
            if (model.serverId == 0) {
                model.createOnServer();
            } else {
                model.updateOnServer();
            }
            model.isSyncDirty = false;
            return true;
        }
        return false;
    }

    public static void savePendingChanges(Reachability reachability) throws ClassNotFoundException {
        if (reachability.isOnline()) {
            // - get all pending objects
            List<PendingObject> pendingObjects =  new Select().from(PendingObject.class).execute();

            for (PendingObject object : pendingObjects) {
                // - turn each pending object into their actual object
                Class<?> clazz = Class.forName(object.className);
                SyncModel model = (SyncModel) new Gson().fromJson(object.json, clazz);
                // - call function specified in app
                switch (object.call) {
                    case PendingObject.RETROFIT_SYNC_CREATE :
                        model.createOnServer();
                        break;
                    case PendingObject.RETROFIT_SYNC_UPDATE :
                        model.updateOnServer();
                        break;
                    case PendingObject.RETROFIT_SYNC_DELETE :
                        model.deleteOnServer();
                        break;
                }
                object.delete();
            }
        }
    }

    private static Gson getActiveAndroidGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }
}