# Java 函数式编程

系统学习Java函数式编程，参考《Java 8 函数式编程》

[函数式编程概念讨论](https://github.com/justinyhuang/Functional-Programming-For-The-Rest-of-Us-Cn/blob/master/FunctionalProgrammingForTheRestOfUs.cn.md)

## <font color="blue">1 简介</font>

## <font color="blue">2 Lambda表达式</font>

## <font color="blue">3 流</font>

## <font color="blue">4 类库</font>

### 4.4 @FunctionalInterface（函数式接口检查）

这个注解是为了表示接口是函数式接口，函数式接口要求有且只能含有一个抽象方法（SAM接口，即Single Abstract Method interfaces）；
定义这种接口是为了使用Lambda表达式。

该注释会强制javac 检查一个接口是否符合函数接口的标准。如果该注释添加给一个枚举
类型、类或另一个注释，或者接口包含不止一个抽象方法，javac 就会报错。重构代码时，
使用它能很容易发现问题。

## <font color="blue">5 高级集合类和收集器</font>

## <font color="blue">6 数据并行化</font>

## <font color="blue">7 测试、调试和重构</font>

## <font color="blue">8 设计和架构的原则</font>

## <font color="blue">9 使用Lambda表达式</font>

## <font color="blue">10 下一步该怎么办</font>

 