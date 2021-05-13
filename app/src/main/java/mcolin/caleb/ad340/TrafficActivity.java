package mcolin.caleb.ad340;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrafficActivity extends BaseActivity {

    private RequestQueue requestQueue;
    private ArrayList<CamItem> camItemArrayList;
    private CamAdapter camAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);

        // Initiate ArrayList of items
        camItemArrayList = new ArrayList<>();
        // Initiate Adapter with an empty ArrayList
        camAdapter = new CamAdapter(TrafficActivity.this, camItemArrayList);

        // Initiate RecyclerView
        RecyclerView recyclerView = findViewById(R.id.trafficCams);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(camAdapter);

        // Initiate RequestQueue from 'Volley' for loading data from external source
        requestQueue = Volley.newRequestQueue(this);

        // Check network connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            gatherData();
        } else {
            Toast.makeText(getApplicationContext(), "No connection Detected.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method uses the Volley library to relieve JSON data from a given url.
     * The desired data is then parsed out in to string for use in the RecyclerView
     */
    public void gatherData() {
        String urlSource = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlSource, null,
                response -> {
                    try {
                        // The Feature array of objects
                        JSONArray featuresArray = response.getJSONArray("Features");
                        // Each object within the Feature array
                        for (int i = 0; i < featuresArray.length(); i++) {
                            JSONObject featureItem = featuresArray.getJSONObject(i);
                            // The Camera array of objects
                            JSONArray cameraArray = featureItem.getJSONArray("Cameras");
                            // Each object within the Camera array
                            for (int k = 0; k < cameraArray.length(); k++) {
                                JSONObject cameraItem = cameraArray.getJSONObject(k);
                                // Parsing out every string in each object of each array within each object of the main array.
                                String id = cameraItem.getString("Id");
                                String address = cameraItem.getString("Description");
                                String imageUrl = cameraItem.getString("ImageUrl");
                                String type = cameraItem.getString("Type");
                                // Pass the parsed data into the CamItem class
                                camItemArrayList.add(new CamItem(id, type, address, imageUrl));
                            }
                        }
                        // Every time the ArrayList is updated the adapter in turn in updated.
                        // This will reload the adapter open change
                        camAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    /**
     * Inner class for each Camera item
     */
    public static class CamItem {
        private final String mCamId;
        private final String mCamType;
        private final String mCameAddress;
        private final String mCamImageUrl;

        public CamItem(String id, String type, String address, String url) {
            mCameAddress = address;
            mCamId = id;
            mCamType = type;
            mCamImageUrl = checkType(type, url);
        }

        public String checkType(String type, String url) {
            String newUrl = "";
            if (type.equals("sdot")) {
                newUrl = "https://www.seattle.gov/trafficcams/images/" + url;
            } else if (type.equals("wsdot")) {
                newUrl = "https://images.wsdot.wa.gov/nw/" + url;
            } else {
                Log.i("TAG", "incorrect type");
            }
            return newUrl;
        }

        public String getCamId() {
            return mCamId;
        }

        public String getType() {
            return mCamType;
        }

        public String getAddress() {
            return mCameAddress;
        }

        public String getUrl() {
            return mCamImageUrl;
        }
    }

    /**
     * Inner Adapter class for populating the RecyclerView
     */
    public static class CamAdapter extends RecyclerView.Adapter<CamAdapter.MyViewHolder> {
        private final Context mContext;
        private final ArrayList<CamItem> camItemsList;

        public CamAdapter(Context c, ArrayList<CamItem> list) {
            mContext = c;
            camItemsList = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.traffic_item, parent, false);
            return new MyViewHolder(v);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // Using 'position' to cycle through each item in the array
            CamItem currentItem = camItemsList.get(position);
            // Getting the data that was passed into the CamItem class
            String url = currentItem.getUrl();
            String id = currentItem.getCamId();
            String type = currentItem.getType();
            String address = currentItem.getAddress();
            // Setting the data to the items in the 'traffic_item.xml' using the holder below
            holder.mCamId.setText(String.format("%s : ", id));
            holder.mCamType.setText(type);
            holder.mCamAddress.setText(address);
            Picasso.get().load(url).fit().centerInside().into(holder.mCamImage);
        }

        @Override
        public int getItemCount() {
            return camItemsList.size();
        }

        /**
         * Inner ViewHolder Class with in the RecyclerView Adapter
         */
        static class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView mCamImage;
            public TextView mCamId;
            public TextView mCamType;
            public TextView mCamAddress;

            public MyViewHolder(View itemView) {
                super(itemView);
                mCamImage = itemView.findViewById(R.id.cam_image);
                mCamId = itemView.findViewById(R.id.cam_id);
                mCamType = itemView.findViewById(R.id.cam_type);
                mCamAddress = itemView.findViewById(R.id.cam_address);
            }
        }
    }
}