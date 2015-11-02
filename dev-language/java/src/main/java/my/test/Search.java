package my.test;

/**
 * Created by cuixiaofeng on 15/9/1.
 */
public class Search {
 public int getNumber(int[] datas,int num) {
     int beginNum = 0;
     int endNum = datas.length;
     int number = endNum/2;
     while (datas[number] != num) {
         if (num > datas[number]) {
             beginNum = endNum / 2;
         } else {
             endNum = endNum / 2;
         }
         number = (beginNum+endNum) / 2;
         if(beginNum == endNum){
             return -1;
         }
     }
     return number;
 }
}
