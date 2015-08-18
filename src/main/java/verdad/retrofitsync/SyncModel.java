package verdad.retrofitsync;

import com.google.gson.annotations.Expose;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/**
 * Created by bqmackay on 8/12/15.
 */
public abstract class SyncModel extends Model {
    @Expose @Column (name = "serverId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE) public long id;
    @Expose @Column (name = "isSyncDirty") public boolean isSyncDirty;
}
