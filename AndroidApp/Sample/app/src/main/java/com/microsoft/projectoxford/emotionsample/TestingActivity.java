package com.microsoft.projectoxford.emotionsample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class TestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Map<String, Integer> emotionPosition= new HashMap<String, Integer>();
        final String[] emotions = new String[]{"HAPPINESS", "SADNESS", "SURPRISE", "ANGER", "DISGUST", "FEAR", "NEUTRAL", "CONTEMPT"};
        for(int i = 0; i < emotions.length; i++) {
            emotionPosition.put(emotions[i], i);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        GridView gridview = (GridView) findViewById(R.id.myGrid);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setNumColumns(2);
        Intent testIntent = getIntent();
        Bundle b = testIntent.getExtras();
        final String emotion = (String) b.get("result");
        Log.d("emotion in test mode", emotion);


        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //     Toast.makeText(TestActivity.this, "" + position,
                //             Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder1 = new AlertDialog.Builder(TestingActivity.this);
                int correctPosition = emotionPosition.get(emotion);
                Log.d("POSITION", Integer.toString(position));
                Log.d("CORRECT POSITION", Integer.toString(correctPosition));


                if(position == correctPosition) {
                    ImageView image = new ImageView(TestingActivity.this);
                    image.setImageResource(R.drawable.check);
                    builder1.setView(image);
                    builder1.setCancelable(true);

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent recognizePage = new Intent(TestingActivity.this, RecognizeActivity.class);
                            TestingActivity.this.startActivity(recognizePage);
                        }
                    }, 2000);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not Quite, Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.happiness, R.drawable.sadness,
            R.drawable.surprise, R.drawable.anger,
            R.drawable.disgust, R.drawable.fear,
            R.drawable.neutral, R.drawable.contempt
    };
}
