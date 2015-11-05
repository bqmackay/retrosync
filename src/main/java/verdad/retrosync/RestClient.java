package verdad.retrosync;

/**
 * Created by bqmackay on 9/3/15.
 */
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestClient {

    private final String TAG = this.getClass().getSimpleName();
    private static RestClient instance;

    private RestAdapter mRestAdapter;

    // creating a kind of cache for using different clients
    private Map<String, Object> mClients = new HashMap<String, Object>();

    // singleton pattern
    public static RestClient getInstance() {
        if (null == instance) {
            instance = new RestClient();
        }
        return instance;
    }

    // used on Application.OnCreate() to initialize the service
    public void configureRestAdapter(String baseServerPath, RequestInterceptor interceptor) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(baseServerPath)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(interceptor)
                .build();
    }

    @SuppressWarnings("unchecked")
    public <T> T getClient(Class<T> clazz) {

        // guard for uninitialized mRestAdapter
        if (mRestAdapter == null) {
            return null;
        }

        // initializing generic client
        T client = null;

        // check service cache for client
        if ((client = (T) mClients.get(clazz.getCanonicalName())) != null) {
            return client;
        }

        // create a new client and save it in cache
        client = mRestAdapter.create(clazz);
        mClients.put(clazz.getCanonicalName(), client);
        return client;
    }

}