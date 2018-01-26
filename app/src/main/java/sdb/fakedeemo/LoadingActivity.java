package sdb.fakedeemo;

//用于页面之间的转跳，处理临时文件等
//主要任务为处理intent的各种不同需求
//处理数据修改（采用临时文件的方式传递数据

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;

public class LoadingActivity extends AppCompatActivity {
    ConstraintLayout rl;
    private int ScreenH, ScreenW;
    private SoundCondition[] sound = new SoundCondition[100];
    private ImageView ImBack;
    private ImageView ImSoundPic;
    private Intent intent;
    private  Bundle LoadBundle;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(rl);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //强制横屏

        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        ScreenW = outMetrics.widthPixels;
        ScreenH = outMetrics.heightPixels;
        //获得屏幕长宽

        Bundle bundle = this.getIntent().getExtras();
        String from = bundle.getString("FROM");
        String where = bundle.getString("WHERE");
        String way = from + "-" + where;

        System.out.println(way);

        if (way.equals("Main-SoundChoose")) {


            //暂时不需要这一步转跳


        } else if (way.equals("SoundChoose-SoundChoose2")) {


            LoadBundle = new Bundle();
            LoadBundle.putString("SoundGroupName", bundle.getString("SoundGroupName"));
            System.out.println(bundle.getString("SoundGroupName"));
            intent = new Intent(LoadingActivity.this, SoundChoose2Activity.class);


        } else if (way.equals("SoundChoose2-Performing")) {


            LoadBundle = new Bundle();
            LoadBundle.putString("SoundName", bundle.getString("SoundName"));
            intent = new Intent(LoadingActivity.this, PerformingActivity.class);


        } else if (way.equals("Performing-Statistics")) {


            LoadBundle = new Bundle();
            LoadBundle.putString("SoundName", bundle.getString("SoundName"));
            intent = new Intent(LoadingActivity.this, StatisticsActivity.class);


        } else if (way.equals("Statistics-SoundChoose2")) {//暂时先去mainactivity


            LoadBundle = new Bundle();
            LoadBundle.putString("SoundName", bundle.getString("SoundName"));
            intent = new Intent(LoadingActivity.this, MainActivity.class);


        }

        intent.putExtras(LoadBundle);
        startActivity(intent);
        LoadingActivity.this.finish();

    }
    /*
    public boolean onTouchEvent(android.view.MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP: {
                intent.putExtras(LoadBundle);
                startActivity(intent);
                LoadingActivity.this.finish();
                break;
            }
        }
        return true;
    }
    */
}
