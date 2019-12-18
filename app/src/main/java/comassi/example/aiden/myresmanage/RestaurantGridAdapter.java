package comassi.example.aiden.myresmanage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RestaurantGridAdapter extends BaseAdapter {
    Context context;
    ArrayList<CommentData> comList = new ArrayList<CommentData>();
    LayoutInflater inflater;

    public RestaurantGridAdapter(Context context,ArrayList<CommentData> comList) {
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        convertView = inflater.inflate(R.layout.restaurant_image_list, null);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.restaurant_list_image);

        Glide.with(context)
                .load(comList.get(position).getComImage())
                .into(imageView);



        return convertView;
    }
}
