package comassi.example.aiden.myresmanage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RestaurantActivity extends AppCompatActivity {
    ImageView resReviewImage;
    EditText editReview;
    Button btnCall, btnMap;


    private TextView restaurant_id, restaurant_adress, restaurant_call;
    private GridView restaurant_grid;
    private ListView restaurant_re_list;
    private ArrayList<RestaurantData> list = new ArrayList<>();
    private Button addReview;
    private LinearLayoutManager linearLayoutManager;
    private RestaurantListAdapter restaurantListAdapter;
    private RestaurantGridAdapter adapter;


    ProgressDialog mProgressDialog;
    private static final String TAG = "blackjin";

    private Boolean isPermission = true;

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;

    private File tempFile;
    int mDegree;
    Bitmap resized;
    Bitmap bitmap;

    ProgressDialog prgDialog;
    String encodedString;
    String fileName;
    private static int RESULT_LOAD_IMG = 1;

    ArrayList<CommentData> comlist = new ArrayList<CommentData>();

    String address, name, phone;
    static int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        MainActivity.resCheck++;
        restaurant_id = findViewById(R.id.restaurant_id);
        restaurant_adress = findViewById(R.id.restaurant_adress);
        restaurant_call = findViewById(R.id.restaurant_call);
        restaurant_grid = findViewById(R.id.restaurant_grid);
        restaurant_re_list = findViewById(R.id.restaurant_re_list);
        btnMap = findViewById(R.id.btnMap);
        btnCall = findViewById(R.id.btnCall);
        addReview = findViewById(R.id.addReview);


        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");


        position = intent.getIntExtra("cPosition", 0);
        position = intent.getIntExtra("vPosition", 0);
        position = intent.getIntExtra("mPosition", 0);


        restaurant_id.setText(name);
        restaurant_adress.setText(address);
        restaurant_call.setText(phone);


        //리스트뷰에 들어갈 항목 받아오는 함수
        downloadComment();


        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(v.getContext(), R.layout.add_review, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setTitle("리뷰 등록");
                builder.setView(view);
                encodedString = null;

                resReviewImage = view.findViewById(R.id.resReviewImage);
                Button btnMyImage = view.findViewById(R.id.btnMyImage);
                editReview = view.findViewById(R.id.editReview);

                btnMyImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isPermission) goToAlbum();
                        else
                            Toast.makeText(v.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNegativeButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(encodedString==null){
                            Toast.makeText(RestaurantActivity.this, "이미지를 등록해주세요", Toast.LENGTH_SHORT).show();
                        }else if(editReview.getText().toString().equals("")){
                            Toast.makeText(RestaurantActivity.this, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show();
                        }else{
                            uploadImage();
                        }
                    }
                });

                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });


        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                startActivity(intent);

            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //맵 액티비티 종료
                if(MainActivity.mapCheck > 0){
                    MainActivity.mapCheck--;
                    MapActivity act = (MapActivity)MapActivity.ma;
                    act.finish();
                }

                //레스토랑 엑티비티 종료
                if(MainActivity.resCheck > 1){
                    MainActivity.resCheck--;
                    finish();
                }


                MainFragment.btnGoMap.callOnClick();


            }
        });
    }

    public void downloadComment() {

        String url = "http://alfo07.dothome.co.kr/selcomment.php";

        RequestQueue postReqeustQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                comlist.removeAll(comlist);

                Log.d("wherecango", "리스폰스");


                //음식점 정보받아오기
                try {
                    Log.d("wherecango", "트라이");

                    JSONObject jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("cominfo");


                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject obj = ja.getJSONObject(i);
                        CommentData commentData = new CommentData();
                        commentData.setComImage(obj.getString("image"));
                        commentData.setComContent(obj.getString("content"));
                        commentData.setComDate(obj.getString("date"));
                        commentData.setComEmail(obj.getString("email"));
                        commentData.setComAddress(obj.getString("address"));

                        comlist.add(commentData);
                    }

                    //그리드뷰 어댑터 셋팅
                    adapter = new RestaurantGridAdapter(getApplication(), comlist);
                    restaurant_grid.setAdapter(adapter);


                    //리스트뷰 어댑터 기본셋팅

                    restaurantListAdapter = new RestaurantListAdapter(RestaurantActivity.this, comlist);
                    restaurant_re_list.setAdapter(restaurantListAdapter);


                } catch (JSONException e) {
                    Log.d("wherecango", "캐치");
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
                params.put("address", address);

                return params;
            }
        };

        postReqeustQueue.add(postStringRequest);
    }

    //피카로로 웹서버 이미지 불러오기(글라이드로 대체함)
    public void loadImageInBackground() {

        mProgressDialog = new ProgressDialog(getApplicationContext());
        mProgressDialog.setMessage("Chargement...");
        mProgressDialog.setIndeterminate(false);

        Target target = new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) {

                mProgressDialog.show();
            }

            @Override
            public void onBitmapLoaded(Bitmap arg0, Picasso.LoadedFrom arg1) {

                resReviewImage.setImageBitmap(arg0);
                mProgressDialog.dismiss();
            }

            @Override
            public void onBitmapFailed(Drawable arg0) {
                // TODO Auto-generated method stub
                mProgressDialog.dismiss();
            }
        };

        Picasso.with(getApplicationContext())
                .load("http://a980721.dothome.co.kr/images/test.jpg")
                .into(target);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            String fileNameSegments[] = picturePath.split("/");
            fileName = fileNameSegments[fileNameSegments.length - 1];

            Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            resReviewImage.setImageBitmap(myImg);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedString = Base64.encodeToString(byte_arr, 0);

            //uploadImage();
        }else{
            return;
        }

        if (requestCode == PICK_FROM_ALBUM) {

            Uri photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

            Cursor cursor = null;

            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

                Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        } else if (requestCode == PICK_FROM_CAMERA) {

            setImage();

        }
    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    /**
     * tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     */
    private void setImage() {


        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inSampleSize = 4;
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        //bitmap=originalBm;
        int width = originalBm.getWidth();
        int height = originalBm.getHeight();
        resized = Bitmap.createScaledBitmap(originalBm, (width * 800) / height, 800, true);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());
        Log.d("image", "image : " + originalBm);

        resReviewImage.setImageBitmap(rotateImage(
                resized, 0));


        /**
         *  tempFile 사용 후 null 처리를 해줘야 합니다.
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
         */
        tempFile = null;

    }


    public Bitmap rotateImage(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }


    public class NameValuePair {
        private String image;
        private String image2;

        public NameValuePair(String image, String image2) {
            this.image = image;
            this.image2 = image2;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImage2() {
            return image2;
        }

        public void setImage2(String image2) {
            this.image2 = image2;
        }
    }

    /**
     * API call for upload selected image from gallery to the server
     */
    public void uploadImage() {

        RequestQueue rq = Volley.newRequestQueue(this);
        String url = "http://alfo07.dothome.co.kr/addcomment.php";
        String url2 = "http://alfo07.dothome.co.kr/setimage.php";

        //시간설정
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyMMddHHmmss");
        final String formatDate = sdfNow.format(date);


        //댓글등록
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(getBaseContext(), "The image is upload", Toast.LENGTH_SHORT).show();

                    //쿼리문으로 댓글에 대한 정보를 받아와서 어댑터에 새로 설정한다.
                    downloadComment();

                } catch (Exception e) {
                    Log.d("JSON Exception", e.toString());
                    Toast.makeText(getBaseContext(),
                            "Error while loadin data!" + e.toString(),
                            Toast.LENGTH_LONG).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "Error [" + error + "]");
                Toast.makeText(getBaseContext(),
                        "Cannot connect to server" + error, Toast.LENGTH_LONG)
                        .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("image", encodedString);
                params.put("filename", fileName);
                params.put("content", editReview.getText().toString());
                params.put("address", address);
                params.put("email", MainActivity.strEmail);
                params.put("curTime", formatDate);
                return params;

            }

        };


        //음식점 이미지셋팅

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST,
                url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("address", address);
                params.put("email", MainActivity.strEmail);
                params.put("curTime", formatDate);
                return params;

            }

        };


        rq.add(stringRequest);
        rq.add(stringRequest2);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (prgDialog != null) {
            prgDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed(){
        MainActivity.resCheck--;
        finish();

    }
}