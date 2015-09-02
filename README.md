# RetroSync
Maintained by friends at VerdadTech

RetroSync stores your cloud database locally and managers your pending API calls if a network connection wasn't immediately available. This started with the need to save changes locally when network connectivity goes down, and then pushing those changes when network connectivity comes back. This library uses RetroFit and Active Android exclusively to get these things done. 

###Setup
You'll need to setup Active Android first in your app before using RetroSync. You can find those instructions on their [GitHub page](https://github.com/pardom/ActiveAndroid).

###Model
In your model class, you'll want to extend SyncModel and expose all the fields that you want to save locally.

```java
@Table(name = "Contact")
public class Contact extends SyncModel {
    @Expose @Column(name = "first_name") public String first_name;
    @Expose @Column(name = "last_name") public String last_name;
}
```

SyncModel stores the model's id from the server locally and makes it unique. That way, if you pull down that data again and the object has changed on the server, it will overwrite the item on your local database instead of adding a new item. Expose is needed to make sure certain fields that RetroFit seems to add are not included. Otherwise, you would build an adapter for every item.

###RetroSync Callback
RetroSync requires a special callback called SyncInteractorInterface. You will need to implement the create, update, and delete methods and make the respective API calls there, passing the provided retroSyncCallback as the callback to the RetroFit API.

```java
public class ContactInteractor implements SyncInteractorInterface<Contact> {

    private final String TAG = "ContactInteractor";

    ContactServiceInterface service;

    public MyInteractor() {
        provider = new NetworkProvider();
        service = (ContactServiceInterface) provider.create(ContactServiceInterface.class);
    }

    @Override
    public void create(Contact contact, Callback retroSyncCallback) {
        service.createContact(contact.getFirstName(), contact.getLastName(), retroSyncCallback);
    }

    @Override
    public void update(Contact contact, Callback retroSyncCallback) {
        service.updateContact(contact.getFirstName(), contact.getLastName(), retroSyncCallback);
    }

    @Override
    public void delete(Contact model, Callback retroSyncCallback) {
        service.deleteContact(contact.getFirstName(), contact.getLastName(), retroSyncCallback);
    }
    
    @Override
    public void success(Contact contact, Response response) {
        Log.i(TAG, "Success!");
    }

    @Override
    public void failure(RetrofitError error) {
        Log.i(TAG, "Failed: " + error);
    }
}
```

###Syncing
With your model and interactor ready to go, you can now make your data calls without worrying about network connections. When you are ready to make a network call, create an instance RetroSync, passing it a context.

```java
RetroSync synchronizer = new RetroSync(context);
```

Then when you want to make your network call, simply call:

```java
synchronizer.save(model, yourCallback, PendingObject.RETROFIT_SYNC_CREATE);
```

This will first save your data and your local database. Then it will attempt to make the data call. If the data call is successul, you're callback will be returned as expected. If the network is unavialable, a pending data call object will be saved. 

###Sending Pending Data Calls
When you want to send your pending data calls again, call the static method savePendingChanges on the RetroSync class.

```java
RetroSync.savePendingChanges(context);
```

###Sending Pending Data Calls via BroadcastReceiver
RetroSync has a built in broadcast receiver that listens for network connectivity changes. Adding this to your manifest will automatically send data updates when a data connection is available. By having simply adding the project, you're already set up!

