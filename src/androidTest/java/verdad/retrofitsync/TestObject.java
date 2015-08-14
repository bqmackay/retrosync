package verdad.retrofitsync;

import com.google.gson.annotations.Expose;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by bqmackay on 8/12/15.
 */
@Table(name = "TestObject")
public class TestObject extends SyncModel {

    @Expose @Column(name = "first_name") public String firstName;

    @Override
    public void createOnServer() {
        new MockServer().create();
    }

    @Override
    public void deleteOnServer() {
        new MockServer().delete();
    }

    @Override
    public void updateOnServer() {
        new MockServer().update();
    }
}