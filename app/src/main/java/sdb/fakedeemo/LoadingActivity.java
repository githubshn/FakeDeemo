package sdb.fakedeemo;

//用于页面之间的转跳，处理临时文件等
//主要任务为处理intent的各种不同需求
//处理数据修改（采用临时文件的方式传递数据

import android.content.pm.ActivityInfo;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;

public class LoadingActivity extends AppCompatActivity {
    ConstraintLayout rl;
    private int ScreenH,ScreenW;
    private SoundCondition[] sound=new SoundCondition[100];
    private ImageView ImBack;
    private ImageView ImSoundPic;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_main,null);
        setContentView(rl);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //强制横屏

        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        ScreenW = outMetrics.widthPixels;
        ScreenH = outMetrics.heightPixels;
        //获得屏幕长宽

        Bundle bundle=this.getIntent().getExtras();
        String from=bundle.getString("FROM");
        String where=bundle.getString("WHERE");
        String way=from+"-"+where;
        if(way=="Main-SoundChoose"){

        }else if(way=="SoundChoose-SoundChoose2"){

        }else if(way=="SiundChoose2-Performing"){

        }else if(way=="Performing-Statistics"){

        }else if(way=="Statistics-SoundChoose2"){

        }
    }
}
