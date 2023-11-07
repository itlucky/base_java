package com.itlucky.java8.streamAPI;

import com.itlucky.entity.CustInfo;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

/**
 *  Stream的操作三个步骤
 *    1.创建Stream
 *      一个数据源（如：集合、数组），获取一个流
 *    2.中间操作
 *      一个中间操作链，对数据源的数据进行处理
 *    3.终止操作（终端操作）
 *      一个终止操作，执行中间操作链，并产生结果
 */

public class StreamTest01 {

    List<CustInfo> custInfos = Arrays.asList(
            new CustInfo(1001,"332101","张三",18,"高新区", BigDecimal.valueOf(9999.91)),
            new CustInfo(1002,"332102","李四",16,"蜀山区", BigDecimal.valueOf(8888.01)),
            new CustInfo(1003,"332103","王五",17,"瑶海区", BigDecimal.valueOf(10888.01)),
            new CustInfo(1004,"332104","赵六",19,"经开区", BigDecimal.valueOf(7888.01)),
            new CustInfo(1005,"332105","李七",18,"新站区", BigDecimal.valueOf(6666.61)),
            new CustInfo(1006,"332106","刘八",19,"包河区", BigDecimal.valueOf(5555.55)),
            new CustInfo(1007,"332107","王明",15,"庐阳区", BigDecimal.valueOf(4444.333)),
            new CustInfo(1008,"332108","小红",16,"巢湖区", BigDecimal.valueOf(2222.333)),
            new CustInfo(1002,"332102","李四",16,"蜀山区", BigDecimal.valueOf(8888.01)),
            new CustInfo(1002,"332102","李四",16,"蜀山区", BigDecimal.valueOf(8888.01)),
            new CustInfo(1002,"332102","李四",16,"蜀山区", BigDecimal.valueOf(8888.01))
    );

    //中间操作
    /**
     * 中间操作：
     * 一、筛选与切片
     *   filter：接收 Lambda ，从流中排除某些元素
     *   limit：截断流，使其元素不超过给定数量
     *   skip(n)：跳过元素，返回一个舍弃了前n个元素的流；若流中元素不足n个，则返回一个空流；与 limit(n) 互补
     *   distinct：筛选，通过流所生成的 hashCode() 与 equals() 取除重复元素。所以一般实体类中要重写这俩方法
     *
     * 二、映射
     *   map：接收 Lambda ，将元素转换为其他形式或提取信息；接受一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素
     *   flatMap：接收一个函数作为参数，将流中每一个值都换成另一个流，然后把所有流重新连接成一个流
     *
     * 三、排序
     *   sorted() --自然排序 Comparable
     *   sorted(Comparator com) --定制排序 Comparator
     */

    /**
     *  filter:
     */
    //内部迭代：迭代操作由Stream API完成
    @Test
    public void test1() {
        //中间操作：不会执行任何操作
        Stream<CustInfo > stream = custInfos.stream()
                                 .filter((c) -> {
                                     System.out.println("Stream API 中间操作");
                                     return c.getAge()>18;
                                 });
        //终止操作：一次性执行全部内容，即“惰性求值”
        stream.forEach(System.out::println);
    }

    //外部迭代：
    @Test
    public void test2() {
        Iterator<CustInfo> ci = custInfos.iterator();

        while (ci.hasNext()){
            System.out.println(ci.next());
        }
    }

    /**
     * limit：
     * 执行结果会发现只遍历了两次，其实跟limit结合使用相当于是“短路”操作。
     * 相当于逻辑或、逻辑与关系
     * 如下：也就是能找到2条符合条件的就不会进行其他遍历。
     */
    @Test
    public void test3(){
        custInfos.stream()
                 .filter(a -> {
                     System.out.println("短路"); // && ||
                     return a.getMoney().compareTo(BigDecimal.valueOf(5000l))==1;
                 })
                 .limit(2)
                 .forEach(System.out::println);
    }


    /**
     * skip：
     * 跳过符合条件的前2个
     * 与limit(2)互补，limit(2)：截取符合条件前2个
     */
    @Test
    public void test4(){
        custInfos.stream()
                 .filter(x -> x.getMoney().compareTo(BigDecimal.valueOf(5000l)) == 1)
                 .skip(2)
                 .forEach(System.out::println);
    }

    /**
     * distinct：
     * distinct() 是按照equals和hashcode的方法进行去重，所以要重写实体类中的这俩方法
     */
    @Test
    public void test5(){
        custInfos.stream()
                 .filter(a -> a.getMoney().compareTo(BigDecimal.valueOf(7000l)) ==1)
                 .distinct()
                 .forEach(System.out::println);
    }

    /**
     * map:
     *  接收 Lambda ，将元素转换为其他形式或提取信息；接受一个函数作为参数，该函数会被应用到 每个元素 上，并将其映射成一个新的元素
     *
     * filterMap:
     *  接收一个函数作为参数，将流中每一个值都换成另一个流，然后把所有流重新连接成一个流
     *
     *  其实map和filterMap有点类似于集合中的add()和addAll()的区别。add是将集合添加到新集合中，addAll是将集合中的元素添加到新集合中
     */
    @Test
    public void test6(){
        List<String> list = Arrays.asList("aaa","bbb","ccc","ddd","eee");
        //将集合中每个元素都转成大写
        list.stream()
            .map(str -> str.toUpperCase())
            .forEach(System.out::println);

        System.out.println("---------------");
        //获取集合中每个元素指定属性
        custInfos.stream()
                 .map(CustInfo::getName)
                 .forEach(System.out::println);

        System.out.println("---------------");

        Stream<Stream<Character>> stream = list.stream()
                                               .map(StreamTest01::filterCharacter); //{{a,a,a},{b,b,b},{c,c,c}.....}
        stream.forEach( sm -> {
            sm.forEach(System.out::println);
        });

        System.out.println("---------------");

       Stream<Character> characterStream = list.stream()
            .flatMap(StreamTest01::filterCharacter);  ////{a,a,a,b,b,b,c,c,c.....}
       characterStream.forEach(System.out::println);

    }

    //把字符串中一个个字符提取出来
    public static Stream<Character> filterCharacter(String str){
        List<Character> list = new ArrayList<>();

        for (Character ch : str.toCharArray()){
            list.add(ch);
        }
        return list.stream();
    }

    /**
     *  sorted() --自然排序 Comparable
     *  sorted(Comparator com) --定制排序 Comparator
     */
    @Test
    public void test7(){
        List<String> list = Arrays.asList("bbb","ddd","aaa","ccc","eee");
        list.stream()
                //这里就是按照String中写好的排序方法排
            .sorted()
            .forEach(System.out::println);

        System.out.println("================");
        //按照年龄倒序排，年龄一样按照姓名排序
        custInfos.stream()
                 .sorted((c1,c2) -> {
                     if(c1.getAge() == c2.getAge()){
                         return c1.getName().compareTo(c2.getName());
                     } else {
                         return -Integer.compare(c1.getAge(),c2.getAge());
                     }
                 })
                .forEach(System.out::println);
    }



}