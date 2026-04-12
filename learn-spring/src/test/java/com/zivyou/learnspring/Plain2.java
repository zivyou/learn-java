package com.zivyou.learnspring;


import java.util.Scanner;
import java.util.Arrays;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class Plain2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        int n, k;
        String s;
        n = in.nextInt();
        k = in.nextInt();
        in.nextLine();
        s = in.nextLine();

        if (s.length() <= 0) {
            System.out.println(0);
            return;
        }
        int lowcaseCount  = 0; int upcase = 0;
        for (char c : s.toCharArray()) {
            if ('a'<= c && c<='z')
                lowcaseCount++;
            else
                upcase++;
        }
        if (k <= lowcaseCount) {
            System.out.println(k+upcase);
            return;
        }
        int remaind = (k-lowcaseCount);
        System.out.println(String.format("k=%d, lowcase=%d", k, lowcaseCount));
        if (remaind % 2 ==0) {
            System.out.print(n);
        } else {
            System.out.print(n-1);
        }
    }
}
