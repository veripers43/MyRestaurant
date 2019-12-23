package comassi.example.aiden.myresmanage;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

//메인화면 프래그먼트
public class MainFragment extends Fragment implements View.OnClickListener {


    RequestQueue queue;
    static ArrayList<CardData> list = new ArrayList<>();
    static ArrayList<CardData> list2 = new ArrayList<>();
    RecyclerView recyclerView;
    Button btnSearch;
    static Button btnGoList, btnGoMap;
    EditText editSearch;
    LinearLayoutManager linearLayoutManager;
    CardAdapter cardAdapter;

    CardAdapter2 cardAdapter2;
    RecyclerView choo;

    private GpsTracker gpsTracker;
    static double curLati;
    static double curLongi;

    ImageView testImage;


    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_fragment, container, false);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        queue = Volley.newRequestQueue(view.getContext());

        recyclerView = view.findViewById(R.id.recyclerView);
        choo = view.findViewById(R.id.choo);
        editSearch = view.findViewById(R.id.editSearch);
        btnSearch = view.findViewById(R.id.btnSearch);

        btnGoList = view.findViewById(R.id.btnGoList);
        btnGoMap = view.findViewById(R.id.btnGoMap);

        gpsTracker = new GpsTracker(getActivity());
        curLati = gpsTracker.getLatitude();
        curLongi = gpsTracker.getLongitude();

        linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);


        getResInfo();

        linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        choo.setLayoutManager(linearLayoutManager);

        btnSearch.setOnClickListener(this);
        btnGoList.setOnClickListener(this);
        btnGoMap.setOnClickListener(this);

        testImage = view.findViewById(R.id.testImage);


    }

    //음식점에 대한 정보를 가져와서 셋팅함
    public void getResInfo() {
        String url = "http://alfo07.dothome.co.kr/seltest.php";

        RequestQueue postReqeustQueue = Volley.newRequestQueue(getActivity());
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                list.removeAll(list);
                list2.removeAll(list2);


                Log.d("getResInfo", response);


                //음식점 정보받아오기
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("userinfo");

                    int ox = (int) (Math.random() * ja.length());

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject obj = ja.getJSONObject(i);
                        CardData cardData = new CardData();
                        cardData.setName(obj.getString("name"));
                        cardData.setAddress(obj.getString("address"));
                        cardData.setMenu(obj.getString("menu"));
                        cardData.setPhone(obj.getString("phone"));
                        cardData.setLati(obj.getString("lati"));
                        cardData.setLongi(obj.getString("longi"));
                        cardData.setEmail(obj.getString("email"));
                        cardData.setLastimage(obj.getString("lastimage"));

                        Location startPos = new Location("PointA");
                        Location endPos = new Location("PointB");

                        startPos.setLatitude(curLati);
                        startPos.setLongitude(curLongi);
                        endPos.setLatitude(Double.parseDouble(obj.getString("lati")));
                        endPos.setLongitude(Double.parseDouble(obj.getString("longi")));
                        int distance = (int) (startPos.distanceTo(endPos));

                        cardData.setDistance(distance);

                        list.add(cardData);
                        if (i == ox) {
                            list2.add(cardData);
                        }
                    }

                    //거리가 가까운 순으로 정렬
                    Collections.sort(list, new Comparator<CardData>() {
                        @Override
                        public int compare(CardData d1, CardData d2) {
                            if (d1.getDistance() > d2.getDistance()) {
                                return 1;
                            } else if (d1.getDistance() < d2.getDistance()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    });

                    cardAdapter = new CardAdapter(getActivity(), list);
                    cardAdapter2 = new CardAdapter2(getActivity(), list2);
                    recyclerView.setAdapter(cardAdapter);
                    choo.setAdapter(cardAdapter2);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", MainActivity.strEmail);

                return params;
            }
        };

        postReqeustQueue.add(postStringRequest);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            //검색버튼
            case R.id.btnSearch:

                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("name", editSearch.getText().toString());
                startActivity(intent);
                break;

                //리스트 액티비티로 이동하는 버튼
            case R.id.btnGoList:
                Intent intent1 = new Intent(getActivity(), ListActivity.class);
                if(list.size() == 0){
                    Toast.makeText(getActivity(),"등록된 맛집이 없습니다",Toast.LENGTH_SHORT).show();
                }else{
                    intent1.putExtra("list", list);
                    startActivity(intent1);
                }

                break;

                //맵 액티비티로 이동하는 버튼
            case R.id.btnGoMap:
                Intent intent2 = new Intent(getActivity(), MapActivity.class);
                if(list.size() == 0){
                    Toast.makeText(getActivity(),"등록된 맛집이 없습니다",Toast.LENGTH_SHORT).show();
                }else{
                    intent2.putExtra("list", list);
                    startActivity(intent2);
                }



        }
    }


    private void toastDisplay(String message) {
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
