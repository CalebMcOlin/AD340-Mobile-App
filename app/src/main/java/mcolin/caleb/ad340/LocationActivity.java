package mcolin.caleb.ad340;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationActivity extends BaseActivity {

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        getLocation();
    }

    /**
     * Checks the locations permissions. Asks for permissions if previously denied.
     * Calls the callback function "onRequestPermissionsResult" when asking for permission.
     */
    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission was granted. Continue with the activity
            Log.d("LOCATION", "Permission already granted.");
            loadUI();
        } else {
            // Permission was denied. Ask for permission (call the callback function)
            Log.d("LOCATION", "Permission already denied.");
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    /**
     * Callback function called when asking for permission.
     *
     * @param requestCode the identification code of the permission request.
     * @param permissions the permission requested.
     * @param grantResults the results of the users response.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("LOCATION", "Requesting Permission");
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // user granted the permission
                Log.d("LOCATION", "Permission Granted by user");
                loadUI();
            } else {
                // user denied the permission
                Log.d("LOCATION", "Permission Denied by user");
                finish();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void loadUI() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation();
        Log.d("LOCATION", "Permissions were Granted. Page is loading");
    }
}