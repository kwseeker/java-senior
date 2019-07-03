package top.kwseeker.javafunctional.testcase.chp4_4;

@FunctionalInterface
public interface FunctionInterfaceTest {

    // 抽象方法有且只有一个
    void sub();

    // 默认是 public abstract
    //void add();

    // java.lang.Object中的方法不是抽象方法
    boolean equals(Object var1);

    // default方法是java1.8的新特性（类似C++的虚函数）
    //      default方法是指，在接口内部包含了一些默认的方法实现（也就是接口中可以包含方法体，这打破了Java之前版本对接口的语法限制），
    //      从而使得接口在进行扩展的时候，不会破坏与接口相关的实现类代码。
    //      同时继承两个接口的同名default方法会报错
    // default不是抽象方法
    default void defaultMethod(){
        //这里可以定义默认实现
        //...
    }

    // static不是抽象方法
    static void staticMethod(){
    }
}

//补充：
//接口变量默认是 public static final
//接口方法默认是 public abstract