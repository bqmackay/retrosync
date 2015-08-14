package verdad.retrofitsync;

import com.google.gson.annotations.Expose;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/**
 * Created by bqmackay on 8/12/15.
 */
public class SyncModel extends Model {
    @Expose @Column (name = "serverId") public long serverId;
    @Expose @Column (name = "isSyncDirty") public boolean isSyncDirty;

    public void createOnServer(){}
    public void updateOnServer(){}
    public void deleteOnServer(){}
}
