package sdb.fakedeemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.Scanner;

public class SoundChoose2Activity extends AppCompatActivity {
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

        ImBack=new ImageView(this);
        rl.addView(ImBack);
        ImBack.setX(0);
        ImBack.setY(0);
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ScreenW/6,ScreenW/18);
        ImBack.setLayoutParams(lp);
        ImBack.setScaleType(ImageView.ScaleType.FIT_XY);
        ImBack.setImageResource(R.drawable.back);
        ImBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent=new Intent(SoundChoose2Activity.this,SoundChooseActivity.class);
                startActivity(intent);
                SoundChoose2Activity.this.finish();
            }
        });
        //Back 按钮

        Bundle bundle=this.getIntent().getExtras();
        String SGName=bundle.getString("NAME");
        //获取曲包名

        String filename="sg"+SGName+"catalogue.txt";
        //filename="test.txt";
        //System.out.println(filename);
        Scanner filescanner=null;
        InputStream inputstream=null;
        AssetManager assetmanager=getAssets();

        try{
            inputstream=assetmanager.open(filename);
            filescanner=new Scanner (inputstream);
            int soundnum=new Scanner(filescanner.nextLine()).nextInt();
            int i;
            for(i=0;i<=soundnum+1;i++) {
                sound[i] = new SoundCondition();
                //name    pic   completeness   isavailable    isFC   isAC
                sound[i].name = filescanner.nextLine();
                //System.out.println(sound[i].name);
                sound[i].pic = filescanner.nextLine();
                //System.out.println(sound[i].pic);
                sound[i].id=getResources().getIdentifier(sound[i].pic,"drawable","sdb.fakedeemo");
                //System.out.println(sound[i].id);
                //System.out.println(R.drawable.soundred1);
                sound[i].completeness=new Scanner(filescanner.nextLine()).nextFloat();
                sound[i].isavailable = new Scanner(filescanner.nextLine()).nextBoolean();
                sound[i].isFC = new Scanner(filescanner.nextLine()).nextBoolean();
                sound[i].isAC = new Scanner(filescanner.nextLine()).nextBoolean();
            }
        }catch(Exception e){
            //what to do? i do not know!
            System.out.println(e.getStackTrace());
        }

        final int ImSound=1;
        int ImSoundPicH=ScreenH*5/7;
        ImSoundPic=new ImageView(this);
        rl.addView(ImSoundPic);
        ImSoundPic.setX(ScreenW-ImSoundPicH);
        ImSoundPic.setY(ScreenH/7);
        lp = new ConstraintLayout.LayoutParams(ImSoundPicH,ImSoundPicH);
        ImSoundPic.setLayoutParams(lp);
        ImSoundPic.setScaleType(ImageView.ScaleType.FIT_XY);
        ImSoundPic.setImageResource(sound[ImSound].id);
        //ImSoundPic.setImageResource(R.drawable.soundred1);
        ImSoundPic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Bundle bundle=new Bundle();
                bundle.putString("NAME",sound[ImSound].name);
                Intent intent=new Intent(SoundChoose2Activity.this,PerformingActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                //SoundChoose2Activity.this.finish();
            }
        });
    }
}
