# 编译原理 Decaf/Mind PA 1-A实验报告

计54 马子轩

## 实验目的

掌握LEX与YACC的使用方法。了解Decaf的基本结构。

## 实验内容

实现将所给decaf语言编译成语法树
1、	添加整复数类型支持
2、	Case表达式支持
3、	Super表达式支持
4、	对象复制的支持
5、	卫士表达式的支持

## 实验过程

1. 对lexer.l进行修改.添加相应关键词与符号

@ $ # :
Complex case default super dcopy scopy do od
|||
虚数的识别

2. 对Parser.y进行修改.添加相应的处理函数

注册关键词，构造上下文无关文法处理相应语句。并调用tree的函数打印。

3. 对Tree.java进行修改.

构造语法树，其中Case的编号出现了错误.可能是原有case被删除但没有把前后连接起来。
对新增功能的节点进行操作，打印语法树。

## 测试

1. 针对复数的测试形如1+j时，j会被分析为var，1+1j会被分析为complex。
2. 针对case语句测试了没有default 只有default 都没有的情况。
3. Super 和copy测试较为简单
4. 对do od的测试中测试了内容为空的情况，部分缺少“|||”的情况。多”|||”的情况，还有仅有一个的情况。

## 实验总结

学习了LEX和YACC的使用方法。能够对代码进行词法分析和语法分析，成功的构造语法树。