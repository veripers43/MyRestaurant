package comassi.example.aiden.myresmanage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//메인화면에 오늘의 추천메뉴를 셋팅하는 아답터
public class CardAdapter2 extends RecyclerView.Adapter<CardAdapter2.CustomViewHolder> {
    private Context context;
    private ArrayList<CardData> list;

    public CardAdapter2(Context context, ArrayList<CardData> list) {
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public CardAdapter2.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.res_recommend,viewGroup,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter2.CustomViewHolder customViewHolder, final int position) {

        Glide.with(context).load(list.get(position).getLastimage()).into(customViewHolder.resRecImage);
        customViewHolder.resRecName.setText(list.get(position).getName());
        customViewHolder.resRecMenu.setText(list.get(position).getMenu());
        //customViewHolder.ResRecTel.setText(list.get(position).getPhone());
        //customViewHolder.ResRecAddress.setText(list.get(position).getAddress());
        customViewHolder.resRecDis.setText(list.get(position).getDistance()+"m");
        customViewHolder.itemView.getTag(position);
        customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),RestaurantActivity.class);
                intent.putExtra("address",list.get(position).getAddress());
                intent.putExtra("name",list.get(position).getName());
                intent.putExtra("phone",list.get(position).getPhone());
                MapActivity.position = position;
                context.startActivity(intent);

            }
        });
    }

    //1개만 표시함
    @Override
    public int getItemCount() {
        int size;
        if(list.size() < 2){
            size = list.size();
        }else
            size = 1;
        return size;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView resRecImage;
        private TextView resRecName;
        private TextView resRecMenu;
        private TextView resRecDis;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            resRecImage = itemView.findViewById(R.id.resRecImage);
            resRecName = itemView.findViewById(R.id.resRecName);
            resRecMenu = itemView.findViewById(R.id.resRecMenu);
            resRecDis = itemView.findViewById(R.id.resRecDis);

        }
    }
}