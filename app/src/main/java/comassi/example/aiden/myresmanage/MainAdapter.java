package comassi.example.aiden.myresmanage;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        customViewHolder.tvAddress.setSelected(true);
        customViewHolder.tvAddress.setText(list.get(position).getAddress());
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

        customViewHolder.tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(context);
                ab.setTitle("음식점 삭제");
                ab.setMessage("해당 음식점을 삭제하시겠습니까?");

                ab.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RequestQueue rq = Volley.newRequestQueue(context);
                        String url = "http://alfo07.dothome.co.kr/resdelete.php";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try {
                                    Toast.makeText(context, "The restaurant is deleted", Toast.LENGTH_SHORT).show();
                                    ((ListActivity)context).recyclerViewReresh(position);

                                } catch (Exception e) {
                                    Log.d("JSON Exception", e.toString());
                                    Toast.makeText(context,
                                            "Error while delete comment!" + e.toString(),
                                            Toast.LENGTH_LONG).show();
                                }

                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("ERROR", "Error [" + error + "]");
                                Toast.makeText(context,
                                        "Cannot connect to server" + error, Toast.LENGTH_LONG)
                                        .show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();

                                params.put("email", list.get(position).getEmail());
                                params.put("address", list.get(position).getAddress());


                                return params;

                            }

                        };

                        rq.add(stringRequest);

                     //   ((RestaurantActivity) context).downloadComment();








                    }
                });
                ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                // 다이얼로그 보여주기
                ab.show();


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
        private TextView tvAddress;
        private TextView tvDel;
        private TextView tvPhone;
        private TextView tvDistance;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tvName = itemView.findViewById(R.id.tvName);
            tvMenu = itemView.findViewById(R.id.tvMenu);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDel = itemView.findViewById(R.id.tvDel);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvDistance = itemView.findViewById(R.id.tvDistance);
        }
    }
}
