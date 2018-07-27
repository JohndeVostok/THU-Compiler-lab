# 编译原理 Decaf/Mind PA 1-B实验报告

计54 马子轩

## 实验目的

掌握错误恢复与LL(1)文法分析

## 实验内容

实现编译器的错误恢复,使用LL(1)文法添加功能

## 实验过程

### 错误恢复

编辑Parser.java

```java
if (result == null)
{
	error();
	while (result == null)
	{
		if (followSet(symbol).contains(lookahead)) return null;
		if (follow.contains(lookahead)) return null;
		lookahead = lex();
		result = query(symbol, lookahead);
	}
}
```

添加错误情况的判断，并在末尾根据产生式null的情况返回.

### 添加语法

由于PA1A的文法改造成LL(1)文法,移植到PA1B即可。

其中CASE语句PA1A中就是LL(1)文法

DOOD语句将2段改为3段即可.和StmtList处理方式一致。

## 测试

1. 通过制定优先级来使分析器能分析，我们定义ElseClause变化成else Stmt高优先度，这时候就能正常识别了.

```java
class Main {
    static void main() {
    	if (true) if (true) {} else {};
    }
}
```

分析时else被分在了第二个if中，说明else优先度高.

2. 误报情况

多个终结符}能够屏蔽后面所有的错误.

```java
class Main {
    static void main() {
    	if (true) if (true) {} else {}}};
    	xxxxxxxxxxxxxxx
    }
}
```

因为这个过程中stmt被判定结束了.之后没有继续分析.这时候就不能准确的查找语法错误了.

## 实验总结

对LL(1)文法有了更深入的了解，对错误分析有了基本的认识。这个过程中认识到了PA1A中程序的部分不足。
