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

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CustomViewHolder> {
    private Context context;
    private ArrayList<CardData> list;

    public CardAdapter(Context context, ArrayList<CardData> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public CardAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_item,viewGroup,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.CustomViewHolder customViewHolder, final int position) {
        //customViewHolder.image.setImageResource(list.get(position).getReView());
        Glide.with(context).load(list.get(position).getLastimage()).into(customViewHolder.image);
        customViewHolder.ResName.setText(list.get(position).getName());
        customViewHolder.ResMenu.setText(list.get(position).getMenu());
        customViewHolder.ResTel.setText(list.get(position).getPhone());
        customViewHolder.ResAddress.setSelected(true);
        customViewHolder.ResAddress.setText(list.get(position).getAddress());
        customViewHolder.ResDistance.setText(list.get(position).getDistance()+"m");
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

    @Override
    public int getItemCount() {
        int size;
        if(list.size() < 3){
            size = list.size();
        }else
            size = 3;
        return size;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView ResName;
        private TextView ResMenu;
        private TextView ResTel;
        private TextView ResAddress;
        private TextView ResDistance;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            ResName = itemView.findViewById(R.id.ResName);
            ResMenu = itemView.findViewById(R.id.ResMenu);
            ResTel = itemView.findViewById(R.id.ResTel);
            ResAddress = itemView.findViewById(R.id.ResAddress);
            ResDistance = itemView.findViewById(R.id.ResDistance);

        }
    }
}
