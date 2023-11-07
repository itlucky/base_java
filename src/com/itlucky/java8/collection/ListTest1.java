package com.itlucky.java8.collection;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author itlucky
 * @date 2023/6/18 0:16
 */
public class ListTest1 {

    public static void main(String[] args) {
        Object[] array = new Object[37];
        for (int i = 0; i < array.length; i++) {
            array[i] = new Object();
        }
        Arrays.sort(array, new Comparator<Object>() {
            private int result[] = {1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1, 1, -1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, 1, -1, 1, -1, -1, 1, 1, -1, -1, 1, 1, 1, 1, 1, 1, -1, -1, 0, -1, -1, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, 1, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
            private int index;
            @Override
            public int compare(Object o1, Object o2) {
                return result[index++];
            }
        });
    }
}
