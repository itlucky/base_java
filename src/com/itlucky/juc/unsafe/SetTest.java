package com.itlucky.juc.unsafe;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * 多个线程往Set集合中塞入值
 * java.util.ConcurrentModificationException
 *
 * 同理(ListTest)可证这里的Set集合问题。
 * Set集合底层是HashMap
 */
public class SetTest {

    public static void main(String[] args) {
        //        Set<String> set = new HashSet<>();
        //        Set<String> set = Collections.synchronizedSet(new HashSet<>());
        Set<String> set = new CopyOnWriteArraySet<>();

        for (int i = 1; i<=30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 3));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }
}
