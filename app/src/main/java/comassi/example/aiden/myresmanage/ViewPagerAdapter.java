package comassi.example.aiden.myresmanage;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class ViewPagerAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<CardData> list;




    public ViewPagerAdapter(Context context, ArrayList<CardData> list)
    {
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.map_item, null);




        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvMenu = view.findViewById(R.id.tvMenu);
        TextView tvAddress = view.findViewById(R.id.tvAddress);
        TextView tvPhone = view.findViewById(R.id.tvPhone);
        TextView tvDistance = view.findViewById(R.id.tvDistance);

        tvAddress.setSelected(true);
        tvName.setText(list.get(position).getName());
        tvMenu.setText(list.get(position).getMenu());
        tvAddress.setText(list.get(position).getAddress());
        tvPhone.setText(list.get(position).getPhone());

        double distance = list.get(position).getDistance();

        if(distance>1000){
            distance = distance/1000;
            tvDistance.setText(String.format("%.1f", distance)+" km");
        }else{
            tvDistance.setText((int)distance +"m");
        }





        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(view.getContext(),RestaurantActivity.class);
                intent.putExtra("address",list.get(position).getAddress());
                intent.putExtra("name",list.get(position).getName());
                intent.putExtra("phone",list.get(position).getPhone());
                MapActivity.position = position;

                mContext.startActivity(intent);

            }
        });

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (View)o);
    }
}