package com.example.lly.util.algorithm;

public class BubbleSort {

    public static int[] sort(int[] list) {
        int[] sortedList = list.clone();
        int length = sortedList.length;
        for(int i = 0; i < length - 1; i++) {
            for(int j = 0; j < length - 1 - i; j++) {
                if(sortedList[j] > sortedList[j + 1]) {
                    int temp = sortedList[j];
                    sortedList[j] = sortedList[j + 1];
                    sortedList[j + 1] = temp;
                }
            }
        }
        return sortedList;
    }


    public static void main(String[] args) {
        int[] list1 = {1,3,2,5,2,4,1,2};
        list1 = BubbleSort.sort(list1);
        for(int each : list1) {
            System.out.print(each + " ");
        }
    }

}
