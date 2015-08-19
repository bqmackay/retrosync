package verdad.retrosync;

import com.google.gson.annotations.Expose;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/**
 * Created by bqmackay on 8/12/15.
 */
public abstract class SyncModel extends Model {
    @Expose @Column (name = "serverId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE) public long id;
    @Expose @Column (name = "isSyncDirty") public boolean isSyncDirty;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SyncModel syncModel = (SyncModel) o;

        return id == syncModel.id;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
}
