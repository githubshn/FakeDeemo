//启动页面

package sdb.fakedeemo;

        import android.content.Intent;
        import android.content.pm.ActivityInfo;
        import android.graphics.drawable.Drawable;
        import android.support.constraint.ConstraintLayout;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.DisplayMetrics;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout rl;
    private ImageView ImWelcome,ImBottomNotice;
    private int ScreenH,ScreenW;

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

        ImWelcome=new ImageView(this);
        rl.addView(ImWelcome);
        ImWelcome.setX(0);
        ImWelcome.setY(0);
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams((int) ScreenW, (int) ScreenH);
        ImWelcome.setLayoutParams(lp);
        ImWelcome.setScaleType(ImageView.ScaleType.FIT_XY);
        ImWelcome.setImageResource(R.drawable.welcome);
        ImWelcome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,SoundChooseActivity.class);
                startActivity(intent);
                //MainActivity.this.finish();
            }
        });
    }
}
