package sdb.fakedeemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Message;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


public class PerformingActivity extends AppCompatActivity {

    ConstraintLayout rl;
    private int ScreenH,ScreenW;
    private Key[] key=new Key[5000];
    private int speed,length,sparetime1=1000,sparetime2=3000,preparetime=1000;
    private String[] reader=new String[1000];
    private int lenreader,keynum;
    private MediaPlayer mediaplayer;
    private Boolean SoundOver=false;
    private ImageView ImStop;
    private TextView TextPosition,TextMouse,TextScore;
    private Timer timer;
    private TimerTask timerTask;
    final int refresh=40;//大约24分之一秒
    private SoundTrack[] soundtrack=new SoundTrack[37];
    private int nowKeyID;
    private float nowKeyTime;
    private int ContinueSound[]={1,1,2,2,4,4,4,4,8,8,8,8,8,8,8,8,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,32};
    private DrawCanvas view;

    private GameScore score=new GameScore();
    public float simglekeyscore;

    private int testnum=0;

    void Close(){
        /*
        if(mediaplayer!=null||mediaplayer.isPlaying()){
            mediaplayer.release();
            mediaplayer.reset();
        }
        */
        SoundOver=true;
        //Bundle bundle=this.getIntent().getExtras();
        //String SGName=bundle.getString("NAME");
        //bundle.putString("NAME",SGName);
        Intent intent = new Intent(PerformingActivity.this, StatisticsActivity.class);
        startActivity(intent);
        PerformingActivity.this.finish();
    }

    float ComboScore(int combo){
        if(combo<0) return 0;
        float re=(float)combo;
        return re*re;
    }

    private void init() {
        view=new DrawCanvas(this);
        view.total=0;
        view.ScreenH=ScreenH;
        view.ScreenW=ScreenW;
        view.preparetime=preparetime;
        view.duration=mediaplayer.getDuration();
        view.setX(0);
        view.setY(0);
        //通知view组件重绘
        view.invalidate();
        rl.addView(view);
        //System.out.println("canvas");
    }

    public int CalculateMouse(float x,float y){
        final int starth=ScreenH/10;
        final int endh=ScreenH*7/8;
        final int linew=ScreenH/100;
        final int USTW=ScreenW*3/4/36;//UpSoundTrackWidth   总共占3/4
        final int BSTW=ScreenW/36;//BottomSoundTrackWidth   左右留1/2
        float a=(y-starth)/(endh-starth);
        float width=((BSTW-USTW)*a+USTW);
        int sx=ScreenW/8+USTW;
        int ex=BSTW;
        float hx=sx+(ex-sx)*a-width/2;
        int num=Math.round((x-hx)/width)+1;
        TextMouse.setText(String.valueOf(num));
        return num;
    }

    public int Inta(String a,int st){
        int x=a.codePointAt(st-1)-48;
        if(x>10)x-=7;
        return x;
    }

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

        final Handler handler=new Handler() {
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what!=-1) {
                    TextPosition.setText(String.valueOf(msg.what));
                    //RefreshSoundTrack
                    while(nowKeyID<=keynum&&(key[nowKeyID].type!="Key"||key[nowKeyID].place*1000+sparetime1-preparetime<msg.what)){
                        if(key[nowKeyID].type!="Key"){
                            nowKeyID++;
                        }else{

                            if(key[nowKeyID].start!=0) {
                                for (int i = key[nowKeyID].start; i <= key[nowKeyID].end; i++) {
                                    soundtrack[i].addkey(nowKeyID,key[nowKeyID]);
                                }
                            }//0代表休止
                            nowKeyID++;
                        }
                    }

                    //DrawSoundTrack();
                    view.total=0;
                    view.cp=msg.what;
                    view.completeness=score.score/score.totScore;
                    for(int i=1;i<=35;i++){
                        //draw soundtrack[i];
                        int t=soundtrack[i].gett();
                        int w=soundtrack[i].getw();
                        for(int j=t;j<=w;j++){
                            if(soundtrack[i].inquene[j]){
                                float aa=(msg.what-soundtrack[i].queueKey[j].place*1000-sparetime1)/preparetime*100;
                                if(aa>=20){
                                    System.out.println(aa);
                                    for(int ii=soundtrack[i].queueKey[j].start;ii<=soundtrack[i].queueKey[j].end;ii++) {
                                        soundtrack[i].del(soundtrack[i].queue[j]);
                                    }
                                    score.Combo=0;
                                    //琴键自然死亡，Combo重置
                                }else {
                                    view.total++;
                                    view.keys[view.total] = soundtrack[i].queueKey[j].start;
                                    view.keye[view.total] = soundtrack[i].queueKey[j].end;
                                    view.keyp[view.total] = soundtrack[i].queueKey[j].place;
                                    view.keyt[view.total] = 'S';//simple
                                }
                            }
                        }
                    }
                    view.invalidate();
                }else {
                    TextPosition.setText("Over");
                    timer.cancel();
                }
            }
        };

        timerTask=new TimerTask() {
            int i=10;
            @Override
            public void run() {
                Message message=Message.obtain();
                if(mediaplayer!=null&&mediaplayer.isPlaying()) {
                    message.what = mediaplayer.getCurrentPosition();
                }else{
                    message.what = -1;
                    SoundOver=true;
                    mediaplayer.release();
                }
                handler.sendMessage(message);
            }
        };

        ImStop=new ImageView(this);
        rl.addView(ImStop);
        ImStop.setX(ScreenW/18);
        ImStop.setY(ScreenW/9);
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ScreenW/18,ScreenW/18);
        ImStop.setLayoutParams(lp);
        ImStop.setScaleType(ImageView.ScaleType.FIT_XY);
        ImStop.setImageResource(R.drawable.back2);
        ImStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Close();
            }
        });
        //Back 按钮

        TextPosition=new TextView(this);
        rl.addView(TextPosition);
        TextPosition.setX(500);
        TextPosition.setY(500);
        TextPosition.setHeight(100);
        TextPosition.setWidth(300);
        TextPosition.setText("preparing");

        TextMouse=new TextView(this);
        rl.addView(TextMouse);
        TextMouse.setX(500);
        TextMouse.setY(650);
        TextMouse.setHeight(100);
        TextMouse.setWidth(300);
        TextMouse.setText("Mouse");

        TextScore=new TextView(this);
        rl.addView(TextScore);
        TextScore.setX(ScreenW*9/10);
        TextScore.setY(0);
        TextScore.setHeight(100);
        TextScore.setWidth(300);
        TextScore.setTextSize(30);
        TextScore.setTextColor(Color.GRAY);
        TextScore.setText("0.00%");



        Bundle bundle=this.getIntent().getExtras();
        String SoundName=bundle.getString("NAME");
        //获取歌曲名

        //积分板块init
        score.score=0;
        score.totKey=0;
        for(int i=0;i<=5;i++){
            score.Judge[i]=0;
        }
        score.Combo=0;
        score.totScore=0;
        //积分板块init

        String filename="key"+SoundName+".txt";
        filename="142.txt";
        //System.out.println(filename);
        Scanner filescanner=null;
        InputStream inputstream=null;
        AssetManager assetmanager=getAssets();

        try{
            inputstream=assetmanager.open(filename);
            filescanner=new Scanner (inputstream);
            int i=0;
            while(filescanner.hasNextLine()){
                i++;
                reader[i]=new String(filescanner.nextLine());
                //System.out.println(reader[i]);
            }
            lenreader=i;
        }catch(Exception e){
            //what to do? i do not know!
            System.out.println(e.getStackTrace());
        }
        int i;
        keynum=0;
        for(i=1;i<=lenreader;i++){
            String regex="[\\[\\(\\<]";
            String[] stringkey=reader[i].split(regex);
            int  cut=0;
            for(String a:stringkey){
                //System.out.println("a="+a);
                if(a.indexOf(')')!=-1){
                    // (n~)
                    switch (a.charAt(a.indexOf(')')-1)){
                        case '~':
                            cut=Integer.parseInt(a.substring(0,a.indexOf(')')-1));
                            break;
                        default:
                            break;
                    }
                }else if(a.indexOf('>')!=-1){
                    int place=a.indexOf(':');
                    switch (a.substring(0,place)){
                        case "Speed":
                            keynum++;
                            key[keynum]=new Key();
                            key[keynum].start=0;
                            key[keynum].end=0;
                            key[keynum].type="Speed";
                            key[keynum].data=Integer.parseInt(a.substring(place+1,a.indexOf('>')));
                            speed=key[keynum].data;
                            //System.out.println(key[keynum].data);
                            //System.out.println(key[keynum].type);
                            break;
                        case "Length":
                            keynum++;
                            key[keynum]=new Key();
                            key[keynum].start=0;
                            key[keynum].end=0;
                            key[keynum].type="Length";
                            key[keynum].data=Integer.parseInt(a.substring(place+1,a.indexOf('>')));
                            length=key[keynum].data;
                            //System.out.println(key[keynum].data);
                            //System.out.println(key[keynum].type);
                            break;
                        default:
                            break;
                    }
                }else if(a.indexOf(']')!=-1){
                    int place1=a.indexOf(']');
                    float atime=1;
                    while(a.charAt(a.length()-1)=='\\')a=a.substring(0,a.length()-1);
                    if(place1<a.length()-1) {
                        String aa = a.substring(place1 + 1);
                        int laa = aa.length()-1;
                        int iaa = 0;
                        //System.out.println(aa);
                        while (iaa<=laa){
                            switch (aa.charAt(iaa)){
                                case '-':
                                    atime=atime+1;
                                    break;
                                case '_':
                                    atime=atime/2;
                                    break;
                                case '.':
                                    float dotnum=1;
                                    dotnum=dotnum/2;
                                    while(iaa+1<=laa&&aa.charAt(iaa+1)=='.'){
                                        iaa++;
                                        dotnum=dotnum/2;
                                    }
                                    atime=atime*(2-dotnum);
                                    break;
                                default:
                                    break;
                            }
                            //System.out.println(aa.charAt(iaa));
                            //System.out.println(atime);
                            iaa++;
                        }
                    }
                    if(cut!=0)atime=atime*ContinueSound[cut]/cut;
                    //System.out.println(atime);
                    int tt=keynum;
                    while(tt>0&&key[tt].type!="Key"){
                        tt--;
                    }
                    float aatime;
                    if(tt!=0)aatime=key[tt].place+key[tt].time*60/key[tt].speed;else aatime=0;
                    //System.out.println(aatime);
                    //计算时长
                    a=a.substring(0,place1);
                    //System.out.println(a);
                    while(a!=""){
                        int place2=a.indexOf(',');
                        String aa;
                        if(place2!=-1) {
                            aa = a.substring(0, place2);
                        }else{
                            aa=a;
                        }
                        if(aa.length()==1){
                            keynum++;
                            key[keynum]=new Key();
                            key[keynum].start = Inta(aa,1);
                            key[keynum].end = key[keynum].start+length-1;
                            key[keynum].type = "Key";
                            key[keynum].data = 0;
                            key[keynum].time=atime;
                            key[keynum].speed=speed;
                            key[keynum].place=aatime;

                            score.totKey++;//统计出现的键的个数

                            //System.out.println(key[keynum].place);
                            //System.out.println(key[keynum].type);
                        }else if(aa.length()==2){
                            keynum++;
                            key[keynum]=new Key();
                            key[keynum].start = Inta(aa,1);
                            key[keynum].end = Inta(aa,2);
                            key[keynum].type = "Key";
                            key[keynum].data = 0;
                            key[keynum].time=atime;
                            key[keynum].speed=speed;
                            key[keynum].place=aatime;

                            score.totKey++;//统计出现的键的个数

                            //System.out.println(key[keynum].place);
                            //System.out.println(key[keynum].type);
                        }
                        if(place2==-1){
                            a="";
                        }else{
                            a=a.substring(place2+1);
                        }
                    }
                    cut=0;
                }
            }
        }
        //将文件解析完成


        simglekeyscore=ComboScore(score.totKey)/3*7/score.totKey;
        score.totScore=score.totKey*simglekeyscore+ComboScore(score.totKey);//计算总分（未归一化）
        //System.out.println("Score");
        //System.out.println(simglekeyscore);
        //System.out.println(score.totScore);
        //System.out.println("Score");

        mediaplayer=new MediaPlayer();
        mediaplayer.reset();
        String musicname;
        musicname="music"+SoundName+".mp3";
        musicname="test.mp3";

        try {
            AssetFileDescriptor fileDescriptor = getAssets().openFd(musicname);
            mediaplayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            mediaplayer.prepare();
        }catch(Exception e){
            //???
            System.out.println("Fail to get resource");
        }

        //if(mediaplayer==null)System.out.println("null");else System.out.println("start");
        //System.out.println(mediaplayer.getDuration());

        //歌曲准备结束
        testnum=0;


        SoundOver=false;
        mediaplayer.start();
        nowKeyID=1;
        nowKeyTime=0;
        for(int si=1;si<=35;si++){
            soundtrack[si]=new SoundTrack();
        }
        init();//Canvas的初始化
        timer=new Timer();
        timer.schedule(timerTask,0,refresh);
        //Timer

    }


    public boolean onTouchEvent(android.view.MotionEvent event) {
        if(!SoundOver) {
            delKey delkey = new delKey();
            float x1, x2, y1, y2, x3, y3;
            int STonClick;
            int pointnum = event.getPointerCount();
            //System.out.println(pointnum);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    x1 = event.getX();
                    y1 = event.getY();
                    STonClick = CalculateMouse(x1, y1);
                    //TextScore.setText("Down");


                    if (STonClick >= 0 && STonClick <= 35) {
                        delkey = soundtrack[STonClick].judge(mediaplayer.getCurrentPosition(), preparetime, sparetime1, 'B');
                        if (delkey.score > 0) {
                            //System.out.println("id");
                            //System.out.println(delkey.id);
                            //System.out.println(String.valueOf(STonClick));
                            testnum++;
                            for (int i = delkey.start; i <= delkey.end; i++) {
                                soundtrack[i].del(delkey.id);
                            }
                            score.Combo++;
                            score.Judge[delkey.score]++;
                            score.score=score.score+simglekeyscore+ComboScore(score.Combo)-ComboScore(score.Combo-1);//计算分数
                            int as=(int)(score.score/score.totScore*10000);
                            float ass=as/100;
                            TextScore.setText(String.valueOf(ass)+"%");
                            //System.out.println("Combo");
                            //System.out.println(score.Combo);
                            //System.out.println(score.score);
                            //System.out.println("Combo");
                        }
                    }

                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    x2 = event.getX();
                    y2 = event.getY();
                    STonClick = CalculateMouse(x2, y2);
                    //TextScore.setText("Move");
                    //if(STonClick>=0&&STonClick<=35)soundtrack[STonClick].judge(mediaplayer.getCurrentPosition());
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    x3 = event.getX();
                    y3 = event.getY();
                    STonClick = CalculateMouse(x3, y3);
                    //TextScore.setText("UP");
                    //if(STonClick>=0&&STonClick<=35)soundtrack[STonClick].judge(mediaplayer.getCurrentPosition());
                    break;
                }
            }
        }
        return true;
    };

}
