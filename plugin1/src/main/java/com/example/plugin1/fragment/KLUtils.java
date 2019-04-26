package com.example.plugin1.fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/27.
 */

public class KLUtils {
    // 如果出现为0的情况则设置为最大值。
    private static final double max = 999999;
    private static final double ignore = 0.03;
    // p为要比较的原目标，q为当前
    public static double getKLDistance(List<Integer> p,List<Integer> q){
        double sum_p  = 0.0;
        for (Integer integer: p){
            sum_p+= integer;
        }
        List<Double> real_p = new ArrayList<>(p.size());
        for (Integer integer: p){
            real_p.add(integer/sum_p);
        }
        double sum_q  = 0.0;
        for (Integer integer: q){
            sum_q+= integer;
        }
        List<Double> real_q = new ArrayList<>(q.size());
        for (Integer integer: q){
            real_q.add(integer/sum_q);
        }
        int length = p.size();
        double result=0;
        for (int i = 0;i<length;i++){
            if (real_p.get(i) <= ignore || real_q.get(i) <= ignore){
                continue;
            }
            result+= real_p.get(i)*Math.log(real_p.get(i)/real_q.get(i));
        }
        return result;
    }
}
