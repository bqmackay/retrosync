package verdad.retrosync;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by bqmackay on 8/28/15.
 */
public class RetroSyncDatabaseObjectUtil {
    public static <T extends Model> List<T> getAll(Class<T> type) {
        return new Select().from(type).execute();
    }

    public static <T extends Model> List<T> getAll(Class<T> type, String orderBy) {
        return new Select().from(type).orderBy(orderBy).execute();
    }

    public static <T extends Model> T getObjectById(Class<T> type, long id) { return new Select().from(type).where("serverId = ?", id).executeSingle(); }
}
