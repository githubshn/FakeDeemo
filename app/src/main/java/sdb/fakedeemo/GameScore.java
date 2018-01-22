package sdb.fakedeemo;

/**
 * Created by sxh on 2018/1/22.
 */

public class GameScore {
    int totKey;//按键总数
    int Combo;//连击
    int[] Judge=new int[10];//技术统计
    float score;//未归一化得分
    float totScore;//归一化条件
}
