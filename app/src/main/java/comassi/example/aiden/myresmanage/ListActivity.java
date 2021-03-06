package comassi.example.aiden.myresmanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.ArrayList;

//리스트로 더보기를 눌렀을때 보여주는 화면
public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    static RecyclerView recyclerView;
    ArrayList<CardData> list = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    MainAdapter mainAdapter;

    ImageButton btnGoMain, btnGoMap;
    TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        list = (ArrayList<CardData>)intent.getSerializableExtra("list");

        recyclerView = findViewById(R.id.recyclerView);
        tvTest = findViewById(R.id.tvTest);
        btnGoMain = findViewById(R.id.btnGoMain);
        btnGoMap = findViewById(R.id.btnGoMap);


        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mainAdapter = new MainAdapter(this,list);
        recyclerView.setAdapter(mainAdapter);



        btnGoMain.setOnClickListener(this);
        btnGoMap.setOnClickListener(this);




    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnGoMain: finish(); break;
            case R.id.btnGoMap:
                finish();
                MainFragment.btnGoMap.callOnClick();
                break;

        }
    }

    public void reset(){
        recyclerView.notifyAll();
    }


    //음식점을 삭제했을대 리스트를 새로고침하기 위한 함수
    public void recyclerViewReresh(int position){
        list.remove(position);
        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(mainAdapter);
    }
}
