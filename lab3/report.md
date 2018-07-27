# 编译原理 Decaf/Mind PA 3实验报告

计54 马子轩

## 实验目的

在语法制导基础上掌握语法制导的中间翻译方法。对过程调用，面向对象存储布局有一定了解。

## 实验内容

将之前生成的带修饰的抽象语法树翻译成中间表示TAC。两趟扫描，第一次计算每个类对应的对象大小，类成员，虚函数表，创建functy对象。第二趟完成语句的翻译。

1. 添加整复数类型支持

2. Case表达式支持

3. Super表达式支持

4. 对象复制的支持

5. 卫士表达式的支持

## 实验过程

1. 将PA2内容移植到项目。

2. 添加复数类型支持

复数类型我的实现方式是创建一个长度为2的数组，存储数组地址，但是为了保证进行操作的时候不是引用而是直接复制，需要引入特判。

Translator创建对象时候特判成员函数

```java
for (int i = 0; i < c.vars.size(); i++) if (c.vars.get(i).type.equal(BaseType.COMPLEX))
	genStore(genNewArray(genLoadImm4(2)),  newObj, OffsetCounter.WORD_SIZE * (i + 1));
```

我在此处实现的时候多增加了vars列表来表示类的成员变量。另外还需要特判scopy和dcopy

Transpass2时候需要针对赋值特判，运算特判（int + comp, comp + int, comp + comp, int * comp, comp * int, comp * comp），Literal特判，变量定义特判

3. Case表达式

Case较为容易，主要是branch的运用

```java
public void visitCase(Tree.Case caseExpr) {
	caseExpr.expr.accept(this);
	Label exit, next, caseLabel;
	Temp flag = tr.genLoadImm4(0), cond;
	caseExpr.val = Temp.createTempI4();
	for (Tree f : caseExpr.slist)
	{
		Tree.Casedef casebranch = (Tree.Casedef) f;
		caseLabel = Label.createLabel();
		next = Label.createLabel();
		casebranch.accept(this);
		cond = tr.genSub(caseExpr.expr.val, casebranch.c.val);
		tr.genBeqz(cond, caseLabel);
		tr.genBranch(next);
		tr.genMark(caseLabel);
		tr.genAssign(flag, tr.genLoadImm4(1));
		tr.genAssign(caseExpr.val, casebranch.expr.val);
		tr.genMark(next);
	}
	Tree.DefaultCase defaultbranch = (Tree.DefaultCase) caseExpr.stmt;
	caseLabel = Label.createLabel();
	exit = Label.createLabel();
	tr.genBeqz(flag, caseLabel);
	tr.genBranch(exit);
	tr.genMark(caseLabel);
	defaultbranch.accept(this);
	tr.genAssign(caseExpr.val, defaultbranch.expr.val);
	tr.genMark(exit);
}
```

4. Super表达式

Super表达式和this表达式主要是pa2的内容。

```java
Temp vt;
if (callExpr.receiver instanceof Tree.SuperExpr || callExpr.receiver instanceof Tree.ThisExpr)
	vt=tr.genLoadVTable(callExpr.symbol.getScope().getOwner().getVtable());
else vt = tr.genLoad(callExpr.receiver.val, 0);
```

只要在callexpr中添加这一段，再添加visitsuper即可。内容是判断callexpr的类型，并构造相应的vtable。

5. scopy dcopy

具体实现就是先调用构造函数，scopy直接复制变量，对complex特判。而dcopy复制过程中指针递归调用dcopy。

Transpass2中

```java
@Override
public void visitScopy(Tree.Scopy that)
{
	that.expr.accept(this);
	Class c = ((ClassType) that.expr.type).getSymbol();
	that.val = tr.genShallowCopy(that.expr.val, c);
}

@Override
public void visitDcopy(Tree.Dcopy that)
{
	that.expr.accept(this);
	Class c = ((ClassType) that.expr.type).getSymbol();
	that.val = tr.genDeepCopy(that.expr.val, c);
}
```

Transtaler中构造

```java
public Temp genShallowCopy(Temp rsc, Class symbol)
{
	Temp dst = Temp.createTempI4();
	dst = genDirectCall(symbol.getNewFuncLabel(), BaseType.INT);
	for (int i = 0; i < symbol.vars.size(); i++)
	{
		if (symbol.vars.get(i).type.equal(BaseType.COMPLEX))
		{
			Temp comprsc = genLoad(rsc, 4 * (i + 1));
			Temp compdst = genNewArray(genLoadImm4(2));
			genStore(genLoad(comprsc, 0), compdst, 0);
			genStore(genLoad(comprsc, 4), compdst, 4);
			genStore(compdst, dst, 4 * (i + 1));
		}
		else
		{
			Temp tmp = genLoad(rsc, 4 * (i + 1));
			genStore(tmp, dst, 4 * (i + 1));
		}
	}
	return dst;
}

public Temp genDeepCopy(Temp rsc, Class symbol)
{
	Temp dst = Temp.createTempI4();
	dst = genDirectCall(symbol.getNewFuncLabel(), BaseType.INT);
	for (int i = 0; i < symbol.vars.size(); i++)
	{
		if (symbol.vars.get(i).type.isClassType())
		{
			Class c = ((ClassType) symbol.vars.get(i).type).getSymbol();
			Temp subrsc = genLoad(rsc, 4 * (i + 1));
			Temp subdst = genDeepCopy(subrsc, c);
			genStore(subdst, dst, 4 * (i + 1));
		}
		else if (symbol.vars.get(i).type.equal(BaseType.COMPLEX))
		{
			Temp comprsc = genLoad(rsc, 4 * (i + 1));
			Temp compdst = genNewArray(genLoadImm4(2));
			genStore(genLoad(comprsc, 0), compdst, 0);
			genStore(genLoad(comprsc, 4), compdst, 4);
			genStore(compdst, dst, 4 * (i + 1));
		}
		else
		{
			Temp tmp = genLoad(rsc, 4 * (i + 1));
			genStore(tmp, dst, 4 * (i + 1));
		}
	}
	return dst;
}
```

6. 循环卫士语句

直到pa3我才意识到原来对循环卫士语句的理解有误。实际只要会用branch循环卫士语句非常容易。

```java
@Override
public void visitDOODStmt(Tree.DOODStmt that)
{
	Label enter, exit, next;
	enter = Label.createLabel();
	exit = Label.createLabel();
	loopExits.push(exit);
	tr.genMark(enter);
	for (Tree f : that.slist)
	{
		Tree.DOODSubStmt sub = (Tree.DOODSubStmt) f;
		sub.expr.accept(this);
		next = Label.createLabel();
		tr.genBeqz(sub.expr.val, next);
		sub.stmt.accept(this);
		tr.genBranch(enter);
		tr.genMark(next);
	}
	tr.genMark(exit);
	loopExits.pop();
}
```

7. 运行错误检测

只需修改Translater中genDiv和genMod即可，两者一致，我只展示Div。

```java
public Temp genDiv(Temp src1, Temp src2) {
	Label err = Label.createLabel();
	Label exit = Label.createLabel();
	genBeqz(src2, err);
	Temp dst = Temp.createTempI4();
	append(Tac.genDiv(dst, src1, src2));
	genBranch(exit);
	genMark(err);
	Temp msg = genLoadStrConst(RuntimeError.DIV_ZERO);
	genParm(msg);
	genIntrinsicCall(Intrinsic.PRINT_STRING);
	genIntrinsicCall(Intrinsic.HALT);
	genMark(exit);
	return dst;
}
```

## 测试

编写了一系列小程序测试多次继承的情况下dcopy和scopy的正确性。并针对Dcopy和Scopy对complex的调用有较多测试。编译器都能够正确生成tac并运行。

## 实验总结

这次实验的主要工作在于理解TAC，而TAC是非常类似于MIPS的，所以掌握起来也并不困难。在这个基础上前3次pa让我们对decaf框架有了清晰的认识。实际上工作难点就变成了各种特判，尤其是complex，这一部分也是实现起来比较困难的。总的来说，经过三次pa我对编译器如何将高级语言翻译成机器语言有了更直观的认识，也能更好的应用到之后的工作中。

