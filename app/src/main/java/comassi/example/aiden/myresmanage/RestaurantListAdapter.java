package comassi.example.aiden.myresmanage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//음식점 액티비티에서 댓글을 셋팅하는 아답터
public class RestaurantListAdapter extends BaseAdapter {
    Context context;
    ArrayList<CommentData> comList = new ArrayList<CommentData>();
    LayoutInflater inflater;


    public RestaurantListAdapter(Context context, ArrayList<CommentData> comList) {
        this.context = context;
        this.comList = comList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return comList.size();
    }

    @Override
    public Object getItem(int position) {
        return comList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        convertView = inflater.inflate(R.layout.restaurant_item, null);

        ImageView imageView = convertView.findViewById(R.id.ivComImage);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvDate2 = convertView.findViewById(R.id.tvDate2);
        TextView editReview = convertView.findViewById(R.id.editReview);

        Glide.with(context)
                .load(comList.get(position).getComImage())
                .into(imageView);

        tvDate.setText(comList.get(position).getComDate());
        editReview.setText(comList.get(position).getComContent());


        //댓글삭제버튼 이벤트
        tvDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ab = new AlertDialog.Builder(context);
                ab.setTitle("댓글삭제");
                ab.setMessage("해당 댓글을 삭제하시겠습니까?");

                ab.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RequestQueue rq = Volley.newRequestQueue(context);
                        String url = "http://alfo07.dothome.co.kr/comdeleate.php";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try {
                                    //Toast.makeText(context, "The comment is deleted", Toast.LENGTH_SHORT).show();

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

                                String test = comList.get(position).getComImage();
                                String test2 = test.substring(test.length() - 23, test.length());
                                params.put("email", comList.get(position).getComEmail());
                                params.put("address", comList.get(position).getComAddress());
                                params.put("image", comList.get(position).getComImage());
                                params.put("sub", test2);

                                return params;

                            }

                        };

                        rq.add(stringRequest);

                        ((RestaurantActivity) context).downloadComment();




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


        return convertView;
    }
}
