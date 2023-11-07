package com.itlucky.base;

public class Tets1 {




    private static class Node<E>{
        E item;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev,Node<E> next,E item){
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

}
