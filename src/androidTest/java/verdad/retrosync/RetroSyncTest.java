package verdad.retrosync;

import com.activeandroid.query.Select;

import junit.framework.TestCase;

import java.util.List;

/**
 * Created by bqmackay on 8/12/15.
 */
public class RetroSyncTest extends TestCase {

    TestObject object;
    RetroSync sync;
    MockInteractor interactor;

    public void setUp() throws Exception {
        super.setUp();
        object = new TestObject();
        object.firstName = "TestName";
        interactor = new MockInteractor();
    }

    public void tearDown() throws Exception {
        object = null;
        List<PendingObject> pendingObjects =  new Select().from(PendingObject.class).execute();
        for (PendingObject obj : pendingObjects) {
            obj.delete();
        }
    }

    public void testSavePendingObject() throws Exception {
        sync = new RetroSync(new MockBadReachability());
        sync.save(object, interactor, PendingObject.RETROFIT_SYNC_CREATE);
        List<PendingObject> pendingObjects = new Select().from(PendingObject.class).execute();
        assertEquals(1, pendingObjects.size());
    }

    public void testSaveWithBadReachability() throws Exception {
        sync = new RetroSync(new MockBadReachability());
        sync.save(object, interactor, PendingObject.RETROFIT_SYNC_CREATE);
        assertEquals(true, object.isSyncDirty);
        List<PendingObject> pendingObjects = new Select().from(PendingObject.class).execute();
        assertEquals(1, pendingObjects.size());
    }

    public void testSaveWithGoodReachability() throws Exception {
        sync = new RetroSync(new MockGoodReachability());
        sync.save(object, interactor, PendingObject.RETROFIT_SYNC_CREATE);
        assertEquals(false, object.isSyncDirty);
        List<PendingObject> pendingObjects =  new Select().from(PendingObject.class).execute();
        assertEquals(0, pendingObjects.size());
    }

    public void testSavePendingChanges() throws Exception {
        sync = new RetroSync(new MockBadReachability());
        sync.save(object, interactor, PendingObject.RETROFIT_SYNC_CREATE);
        RetroSync.savePendingChanges(new MockGoodReachability());
        List<PendingObject> pendingObjects =  new Select().from(PendingObject.class).execute();
        assertEquals(0, pendingObjects.size());
    }

    public void testKeepPendingChangesIfReachabilityIsBad() throws Exception {
        sync = new RetroSync(new MockBadReachability());
        sync.save(object, interactor, PendingObject.RETROFIT_SYNC_CREATE);
        RetroSync.savePendingChanges(new MockBadReachability());
        List<PendingObject> pendingObjects =  new Select().from(PendingObject.class).execute();
        assertEquals(1, pendingObjects.size());
    }
}

class MockBadReachability extends Reachability{

    @Override
    public boolean isOnline() {
        return false;
    }
}

class MockGoodReachability extends Reachability{

    @Override
    public boolean isOnline() {
        return true;
    }
}