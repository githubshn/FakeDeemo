//选区歌曲界面
//SGName is needed to create

package sdb.fakedeemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Scanner;

public class SoundChoose2Activity extends AppCompatActivity {
    ConstraintLayout rl;
    private int ScreenH,ScreenW;
    private SoundCondition[] sound=new SoundCondition[100];
    private ImageView ImBack;
    private ImageView ImSoundPic,ImMid,ImUP,ImDown;
    private int SoundNum,ImMidID;
    private float x1=0,x2=0,x3=0,y1=0,y2=0,y3=0;
    private TextView TextCondition;
    private String SGName;
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

        Bundle bundle=this.getIntent().getExtras();
        SGName=bundle.getString("SGName");
        System.out.println(SGName);
        //获取曲包名

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

        String filename="sg"+SGName+"catalogue.txt";
        Scanner filescanner=null;
        InputStream inputstream=null;
        AssetManager assetmanager=getAssets();
        try{
            inputstream=assetmanager.open(filename);
            filescanner=new Scanner (inputstream);
            SoundNum=new Scanner(filescanner.nextLine()).nextInt();
            int i;
            for(i=0;i<=SoundNum+1;i++) {
                sound[i] = new SoundCondition();
                //name    pic   completeness   isavailable    isFC   isAC
                sound[i].name = filescanner.nextLine();
                sound[i].pic = filescanner.nextLine();
                sound[i].id=getResources().getIdentifier(sound[i].pic,"drawable","sdb.fakedeemo");
                sound[i].completeness=new Scanner(filescanner.nextLine()).nextFloat();
                sound[i].isavailable = new Scanner(filescanner.nextLine()).nextBoolean();
                sound[i].isFC = new Scanner(filescanner.nextLine()).nextBoolean();
                sound[i].isAC = new Scanner(filescanner.nextLine()).nextBoolean();
            }
        }catch(Exception e){
            //what to do? i do not know!
            System.out.println(e.getStackTrace());
        }
        //读入歌曲列表

        ImMidID=1;
        int ImSoundPicH=ScreenH*5/7;

        ImMid=new ImageView(this);
        rl.addView(ImMid);
        ImMid.setX(ScreenW-ImSoundPicH);
        ImMid.setY(ScreenH/7);
        lp = new ConstraintLayout.LayoutParams(ImSoundPicH,ImSoundPicH);
        ImMid.setLayoutParams(lp);
        ImMid.setScaleType(ImageView.ScaleType.FIT_XY);
        ImMid.setImageResource(sound[ImMidID].id);

        ImUP=new ImageView(this);
        rl.addView(ImUP);
        ImUP.setX(ScreenW-ImSoundPicH);
        ImUP.setY(ScreenH/7-ImSoundPicH);
        lp = new ConstraintLayout.LayoutParams(ImSoundPicH,ImSoundPicH);
        ImUP.setLayoutParams(lp);
        ImUP.setScaleType(ImageView.ScaleType.FIT_XY);
        ImUP.setImageResource(sound[ImMidID-1].id);

        ImDown=new ImageView(this);
        rl.addView(ImDown);
        ImDown.setX(ScreenW-ImSoundPicH);
        ImDown.setY(ScreenH/7+ImSoundPicH);
        lp = new ConstraintLayout.LayoutParams(ImSoundPicH,ImSoundPicH);
        ImDown.setLayoutParams(lp);
        ImDown.setScaleType(ImageView.ScaleType.FIT_XY);
        ImDown.setImageResource(sound[ImMidID+1].id);

        TextCondition=new TextView(this);
        rl.addView(TextCondition);
        TextCondition.setX(0);
        TextCondition.setY(ScreenH/7);
        TextCondition.setHeight(100);
        TextCondition.setWidth(ScreenW-ScreenH*5/7);
        TextCondition.setTextSize(30);
        TextCondition.setGravity(Gravity.CENTER);
        TextCondition.setText(sound[ImMidID].name);
        //TextCondition.setVisibility(View.INVISIBLE);

        /*
        ImSoundPic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Bundle bundle = new Bundle();
                bundle.putString("SoundName", sound[ImSound].name);
                bundle.putString("FROM","SoundChoose2");
                bundle.putString("WHERE","Performing");
                Intent intent = new Intent(SoundChoose2Activity.this, LoadingActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                SoundChoose2Activity.this.finish();
            }
        });
        */

    }
    public boolean onTouchEvent(android.view.MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                x1 = event.getX();
                y1 = event.getY();
                //System.out.println("x1");
                //System.out.println(x1);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                x2 = event.getX();
                y2 = event.getY();
                break;
            }
            case MotionEvent.ACTION_UP: {
                x3 = event.getX();
                y3 = event.getY();
                //System.out.println("x3");
                //System.out.println(x3);
                //System.out.println("*x1");
                //System.out.println(x1);
                if(x1>=ScreenW-ScreenH*5/7){
                    if(y3-y1<0&&Math.abs(y3-y1)>=100){
                        if(ImMidID<SoundNum){
                            ImMidID++;
                        }
                    }else if(y3-y1>0&&Math.abs(y3-y1)>=100){
                        //System.out.println("left");
                        if(ImMidID>1){
                            ImMidID--;
                        }
                    }else if((y1>=ScreenH/7&&y1<=ScreenH*6/7)&&(Math.abs(x3-x1)<=100)&&(Math.abs(y1-y3)<=100)){
                        if(sound[ImMidID].isavailable) {
                            Bundle bundle = new Bundle();
                            bundle.putString("SoundName", sound[ImMidID].name);
                            bundle.putString("SGName", SGName);
                            bundle.putString("FROM","SoundChoose2");
                            bundle.putString("WHERE","Performing");
                            Intent intent = new Intent(SoundChoose2Activity.this, LoadingActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            SoundChoose2Activity.this.finish();
                        }else{
                            Toast.makeText(SoundChoose2Activity.this,"LOCKED", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                //暂时没有动画
                ImUP.setImageResource(sound[ImMidID-1].id);
                ImMid.setImageResource(sound[ImMidID].id);
                ImDown.setImageResource(sound[ImMidID+1].id);
                //System.out.println(ImMidID);
                String atext;
                atext=sound[ImMidID].name;
                if(sound[ImMidID].isavailable){
                    atext=atext;
                    TextCondition.setTextColor(Color.BLACK);
                }else{
                    atext=atext+"  LOCKED!";
                    TextCondition.setTextColor(Color.RED);
                }
                TextCondition.setText(atext);
                TextCondition.setVisibility(View.VISIBLE);
                //更新曲包图案
                break;
            }
        }
        return true;
    };
}
