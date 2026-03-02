import java.util.*;

public class HTB {

    public static void twoSum(int[] arr, int target){
        Map<Integer,Integer> map = new HashMap<>();

        for(int num: arr){
            int comp = target - num;
            if(map.containsKey(comp)){
                System.out.println("Pair: "+num+" + "+comp);
            }
            map.put(num,1);
        }
    }

    public static void main(String[] args){
        int[] tx = {500,300,200,700};
        twoSum(tx,500);
    }
}