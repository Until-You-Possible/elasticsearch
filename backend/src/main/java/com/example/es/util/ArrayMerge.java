package com.example.es.util;

public class ArrayMerge {

    public static Object[] arrayCopyMerge(Object[]... arrays) {

        //数组长度
        int arrayLength = 0;
        //目标数组的起始位置
        int startIndex = 0;

        for(Object[] file : arrays){
            arrayLength = arrayLength + file.length;
        }

        Object[] fileArray = new Object[arrayLength];

        for(int i = 0; i < arrays.length; i++){

            if(i > 0){

                //i为0 时，目标数组的起始位置为0 ,i为1时，目标数组的起始位置为第一个数组长度
                //i为2时，目标数组的起始位置为第一个数组长度+第二个数组长度
                startIndex = startIndex + arrays[i-1].length;
            }
            System.arraycopy(arrays[i], 0, fileArray, startIndex, arrays[i].length);

        }
        return fileArray;
    }

}
