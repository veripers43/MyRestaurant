package comassi.example.aiden.myresmanage;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddFragment extends Fragment implements View.OnClickListener {

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;

    MapView mapView;
    ImageView imageView;
    private GpsTracker gpsTracker;


    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_add_fragment, container, false);

        imageView = view.findViewById(R.id.imageView);

        imageView.setAlpha(120);
        mapView = new MapView(view.getContext());

        LinearLayout map_view = view.findViewById(R.id.map_view);

        gpsTracker = new GpsTracker(view.getContext());

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        mapView.setMapCenterPoint(mapPoint,true);
        mapView.setZoomLevel(-1,true);
        map_view.addView(mapView);


        //현위치 중심에 마크를 찍음

        fab_open = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_close);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);






        return view;
    }

    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(view.getContext(), Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(view.getContext(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(view.getContext(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }
        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(view.getContext(), "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString();

    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                anim();
                break;
            case R.id.fab1:
                anim();
                MapPoint mp = mapView.getMapCenterPoint();
                final double a = mp.getMapPointGeoCoord().latitude;
                final double b = mp.getMapPointGeoCoord().longitude;
                String address = getCurrentAddress(a, b);
                address = address.substring(5);
                Toast.makeText(v.getContext(), ""+address, Toast.LENGTH_SHORT).show();

                View view=View.inflate(v.getContext(),R.layout.alert_dialog,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                dlg.setTitle("음식점 정보등록");
                dlg.setIcon(R.drawable.fork);
                dlg.setView(view);

                TextView tvName=view.findViewById(R.id.tvName);
                TextView tvAdress=view.findViewById(R.id.tvAdress);
                TextView tvFood=view.findViewById(R.id.tvFood);
                TextView tvPhone=view.findViewById(R.id.tvPhone);
                final EditText edtName=view.findViewById(R.id.edtName);
                final EditText edtAdress=view.findViewById(R.id.edtAdress);
                final EditText edtPhone=view.findViewById(R.id.edtPhone);
                final Spinner spinner=view.findViewById(R.id.spinner);

                edtAdress.setFocusable(false);
                edtAdress.setClickable(false);
                edtAdress.setText(address);

                ArrayAdapter menuAdapter = ArrayAdapter.createFromResource(v.getContext(),
                        R.array.spinnerArray, android.R.layout.simple_spinner_item);
                menuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(menuAdapter);

                dlg.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = edtName.getText().toString();
                        String address = edtAdress.getText().toString();
                        String menu = spinner.getSelectedItem().toString();
                        String phone = edtPhone.getText().toString();
                        String lati = String.valueOf(a);
                        String longi = String.valueOf(b);
                        String email = MainActivity.strEmail;

                        if(name.equals("")){
                            Toast.makeText(v.getContext(), "음식점 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Response.Listener<String> responseListener  = new Response.Listener<String>() {

                            @Override

                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    Log.d("RegisterActivity","음식점등록 답변 기다림");

                                    if(success){
                                        toastDisplay("등록 성공!");
                                    }else{
                                        toastDisplay("등록 실패!");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }
                            }
                        };
                        RegisterRequest registerRequest = new RegisterRequest(name,address,menu,phone,lati,longi,email,responseListener);
                        RequestQueue queue = Volley.newRequestQueue(v.getContext());
                        queue.add(registerRequest);





                    }
                });
                dlg.setNegativeButton("취소",null);
                dlg.show();
                break;
            case R.id.fab2:
                anim();
                gpsTracker = new GpsTracker(v.getContext());

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                String address1 = getCurrentAddress(latitude, longitude);
                mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord
                        (latitude, longitude), -1, true);
                Toast.makeText(v.getContext(), "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
                //Toast.makeText(this, "Button2", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }

    private void toastDisplay(String message) {
        Toast.makeText(view.getContext(),message,Toast.LENGTH_SHORT).show();
    }


}
