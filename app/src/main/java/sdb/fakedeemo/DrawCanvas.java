package sdb.fakedeemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by sxh on 2018/1/18.
 */

public class DrawCanvas extends View{
    public int ScreenH,ScreenW;
    public int[] keys=new int[1000];
    public int[] keye=new int[1000];
    public float[] keyp=new float[1000];
    public char[] keyt=new char[1000];
    public int total;
    //start end place type
    public int cp;//currentpossition
    public int preparetime;

    public DrawCanvas(Context context){
        super(context);
    }

    public void drawkey(Canvas canvas,Paint paint,int s,int e,float time,char type){

        final int starth=ScreenH/10;
        final int endh=ScreenH*7/8;
        final int linew=ScreenH/100;
        final int USTW=ScreenW*3/4/36;//UpSoundTrackWidth   总共占3/4
        final int BSTW=ScreenW/36;//BottomSoundTrackWidth   左右留1/2

        int keywidth=linew;
        float a=(cp-time*1000)/preparetime;
        int height=starth+Math.round((endh-starth)*a);
        int width=Math.round(((BSTW-USTW)*a+USTW));
        int sx=ScreenW/8+USTW*s;
        int ex=BSTW*s;
        int x=Math.round(sx+(ex-sx)*a-width/2);
        int w=Math.round(width*(e-s+1));
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, height-keywidth, x+w, height+keywidth, paint);// 绘制琴键
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        final int starth=ScreenH/10;
        final int endh=ScreenH*7/8;
        final int linew=ScreenH/100;
        final int USTW=ScreenW*3/4/36;//UpSoundTrackWidth   总共占3/4
        final int BSTW=ScreenW/36;//BottomSoundTrackWidth   左右留1/2

        Paint paint=new Paint();
        paint.setColor(Color.GRAY);// 设置灰色
        paint.setStyle(Paint.Style.FILL);//设置填满
        //System.out.println("ScreenH");
        //System.out.println(ScreenH);
        canvas.drawRect(0, starth, ScreenW, starth+linew, paint);// 上分割线
        canvas.drawRect(0, endh, ScreenW, endh+linew, paint);// 下分割线
        //canvas.drawRect(0,0,300,300,paint);

        //下面是辅助绘制的音轨
        for(int i=1;i<=35;i++){
            canvas.drawLine(ScreenW/8+USTW*i,starth,BSTW*i,endh,paint);
        }
        //音轨绘制完成
        for(int i=1;i<=total;i++){
            drawkey(canvas,paint,keys[i],keye[i],keyp[i],keyt[i]);
        }

    }
}
