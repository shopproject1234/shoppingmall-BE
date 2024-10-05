package com.sangwook.shoppingmall.exception;

public class MethodFunction {

    public static String getMethodName() {
        return "class : " + Thread.currentThread().getStackTrace()[2].getClassName() + ", method : " + Thread.currentThread().getStackTrace()[2].getMethodName();
    }

}
