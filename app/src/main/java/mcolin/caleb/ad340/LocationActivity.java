package mcolin.caleb.ad340;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10002;

    private RequestQueue requestQueue;
    private ArrayList<CamItem> camItemArrayList;
    private GoogleMap mMap;
    private TextView currentLong;
    private TextView currentLat;
    private TextView currentAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        requestQueue = Volley.newRequestQueue(this);
        camItemArrayList = new ArrayList<>();

        currentLong = findViewById(R.id.locationLog);
        currentLat = findViewById(R.id.locationLat);
        currentAddress = findViewById(R.id.locationName);

        // Get permission. If denied exit activity, if granted proceed
        getLocationPermission();
    }

    /**
     * Checks the locations permissions. Asks for permissions if previously denied.
     * Calls the callback function "onRequestPermissionsResult" when asking for permission.
     */
    public void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission was granted by System. Continue with the activity.
            initMap();
        } else {
            // Permission was denied by System. Ask user for permission by calling the callback function.
            requestPermissions(new String[]{FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Callback function called when asking for permission.
     *
     * @param requestCode  the identification code of the permission request.
     * @param permissions  the permission requested.
     * @param grantResults the results of the users response.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted by System. Continue with the activity.
                initMap();
            } else {
                // User denied the permission. End the activity.
                finish();
            }
        }
    }

    /**
     * Starts the map by calling the override method "initMap()"
     */
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(LocationActivity.this);
    }

    /**
     * Holds all the settings and controls for the map
     *
     * @param googleMap The map
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        // Map Settings
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        // Methods
        getDeviceLocation();
        getCamLocation();
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                    getCurrentAddress(current);
                    mMap.setMyLocationEnabled(true); // Show blue location dot
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 11f));  // center camera on location
                } else {
                    Log.d("ERROR", "Can't find location");
                    Toast.makeText(this, "Cannot find your location", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (SecurityException e) {
            Log.e("ERROR", "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    public void getCurrentAddress(LatLng current) {
        Geocoder geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(current.latitude, current.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            Log.d("TEST", address);
            currentAddress.setText(address);
            currentLat.append(""+ current.latitude);
            currentLong.append("" + current.longitude);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method uses the Volley library to relieve JSON data from a given url.
     * The desired data is then parsed out in to string for use in the RecyclerView
     */
    private void getCamLocation() {
        String urlSource = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlSource, null,
                response -> {
                    try {
                        // The Array that hold all the objects within the "Features"
                        JSONArray featuresArray = response.getJSONArray("Features");
                        // Touch each object within the "Features" array
                        for (int i = 0; i < featuresArray.length(); i++) {
                            JSONObject featureItem = featuresArray.getJSONObject(i);
                            // The Array that hold all the objects within the "PointCoordinate" (only 2 doubles within the array). No loop
                            JSONArray pointArray = featureItem.getJSONArray("PointCoordinate");
                            double mLat = pointArray.getDouble(0);
                            double mLong = pointArray.getDouble(1);
                            // The Array that hold all the objects within the "Camera"
                            JSONArray cameraArray = featureItem.getJSONArray("Cameras");
                            // Touch each object within the "Camera" array (Usually only 1 item. Loop still needed).
                            for (int k = 0; k < cameraArray.length(); k++) {
                                JSONObject cameraItem = cameraArray.getJSONObject(k);
                                // Parsing out every string in each object of each array within each object of the main array.
                                String id = cameraItem.getString("Id");
                                String address = cameraItem.getString("Description");
                                String imageUrl = cameraItem.getString("ImageUrl");
                                String type = cameraItem.getString("Type");
                                // Pass the parsed data into the CamItem class
                                camItemArrayList.add(new CamItem(id, type, address, imageUrl, mLong, mLat));
                            }
                        }
                        // Once the async logic is done, call this function.
                        showMarkers();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    /**
     * Displays all the locations as marker on the map
     */
    private void showMarkers() {
        for (int i = 0; i < camItemArrayList.size(); i++) {
            LatLng latLng = new LatLng(camItemArrayList.get(i).getLat(), camItemArrayList.get(i).getLong());
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(camItemArrayList.get(i).getAddress())
                    .snippet(camItemArrayList.get(i).getCamId() + " : " + camItemArrayList.get(i).getType()));
        }
    }
}