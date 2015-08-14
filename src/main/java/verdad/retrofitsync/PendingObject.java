package verdad.retrofitsync;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by bqmackay on 8/12/15.
 */
@Table(name = "RetrofitSyncPendingObject")
public class PendingObject extends Model {
    public static final String RETROFIT_SYNC_CREATE = "create";
    public static final String RETROFIT_SYNC_UPDATE = "update";
    public static final String RETROFIT_SYNC_DELETE = "delete";

    @Column(name = "class") public String className;
    @Column(name = "call") public String call;
    @Column(name = "json") public String json;
}