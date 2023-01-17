package com.user.app;

import java.util.*;

public class Problem3Sum {
    public static void main(String[] args) {

        System.out.println(threeSum(new int[]{-1,0,1,2,-1,-4}));
    }
//    [-1, 0, 1, 2, -1, -4]
//            [[-1, 0, 1], [-1, -1, 2]]

    public static List<List<Integer>> threeSum(int[] nums) {
        System.out.println(Arrays.toString(nums));
         Arrays.sort(nums);
         Set<List<Integer>> set = new HashSet<>();
        List < List < Integer >> result = new ArrayList < List < Integer >> ();

        for(int i = 0; i< nums.length-2; i++ ){
            int j = i+1;
            int k = nums.length-1;
            while (j<k){
                int sum = nums[i]+nums[j]+nums[k];
                if(sum==0){
                    List < Integer > triplet = new ArrayList< Integer >();
                    triplet.add(nums[i]);
                    triplet.add(nums[j]);
                    triplet.add(nums[k]);
//                    Collections.sort(triplet);
                    set.add(triplet);
                    j++;
                    k--;
                }else if(sum>0){
                    k--;
                }else{
                    j++;
                }
            }
        }
        /*for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                for (int k = j + 1; k < nums.length; k++) {
                    if (nums[i] + nums[j] + nums[k] == 0) {
                        List < Integer > triplet = new ArrayList< Integer >();
                        triplet.add(nums[i]);
                        triplet.add(nums[j]);
                        triplet.add(nums[k]);
                        Collections.sort(triplet);
                        result.add(triplet);
                    }
                }
            }
        }*/
//        result = new ArrayList < List < Integer >> (new LinkedHashSet< List < Integer >>(result));
        return new ArrayList<>(set);
    }
}
