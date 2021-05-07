package mcolin.caleb.ad340;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;

public class TrafficActivity extends AppCompatActivity {


    // Instantiate the RequestQueue.
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);

        // Initiate RecyclerView, put ListView into the ArrayAdapter
        RecyclerView recyclerView = findViewById(R.id.trafficCams);
        CamAdapter camsAdapter = new CamAdapter(this);
        recyclerView.setAdapter(camsAdapter);

        // Initiate queue
        requestQueue = Volley.newRequestQueue(this);

        // Start 'Volley' to gather data
        gatherData();
    }

    /**
     * Method used the Volley library to relieve JSON data from a given url.
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

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    /**
     * Inner Adapter class for populating the RecyclerView
     */
    public static class CamAdapter extends RecyclerView.Adapter<CamAdapter.MyViewHolder> {
        private final Context context;

        public CamAdapter(Context c) {
            this.context = c;
            LayoutInflater inflater = LayoutInflater.from(context);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = inflater.inflate(R.layout.custom_layout, parent, false);
//            MyViewHolder holder = new MyViewHolder(view);
            return null;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
//            holder.serial_number.setText(<your string array[position]>);
        }

        @Override
        public int getItemCount() {
//            return <size of your string array list>;
            return 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView serial_number;

            public MyViewHolder(View itemView) {
                super(itemView);
//                serial_number = (TextView) itemView.findViewById(R.id.serialNo_CL);
            }
        }
    }

    /**
     * MENU
     * Shows all the menu options in the ActionBar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * MENU
     * EventHandler for all the options in the menu
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fire:
                Toast.makeText(this, "Refresh!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
                return true;
            case R.id.about:
                Intent intent = new Intent(getBaseContext(), AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}