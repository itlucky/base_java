package com.itlucky.base;

import com.itlucky.entity.CustInfo;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


// 通过反射获取泛型
public class Reflection3 {

    public void test01(Map<String, CustInfo> map, List<CustInfo> custInfos){
        System.out.println("test01-TODO");
    }

    public Map<String,CustInfo> test02(){

        System.out.println("test02-TODO");
        return null;
    }

    /**
     * 通过以下方法来获取，其实逻辑很简单，就是单词看起来复杂，实际没有啥
     * method.getGenericParameterTypes(): 获取泛型的参数信息 返回Type[]
     * method.getGenericReturnType():  获取泛型的返回值类型  返回Type
     */
    public static void main(String[] args)
        throws NoSuchMethodException {
        // 通过反射获得方法
        Method method = Reflection3.class.getMethod("test01", Map.class, List.class);
        // 获得泛型的参数信息getGenericParameterTypes()
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        //genericParameterType:泛型的参数类型
        for (Type genericParameterType : genericParameterTypes) {
            System.out.println("###"+genericParameterType);
            //ParameterizedType:表示一种参数化类型，比如Collection<String>
            if(genericParameterType instanceof ParameterizedType){
                // 获得真实参数类型
                Type[] actualTypeArguments = ((ParameterizedType)genericParameterType).getActualTypeArguments();
                for (Type actualTypeArgument : actualTypeArguments) {
                    System.out.println(actualTypeArgument);
                }
            }
        }

        System.out.println("**************************************");
        method = Reflection3.class.getMethod("test02");
        Type genericReturnType = method.getGenericReturnType();
        if(genericReturnType instanceof ParameterizedType) {
            System.out.println(genericReturnType);
            Type[] actualTypeArguments = ((ParameterizedType)genericReturnType).getActualTypeArguments();
            for (Type actualTypeArgument : actualTypeArguments) {
                System.out.println(actualTypeArgument);
            }
        }

    }

}
