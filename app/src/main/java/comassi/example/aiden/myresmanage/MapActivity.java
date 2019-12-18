package comassi.example.aiden.myresmanage;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements View.OnClickListener, MapView.POIItemEventListener {

    ImageButton btnGoMain, btnGoList;
    MapPoint mapPoint;

    MapView mapView;
    private GpsTracker gpsTracker;
    LinearLayout map_view;
    MapPOIItem marker;
    ViewPager viewPager;

    static double curLati;
    static double curLongi;

    static int position;

    static ArrayList<MapPOIItem> markerList = new ArrayList<MapPOIItem>();
    ArrayList<CardData> list;
    private static final int DP = 24;

    static Activity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MainActivity.mapCheck++;

        ma = MapActivity.this;

        Intent intent = getIntent();
        list = (ArrayList<CardData>) intent.getSerializableExtra("list");

        btnGoMain = findViewById(R.id.btnGoMain);
        btnGoList = findViewById(R.id.btnGoList);

        btnGoMain.setOnClickListener(this);
        btnGoList.setOnClickListener(this);

        mapView = new MapView(MapActivity.this);

        map_view = findViewById(R.id.map_view);

        gpsTracker = new GpsTracker(MapActivity.this);

        curLati = gpsTracker.getLatitude();
        curLongi = gpsTracker.getLongitude();

        //mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        //mapView.setMapCenterPoint(mapPoint, true);
        mapView.setZoomLevel(-1, true);
        map_view.addView(mapView);


        viewPager = findViewById(R.id.viewPager);
        viewPager.setClipToPadding(false);

        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (DP * density);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin / 2);

        viewPager.setAdapter(new ViewPagerAdapter(this, list));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                mapView.setMapCenterPoint(markerList.get(position).getMapPoint(), true);
                mapView.selectPOIItem(markerList.get(position), true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mapView.setPOIItemEventListener(this);

        for (int i = 0; i < list.size(); i++) {
            marker = new MapPOIItem();
            double a = Double.parseDouble(list.get(i).getLati());
            double b = Double.parseDouble(list.get(i).getLongi());
            mapPoint = MapPoint.mapPointWithGeoCoord(a, b);
            marker.setItemName(list.get(i).getName());
            marker.setTag(i);
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
            mapView.addPOIItem(marker);
            mapView.selectPOIItem(marker, true);
            markerList.add(marker);

        }

        mapView.setMapCenterPoint(markerList.get(position).getMapPoint(), true);
        mapView.selectPOIItem(markerList.get(position), true);
        viewPager.setCurrentItem(position);




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGoMain:
                finish();
                break;
            case R.id.btnGoList:
                finish();
                MainFragment.btnGoList.callOnClick();
                break;

        }
    }


    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        TextView tvTest = findViewById(R.id.tvTest);
        tvTest.setText(mapPOIItem.getItemName());
        viewPager.setCurrentItem(mapPOIItem.getTag());


    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    @Override
    public void onBackPressed(){
        MainActivity.mapCheck--;
        finish();

    }




}
