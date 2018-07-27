package decaf.translate;

import java.util.Stack;

import decaf.tree.Tree;
import decaf.backend.OffsetCounter;
import decaf.machdesc.Intrinsic;
import decaf.symbol.Variable;
import decaf.tac.Label;
import decaf.tac.Temp;
import decaf.type.BaseType;
import decaf.type.ClassType;
import decaf.scope.ClassScope;
import decaf.symbol.Class;

public class TransPass2 extends Tree.Visitor {

	private Translater tr;

	private Temp currentThis;

	private Stack<Label> loopExits;

	public TransPass2(Translater tr) {
		this.tr = tr;
		loopExits = new Stack<Label>();
	}

	@Override
	public void visitClassDef(Tree.ClassDef classDef) {
		for (Tree f : classDef.fields) {
			f.accept(this);
		}
	}

	@Override
	public void visitMethodDef(Tree.MethodDef funcDefn) {
		if (!funcDefn.statik) {
			currentThis = ((Variable) funcDefn.symbol.getAssociatedScope()
					.lookup("this")).getTemp();
		}
		tr.beginFunc(funcDefn.symbol);
		funcDefn.body.accept(this);
		tr.endFunc();
		currentThis = null;
	}

	@Override
	public void visitTopLevel(Tree.TopLevel program) {
		for (Tree.ClassDef cd : program.classes) {
			cd.accept(this);
		}
	}

	@Override
	public void visitVarDef(Tree.VarDef varDef) {
		if (varDef.symbol.isLocalVar()) {
			Temp t = Temp.createTempI4();
            if (varDef.type.type.equal(BaseType.COMPLEX))
    		    t = tr.genNewArray(tr.genLoadImm4(2));
			t.sym = varDef.symbol;
			varDef.symbol.setTemp(t);

		}
	}

	@Override
	public void visitBinary(Tree.Binary expr) {
		expr.left.accept(this);
		expr.right.accept(this);
		switch (expr.tag) {
		case Tree.PLUS:
		    if (expr.left.type.equal(BaseType.COMPLEX))
		    {
		        if (expr.right.type.equal(BaseType.COMPLEX))
		        {
        		    expr.val = tr.genNewArray(tr.genLoadImm4(2));
                    Temp p1 = tr.genLoad(expr.left.val, 0);
                    Temp p2 = tr.genLoad(expr.left.val, 4);
                    Temp q1 = tr.genLoad(expr.right.val, 0);
                    Temp q2 = tr.genLoad(expr.right.val, 4);
        	    	tr.genStore(tr.genAdd(p1, q1), expr.val, 0);
        	    	tr.genStore(tr.genAdd(p2, q2), expr.val, 4);
		        }
		        else
		        {
        		    expr.val = tr.genNewArray(tr.genLoadImm4(2));
                    Temp p1 = tr.genLoad(expr.left.val, 0);
                    Temp p2 = tr.genLoad(expr.left.val, 4);
        	    	tr.genStore(tr.genAdd(p1, expr.right.val), expr.val, 0);
        	    	tr.genStore(p2, expr.val, 4);
		        }
		    }
		    else if (expr.right.type.equal(BaseType.COMPLEX))
		    {
       		    expr.val = tr.genNewArray(tr.genLoadImm4(2));
                Temp q1 = tr.genLoad(expr.right.val, 0);
                Temp q2 = tr.genLoad(expr.right.val, 4);
       	    	tr.genStore(tr.genAdd(expr.left.val, q1), expr.val, 0);
       	    	tr.genStore(q2, expr.val, 4);
		    }
		    else
    			expr.val = tr.genAdd(expr.left.val, expr.right.val);
			break;
		case Tree.MINUS:
			expr.val = tr.genSub(expr.left.val, expr.right.val);
			break;
		case Tree.MUL:
		    if (expr.left.type.equal(BaseType.COMPLEX))
		    {
		        if (expr.right.type.equal(BaseType.COMPLEX))
		        {
        		    expr.val = tr.genNewArray(tr.genLoadImm4(2));
                    Temp p1 = tr.genLoad(expr.left.val, 0);
                    Temp p2 = tr.genLoad(expr.left.val, 4);
                    Temp q1 = tr.genLoad(expr.right.val, 0);
                    Temp q2 = tr.genLoad(expr.right.val, 4);
        	    	tr.genStore(tr.genSub(tr.genMul(p1, q1), tr.genMul(p2, q2)), expr.val, 0);
        	    	tr.genStore(tr.genAdd(tr.genMul(p1, q2), tr.genMul(p2, q1)), expr.val, 4);
		        }
		        else
		        {
        		    expr.val = tr.genNewArray(tr.genLoadImm4(2));
                    Temp p1 = tr.genLoad(expr.left.val, 0);
                    Temp p2 = tr.genLoad(expr.left.val, 4);
        	    	tr.genStore(tr.genMul(p1, expr.right.val), expr.val, 0);
        	    	tr.genStore(tr.genMul(p2, expr.right.val), expr.val, 4);
		        }
		    }
		    else if (expr.right.type.equal(BaseType.COMPLEX))
		    {
       		    expr.val = tr.genNewArray(tr.genLoadImm4(2));
                Temp q1 = tr.genLoad(expr.right.val, 0);
                Temp q2 = tr.genLoad(expr.right.val, 4);
     	    	tr.genStore(tr.genMul(expr.left.val, q1), expr.val, 0);
       	    	tr.genStore(tr.genMul(expr.left.val, q2), expr.val, 4);
		    }
		    else
    			expr.val = tr.genMul(expr.left.val, expr.right.val);
			break;
		case Tree.DIV:
			expr.val = tr.genDiv(expr.left.val, expr.right.val);
			break;
		case Tree.MOD:
			expr.val = tr.genMod(expr.left.val, expr.right.val);
			break;
		case Tree.AND:
			expr.val = tr.genLAnd(expr.left.val, expr.right.val);
			break;
		case Tree.OR:
			expr.val = tr.genLOr(expr.left.val, expr.right.val);
			break;
		case Tree.LT:
			expr.val = tr.genLes(expr.left.val, expr.right.val);
			break;
		case Tree.LE:
			expr.val = tr.genLeq(expr.left.val, expr.right.val);
			break;
		case Tree.GT:
			expr.val = tr.genGtr(expr.left.val, expr.right.val);
			break;
		case Tree.GE:
			expr.val = tr.genGeq(expr.left.val, expr.right.val);
			break;
		case Tree.EQ:
		case Tree.NE:
			genEquNeq(expr);
			break;
		}
	}

	private void genEquNeq(Tree.Binary expr) {
		if (expr.left.type.equal(BaseType.STRING)
				|| expr.right.type.equal(BaseType.STRING)) {
			tr.genParm(expr.left.val);
			tr.genParm(expr.right.val);
			expr.val = tr.genDirectCall(Intrinsic.STRING_EQUAL.label,
					BaseType.BOOL);
			if(expr.tag == Tree.NE){
				expr.val = tr.genLNot(expr.val);
			}
		} else {
			if(expr.tag == Tree.EQ)
				expr.val = tr.genEqu(expr.left.val, expr.right.val);
			else
				expr.val = tr.genNeq(expr.left.val, expr.right.val);
		}
	}

	@Override
	public void visitAssign(Tree.Assign assign) {
		assign.left.accept(this);
		assign.expr.accept(this);
		switch (assign.left.lvKind) {
		case ARRAY_ELEMENT:
			Tree.Indexed arrayRef = (Tree.Indexed) assign.left;
			Temp esz = tr.genLoadImm4(OffsetCounter.WORD_SIZE);
			Temp t = tr.genMul(arrayRef.index.val, esz);
			Temp base = tr.genAdd(arrayRef.array.val, t);
			tr.genStore(assign.expr.val, base, 0);
			break;
		case MEMBER_VAR:
			Tree.Ident varRef = (Tree.Ident) assign.left;
            if (assign.left.type.equal(BaseType.COMPLEX))
            {
                Temp tmp1 = tr.genLoad(varRef.owner.val, varRef.symbol.getOffset());
                Temp tmp2 = tr.genLoad(assign.expr.val, 0);
                tr.genStore(tmp2, tmp1, 0);
                tmp2 = tr.genLoad(assign.expr.val, 4);
                tr.genStore(tmp2, tmp1, 4);
            }
            else
    			tr.genStore(assign.expr.val, varRef.owner.val, varRef.symbol.getOffset());
			break;
		case PARAM_VAR:
		case LOCAL_VAR:
		    if (assign.left.type.equal(BaseType.COMPLEX))
		    {
                Temp tmp = tr.genLoad(assign.expr.val, 0);
                tr.genStore(tmp, assign.left.val, 0);
                tmp = tr.genLoad(assign.expr.val, 4);
                tr.genStore(tmp, assign.left.val, 4);
            }
		    else
    			tr.genAssign(((Tree.Ident) assign.left).symbol.getTemp(), assign.expr.val);
			break;
		}
	}

	@Override
	public void visitLiteral(Tree.Literal literal) {
		switch (literal.typeTag) {
		case Tree.INT:
			literal.val = tr.genLoadImm4(((Integer)literal.value).intValue());
			break;
		case Tree.BOOL:
			literal.val = tr.genLoadImm4((Boolean)(literal.value) ? 1 : 0);
			break;
		case Tree.COMPLEX:
		    literal.val = tr.genNewArray(tr.genLoadImm4(2));
	    	tr.genStore(tr.genLoadImm4(0), literal.val, 0);
	    	tr.genStore(tr.genLoadImm4((Integer)literal.value), literal.val, 4);
		    break;
		default:
			literal.val = tr.genLoadStrConst((String)literal.value);
		}
	}

	@Override
	public void visitExec(Tree.Exec exec) {
		exec.expr.accept(this);
	}

	@Override
	public void visitUnary(Tree.Unary expr) {
		expr.expr.accept(this);
		switch (expr.tag)
		{
		case Tree.NEG:
			expr.val = tr.genNeg(expr.expr.val);
			break;
   		case Tree.RE:
   			expr.val = tr.genLoad(expr.expr.val, 0);
   			break;
   		case Tree.IM:
   			expr.val = tr.genLoad(expr.expr.val, 4);
   			break;
   		case Tree.COMPCAST:

		    expr.val = tr.genNewArray(tr.genLoadImm4(2));
	    	tr.genStore(expr.expr.val, expr.val, 0);
	    	tr.genStore(tr.genLoadImm4(0), expr.val, 4);
   			break;
		default:
			expr.val = tr.genLNot(expr.expr.val);
		}
	}

	@Override
	public void visitNull(Tree.Null nullExpr) {
		nullExpr.val = tr.genLoadImm4(0);
	}

	@Override
	public void visitBlock(Tree.Block block) {
		for (Tree s : block.block) {
			s.accept(this);
		}
	}

	@Override
	public void visitThisExpr(Tree.ThisExpr thisExpr) {
		thisExpr.val = currentThis;
	}

	@Override
	public void visitReadIntExpr(Tree.ReadIntExpr readIntExpr) {
		readIntExpr.val = tr.genIntrinsicCall(Intrinsic.READ_INT);
	}

	@Override
	public void visitReadLineExpr(Tree.ReadLineExpr readStringExpr) {
		readStringExpr.val = tr.genIntrinsicCall(Intrinsic.READ_LINE);
	}

	@Override
	public void visitReturn(Tree.Return returnStmt) {
		if (returnStmt.expr != null) {
			returnStmt.expr.accept(this);
			tr.genReturn(returnStmt.expr.val);
		} else {
			tr.genReturn(null);
		}

	}

	@Override
	public void visitPrint(Tree.Print printStmt) {
		for (Tree.Expr r : printStmt.exprs) {
			r.accept(this);
			tr.genParm(r.val);
			if (r.type.equal(BaseType.BOOL)) {
				tr.genIntrinsicCall(Intrinsic.PRINT_BOOL);
			} else if (r.type.equal(BaseType.INT)) {
				tr.genIntrinsicCall(Intrinsic.PRINT_INT);
			} else if (r.type.equal(BaseType.STRING)) {
				tr.genIntrinsicCall(Intrinsic.PRINT_STRING);
			}
		}
	}

	@Override
	public void visitIndexed(Tree.Indexed indexed) {
		indexed.array.accept(this);
		indexed.index.accept(this);
		tr.genCheckArrayIndex(indexed.array.val, indexed.index.val);
		
		Temp esz = tr.genLoadImm4(OffsetCounter.WORD_SIZE);
		Temp t = tr.genMul(indexed.index.val, esz);
		Temp base = tr.genAdd(indexed.array.val, t);
		indexed.val = tr.genLoad(base, 0);
	}

	@Override
	public void visitIdent(Tree.Ident ident) {
		if(ident.lvKind == Tree.LValue.Kind.MEMBER_VAR){
			ident.owner.accept(this);
		}
		
		switch (ident.lvKind) {
		case MEMBER_VAR:
			ident.val = tr.genLoad(ident.owner.val, ident.symbol.getOffset());
			break;
		default:
			ident.val = ident.symbol.getTemp();
			break;
		}
	}
	
	@Override
	public void visitBreak(Tree.Break breakStmt) {
		tr.genBranch(loopExits.peek());
	}

	@Override
	public void visitCallExpr(Tree.CallExpr callExpr) {
		if (callExpr.isArrayLength) {
			callExpr.receiver.accept(this);
			callExpr.val = tr.genLoad(callExpr.receiver.val,
					-OffsetCounter.WORD_SIZE);
		} else {
			if (callExpr.receiver != null) {
				callExpr.receiver.accept(this);
			}
			for (Tree.Expr expr : callExpr.actuals) {
				expr.accept(this);
			}
			if (callExpr.receiver != null) {
				tr.genParm(callExpr.receiver.val);
			}
			for (Tree.Expr expr : callExpr.actuals) {
				tr.genParm(expr.val);
			}
			if (callExpr.receiver == null) {
				callExpr.val = tr.genDirectCall(
						callExpr.symbol.getFuncty().label, callExpr.symbol
								.getReturnType());
			} else {
				Temp vt;
				if (callExpr.receiver instanceof Tree.SuperExpr || callExpr.receiver instanceof Tree.ThisExpr)
                    vt = tr.genLoadVTable(callExpr.symbol.getScope().getOwner().getVtable());
                else
                    vt = tr.genLoad(callExpr.receiver.val, 0);
				Temp func = tr.genLoad(vt, callExpr.symbol.getOffset());
				callExpr.val = tr.genIndirectCall(func, callExpr.symbol.getReturnType());
			}
		}

	}

	@Override
	public void visitForLoop(Tree.ForLoop forLoop) {
		if (forLoop.init != null) {
			forLoop.init.accept(this);
		}
		Label cond = Label.createLabel();
		Label loop = Label.createLabel();
		tr.genBranch(cond);
		tr.genMark(loop);
		if (forLoop.update != null) {
			forLoop.update.accept(this);
		}
		tr.genMark(cond);
		forLoop.condition.accept(this);
		Label exit = Label.createLabel();
		tr.genBeqz(forLoop.condition.val, exit);
		loopExits.push(exit);
		if (forLoop.loopBody != null) {
			forLoop.loopBody.accept(this);
		}
		tr.genBranch(loop);
		loopExits.pop();
		tr.genMark(exit);
	}

	@Override
	public void visitIf(Tree.If ifStmt) {
		ifStmt.condition.accept(this);
		if (ifStmt.falseBranch != null) {
			Label falseLabel = Label.createLabel();
			tr.genBeqz(ifStmt.condition.val, falseLabel);
			ifStmt.trueBranch.accept(this);
			Label exit = Label.createLabel();
			tr.genBranch(exit);
			tr.genMark(falseLabel);
			ifStmt.falseBranch.accept(this);
			tr.genMark(exit);
		} else if (ifStmt.trueBranch != null) {
			Label exit = Label.createLabel();
			tr.genBeqz(ifStmt.condition.val, exit);
			if (ifStmt.trueBranch != null) {
				ifStmt.trueBranch.accept(this);
			}
			tr.genMark(exit);
		}
	}

	@Override
	public void visitNewArray(Tree.NewArray newArray) {
		newArray.length.accept(this);
		newArray.val = tr.genNewArray(newArray.length.val);
	}

	@Override
	public void visitNewClass(Tree.NewClass newClass) {
		newClass.val = tr.genDirectCall(newClass.symbol.getNewFuncLabel(),
				BaseType.INT);
	}

	@Override
	public void visitWhileLoop(Tree.WhileLoop whileLoop) {
		Label loop = Label.createLabel();
		tr.genMark(loop);
		whileLoop.condition.accept(this);
		Label exit = Label.createLabel();
		tr.genBeqz(whileLoop.condition.val, exit);
		loopExits.push(exit);
		if (whileLoop.loopBody != null) {
			whileLoop.loopBody.accept(this);
		}
		tr.genBranch(loop);
		loopExits.pop();
		tr.genMark(exit);
	}

	@Override
	public void visitTypeTest(Tree.TypeTest typeTest) {
		typeTest.instance.accept(this);
		typeTest.val = tr.genInstanceof(typeTest.instance.val,
				typeTest.symbol);
	}

	@Override
	public void visitTypeCast(Tree.TypeCast typeCast) {
		typeCast.expr.accept(this);
		if (!typeCast.expr.type.compatible(typeCast.symbol.getType())) {
			tr.genClassCast(typeCast.expr.val, typeCast.symbol);
		}
		typeCast.val = typeCast.expr.val;
	}

	@Override
	public void visitPrintComp(Tree.PrintComp printStmt) {
		for (Tree.Expr r : printStmt.exprs) {
			r.accept(this);
            tr.genParm(tr.genLoad(r.val, 0));
        	tr.genIntrinsicCall(Intrinsic.PRINT_INT);
    		tr.genParm(tr.genLoadStrConst("+"));
			tr.genIntrinsicCall(Intrinsic.PRINT_STRING);
            tr.genParm(tr.genLoad(r.val, 4));
        	tr.genIntrinsicCall(Intrinsic.PRINT_INT);
    		tr.genParm(tr.genLoadStrConst("j"));
			tr.genIntrinsicCall(Intrinsic.PRINT_STRING);
		}
	}

	@Override
	public void visitSuperExpr(Tree.SuperExpr superExpr) {
		superExpr.val = currentThis;
	}

	@Override
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

	@Override
    public void visitCasedef(Tree.Casedef that) {
        that.c.accept(this);
        that.expr.accept(this);
    }

	@Override
    public void visitDefaultCase(Tree.DefaultCase that) {
        that.expr.accept(this);
    }

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
}
