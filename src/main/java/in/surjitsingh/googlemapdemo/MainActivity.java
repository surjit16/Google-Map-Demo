package in.surjitsingh.googlemapdemo;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {


    TextView tvlatitue, tvlongitude, tvphysicaladdress;

    FusedLocationProviderClient fusedLocationProviderClient;

    Location lastlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvlatitue = findViewById(R.id.latitude);
        tvlongitude = findViewById(R.id.longitude);
        tvphysicaladdress = findViewById(R.id.physicaladdress);

      fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);


    }

    public void dothis(View view) {
        getLocation();
    }

    void getLocation()
    {
        if(ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{permission.ACCESS_FINE_LOCATION},1);

        }
        else
        {
            Log.d("TAG", "getLocation: permissions granted");
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(location != null)
                    {
                        lastlocation = location;
                        double latitude = lastlocation.getLatitude();
                        double longitue = lastlocation.getLongitude();

                        tvlatitue.setText(""+latitude);
                        tvlongitude.setText(""+longitue);

                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());


                        try {
                            List<Address> locationlist = geocoder.getFromLocation(latitude,longitue,1);


                            if(locationlist.size() >0)
                            {
                                 Address address = locationlist.get(0);
                               /* Log.e("AAAAAAAaa", address.toString());
                                Log.e("AAAAAAAaa", address.getAdminArea());
                                Log.e("AAAAAAAaa", address.getAddressLine(0));
                                Log.e("AAAAAAAaa", address.getFeatureName());
                                Log.e("AAAAAAAaa", address.getLocality());
                                Log.e("AAAAAAAaa", address.getPhone());
                                Log.e("AAAAAAAaa", address.getPostalCode());
                                Log.e("AAAAAAAaa", address.getPremises());
                                Log.e("AAAAAAAaa", address.getSubAdminArea());
                                Log.e("AAAAAAAaa", address.getSubLocality());
                                Log.e("AAAAAAAaa", address.getMaxAddressLineIndex()+"");
*/
                                tvphysicaladdress.setText("Address is:"+ address);
                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{

                        tvlatitue.setText("null");
                        tvlongitude.setText("null");
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                    getLocation();
                else
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void openMap(View view) {

        startActivity(new Intent(this,MapActivity.class));

    }

}
