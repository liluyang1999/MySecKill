package com.example.lly.util.algorithm;

public class InsertSort {

    public static int[] sort(int[] list) {
        int[] sortedList = list.clone();
        int length = sortedList.length;
        for(int i = 1; i < length; i++) {
            int pointer = i;
            for(int j = i - 1; j >= 0; j--) {
                if(sortedList[j] <= sortedList[pointer]) {
                    break;
                } else {
                    int temp = sortedList[pointer];
                    sortedList[pointer] = sortedList[j];
                    sortedList[j] = temp;
                    pointer = j;
                }
            }
        }
        return sortedList;
    }

    public static void main(String[] args) {
        int[] list1 = {1,2,2,5,3,7,5,3};
        int[] newList = InsertSort.sort(list1);
        for(int each : newList) {
            System.out.print(each + " ");
        }
    }



}
