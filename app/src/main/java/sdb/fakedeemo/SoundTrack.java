package sdb.fakedeemo;

public class SoundTrack {
    final int max=1000;
    public int[] queue=new int[1000];
    public Key[] queueKey=new Key[1000];
    public boolean[] inquene=new boolean[1000];
    private int t=0,w=-1;
    public int gett(){
        return t;
    }
    public int getw(){
        return w;
    }

    public void addkey(int keyid,Key key){
        w++;
        queue[w]=keyid;
        queueKey[w]=new Key();
        queueKey[w]=key;
        inquene[w]=true;
    }
    public void del(int del){
        for(int i=t;i<=w;i++){
            if(inquene[i]&&queue[i]==del){
                inquene[i]=false;
                return;
            }
        }
    }

    public delKey judge(int cp,int preparetime,int sparetime,char type){
        delKey re=new delKey();
        re.score=0;
        while(inquene[t]==false&&t<=w){
            t++;
        }
        if(t>w)return re;
        System.out.println('t');
        System.out.println(String.valueOf(t));
        float a=((queueKey[t].place*1000+sparetime)-cp)/preparetime*100;
        System.out.println(a);
        switch (type){
            case 'B':
                if(-10<=a&&a<=10){
                    re.score=1;
                    re.start=queueKey[t].start;
                    re.end=queueKey[t].end;
                    re.id=queue[t];
                }else if((a>=-20&&a<-10)||(a>10&&a<=25)){
                    re.score=2;
                    re.start=queueKey[t].start;
                    re.end=queueKey[t].end;
                    re.id=queue[t];
                }else if(a>25&&a<=50){
                    re.score=3;
                    re.start=queueKey[t].start;
                    re.end=queueKey[t].end;
                    re.id=queue[t];
                }else if(a<-20){
                    re.score=-1;
                    re.start=queueKey[t].start;
                    re.end=queueKey[t].end;
                    re.id=queue[t];
                }
                break;
            default:
                break;
        }
        return re;
    }

}
