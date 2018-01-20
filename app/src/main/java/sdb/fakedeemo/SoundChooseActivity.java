//选区曲包

package sdb.fakedeemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Picture;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Scanner;


public class SoundChooseActivity extends AppCompatActivity {
    ConstraintLayout rl;
    private int ScreenH,ScreenW;
    private ImageView ImBack,ImBottomNotice,ImLeft,ImRight,ImMid;
    private SGCondition[] sgc=new SGCondition[100];
    private int ImMidID;

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
                Intent intent=new Intent(SoundChooseActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        //Back 按钮

        int ImBottomNoticeH=ScreenH/10;
        ImBottomNotice=new ImageView(this);
        rl.addView(ImBottomNotice);
        ImBottomNotice.setX(0);
        ImBottomNotice.setY(ScreenH-ImBottomNoticeH);
        lp = new ConstraintLayout.LayoutParams((int) ScreenW, (int) ImBottomNoticeH);
        ImBottomNotice.setLayoutParams(lp);
        ImBottomNotice.setScaleType(ImageView.ScaleType.FIT_XY);
        ImBottomNotice.setImageResource(R.drawable.start);

        int ImLeftH=ScreenH*2/3;
        int ImleftW=ImLeftH;
        ImLeft=new ImageView(this);
        rl.addView(ImLeft);
        ImLeft.setX(ScreenW/2-ImleftW);
        ImLeft.setY(ScreenH/5);
        lp = new ConstraintLayout.LayoutParams((int)ImleftW/2, (int) ImLeftH);
        ImLeft.setLayoutParams(lp);
        ImLeft.setScaleType(ImageView.ScaleType.FIT_XY);
        ImLeft.setVisibility(View.INVISIBLE);

        int ImMidH=ScreenH*2/3;
        int ImMidW=ImLeftH;
        ImMid=new ImageView(this);
        rl.addView(ImMid);
        ImMid.setX(ScreenW/2-ImleftW/2);
        ImMid.setY(ScreenH/5);
        lp = new ConstraintLayout.LayoutParams((int)ImMidW, (int) ImMidH);
        ImMid.setLayoutParams(lp);
        ImMid.setScaleType(ImageView.ScaleType.FIT_XY);
        ImMid.setVisibility(View.INVISIBLE);

        int ImRightH=ScreenH*2/3;
        int ImRightW=ImLeftH;
        ImRight=new ImageView(this);
        rl.addView(ImRight);
        ImRight.setX(ScreenW/2+ImleftW/2);
        ImRight.setY(ScreenH/5);
        lp = new ConstraintLayout.LayoutParams((int)ImRightW/2, (int) ImRightH);
        ImRight.setLayoutParams(lp);
        ImRight.setScaleType(ImageView.ScaleType.FIT_XY);
        ImRight.setVisibility(View.INVISIBLE);


        //System.out.println("!!!!!");


        ImBottomNotice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String sgfile="sgfile.txt";
                Scanner filescanner=null;
                InputStream inputstream=null;
                AssetManager assetmanager=getAssets();
                try{
                    inputstream=assetmanager.open(sgfile);
                    filescanner=new Scanner (inputstream);
                    int sgnum=new Scanner(filescanner.nextLine()).nextInt();
                    int i;
                    for(i=0;i<=sgnum+1;i++) {
                        sgc[i] = new SGCondition();
                        sgc[i].name = filescanner.nextLine();
                        //System.out.println(sgc[i].name);
                        sgc[i].pic = filescanner.nextLine();
                        //System.out.println(sgc[i].pic);
                        sgc[i].id=getResources().getIdentifier(sgc[i].pic,"drawable","sdb.fakedeemo");
                        //System.out.println(sgc[i].id);
                        sgc[i].isavailable = new Scanner(filescanner.nextLine()).nextBoolean();
                    }
                }catch(Exception e){
                    //what to do? i do not know!
                    //System.out.println(e.getStackTrace());
                }
                //show three ImageView
                ImLeft.setImageResource(sgc[0].id);
                ImMid.setImageResource(sgc[1].id);
                ImRight.setImageResource(sgc[2].id);
                ImLeft.setVisibility(View.VISIBLE);
                ImMid.setVisibility(View.VISIBLE);
                ImRight.setVisibility(View.VISIBLE);
            }
        });
        ImMidID=1;

        ImMid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("NAME",sgc[ImMidID].name);
                Intent intent=new Intent(SoundChooseActivity.this,SoundChoose2Activity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
