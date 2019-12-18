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

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {
    Context context;
    ArrayList<CardData> list;

    public MainAdapter(Context context, ArrayList<CardData> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MainAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,viewGroup,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.CustomViewHolder customViewHolder, final int position) {

        Glide.with(context)
                .load(list.get(position).getLastimage())
                .into(customViewHolder.imageView);

        customViewHolder.tvName.setText(list.get(position).getName());
        customViewHolder.tvMenu.setText(list.get(position).getMenu());
        //customViewHolder.tvComment.setText(list.get(position).getMenu());
        customViewHolder.tvAddress.setSelected(true);
        customViewHolder.tvAddress.setText(list.get(position).getAddress());
        //customViewHolder.tvHello.setText(list.get(position).getMenu());
        customViewHolder.tvPhone.setText("\u2706 "+list.get(position).getPhone());

        double distance = list.get(position).getDistance();

        if(distance>1000){
            distance = distance/1000;
            customViewHolder.tvDistance.setText(String.format("%.1f", distance)+" km");
        }else{
            customViewHolder.tvDistance.setText((int)distance +"m");
        }

        customViewHolder.itemView.getTag(position);



        customViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView tvName;
        private TextView tvMenu;
        private TextView tvComment;
        private TextView tvAddress;
        private TextView tvHello;
        private TextView tvPhone;
        private TextView tvDistance;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tvName = itemView.findViewById(R.id.tvName);
            tvMenu = itemView.findViewById(R.id.tvMenu);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvHello = itemView.findViewById(R.id.tvHello);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvDistance = itemView.findViewById(R.id.tvDistance);
        }
    }
}
