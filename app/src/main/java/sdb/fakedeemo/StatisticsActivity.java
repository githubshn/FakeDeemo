//nothing special is needed to create
package sdb.fakedeemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity {
    ConstraintLayout rl;
    private int ScreenH,ScreenW;
    ImageView Im;
    TextView Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_main,null);
        setContentView(rl);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //强制横屏

        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        ScreenW = outMetrics.widthPixels;
        ScreenH = outMetrics.heightPixels;
        //获得屏幕长宽

        Text=new TextView(this);
        rl.addView(Text);
        Text.setX(ScreenW/2);
        Text.setY(0);
        Text.setHeight(100);
        Text.setWidth(3000);
        Text.setTextSize(30);
        Text.setTextColor(Color.GRAY);
        Text.setText("Have not been completed!");

        Im=new ImageView(this);
        rl.addView(Im);
        Im.setX(0);
        Im.setY(0);
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ScreenW/2,ScreenW/2);
        Im.setLayoutParams(lp);
        //Im.setScaleType(ImageView.ScaleType.FIT_XY);
        Im.setImageResource(R.drawable.test);
        Im.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Bundle bundle = new Bundle();
                bundle.putString("FROM","Statistics");
                bundle.putString("WHERE","SoundChoose2");
                Intent intent = new Intent(StatisticsActivity.this, LoadingActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                StatisticsActivity.this.finish();
            }
        });
    }
}
