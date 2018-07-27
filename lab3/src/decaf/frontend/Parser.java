//### This file created by BYACC 1.8(/Java extension  1.13)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//###           14 Sep 06  -- Keltin Leung-- ReduceListener support, eliminate underflow report in error recovery
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 11 "Parser.y"
package decaf.frontend;

import decaf.tree.Tree;
import decaf.tree.Tree.*;
import decaf.error.*;
import java.util.*;
//#line 25 "Parser.java"
interface ReduceListener {
  public boolean onReduce(String rule);
}




public class Parser
             extends BaseParser
             implements ReduceListener
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

ReduceListener reduceListener = null;
void yyclearin ()       {yychar = (-1);}
void yyerrok ()         {yyerrflag=0;}
void addReduceListener(ReduceListener l) {
  reduceListener = l;}


//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:SemValue
String   yytext;//user variable to return contextual strings
SemValue yyval; //used to return semantic vals from action routines
SemValue yylval;//the 'lval' (result) I got from yylex()
SemValue valstk[] = new SemValue[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new SemValue();
  yylval=new SemValue();
  valptr=-1;
}
final void val_push(SemValue val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    SemValue[] newstack = new SemValue[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final SemValue val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final SemValue val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short VOID=257;
public final static short BOOL=258;
public final static short INT=259;
public final static short COMPLEX=260;
public final static short STRING=261;
public final static short CLASS=262;
public final static short DO=263;
public final static short OD=264;
public final static short DOOD=265;
public final static short NULL=266;
public final static short EXTENDS=267;
public final static short THIS=268;
public final static short WHILE=269;
public final static short FOR=270;
public final static short CASE=271;
public final static short DEFAULT=272;
public final static short IF=273;
public final static short ELSE=274;
public final static short RETURN=275;
public final static short BREAK=276;
public final static short NEW=277;
public final static short SUPER=278;
public final static short PRINT=279;
public final static short PRINT_COMP=280;
public final static short READ_INTEGER=281;
public final static short READ_LINE=282;
public final static short LITERAL=283;
public final static short DCOPY=284;
public final static short SCOPY=285;
public final static short IDENTIFIER=286;
public final static short AND=287;
public final static short OR=288;
public final static short STATIC=289;
public final static short INSTANCEOF=290;
public final static short LESS_EQUAL=291;
public final static short GREATER_EQUAL=292;
public final static short EQUAL=293;
public final static short NOT_EQUAL=294;
public final static short UMINUS=295;
public final static short EMPTY=296;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    4,    5,    5,    5,    5,    5,
    5,    5,    2,    6,    6,    7,    7,    7,    9,    9,
   10,   10,    8,    8,   11,   12,   12,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   14,   14,   14,
   25,   25,   22,   22,   24,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   27,   27,
   26,   26,   30,   30,   16,   17,   20,   15,   31,   31,
   18,   18,   19,   19,   28,   28,   32,   29,   21,   33,
   33,   34,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    2,    1,    1,    1,    1,    1,
    2,    3,    6,    2,    0,    2,    2,    0,    1,    0,
    3,    1,    7,    6,    3,    2,    0,    1,    2,    1,
    1,    1,    2,    2,    2,    1,    2,    3,    1,    0,
    2,    0,    2,    4,    5,    1,    1,    1,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    2,    2,    2,    2,    2,    3,    3,    1,
    1,    4,    5,    6,    5,    8,    4,    4,    1,    1,
    1,    0,    3,    1,    5,    9,    1,    6,    2,    0,
    2,    1,    4,    4,    2,    0,    4,    4,    3,    3,
    1,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    3,    0,    2,    0,    0,   14,   18,
    0,    7,    8,    6,    9,   10,    0,    0,   13,   16,
    0,    0,   17,   11,    0,    4,    0,    0,    0,    0,
   12,    0,   22,    0,    0,    0,    0,    5,    0,    0,
    0,   27,   24,   21,   23,    0,    0,   80,   70,    0,
    0,    0,    0,    0,   87,    0,   71,    0,    0,    0,
    0,   79,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   25,   28,   36,   26,    0,   30,   31,   32,    0,
    0,    0,    0,    0,    0,    0,    0,   48,   46,    0,
   47,    0,  101,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   29,   33,   34,   35,   37,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   41,    0,    0,    0,   99,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   68,   69,
    0,    0,    0,    0,   62,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  102,  100,    0,    0,    0,    0,   72,    0,    0,
   93,   94,   77,   78,    0,    0,   44,    0,    0,   85,
    0,   96,    0,   73,    0,    0,   75,   45,    0,    0,
    0,   88,   74,    0,    0,    0,    0,   95,   89,    0,
    0,    0,   76,   86,    0,    0,   98,   97,
};
final static short yydgoto[] = {                          2,
    3,    4,   73,   21,   34,    8,   11,   23,   35,   36,
   74,   46,   75,   76,   77,   78,   79,   80,   81,   82,
   83,   89,   85,   91,   87,  188,   88,  200,  207,  147,
  202,  208,   92,   93,
};
final static short yysindex[] = {                      -256,
 -278,    0, -256,    0, -248,    0, -246,  -93,    0,    0,
  229,    0,    0,    0,    0,    0, -239,  -46,    0,    0,
   -4,  -80,    0,    0,  -78,    0,   26,   -2,   53,  -46,
    0,  -46,    0,  -76,   54,   63,   56,    0,  -10,  -46,
  -10,    0,    0,    0,    0,    1,  432,    0,    0,   72,
   74,   77,   78,  432,    0,  -67,    0,   79,   81,   82,
   83,    0,   87,   90,   94,  432,  432,  432,  432,  432,
   75,    0,    0,    0,    0,   92,    0,    0,    0,   93,
  105,  107,  108,   76,  907,    0, -148,    0,    0,  462,
    0, -188,    0,  432,  432,  432,  432,  907,  128,   85,
  432,  432,  132,  134,  432,  432,  432,  -30,  907,  907,
  907,  -30, -117,  489,    0,    0,    0,    0,    0,  432,
  432,  432,  432,  432,  432,  432,  432,  432,  432,  432,
  432,  432,  432,    0,  432,  139,   35,    0,  432,  500,
  121,  522,  552,  140,   96,  907,  -24,  -12,    0,    0,
  563,  585,  613,  146,    0,  907,  969,  957,    7,    7,
  981,  981,  -19,  -19,  -30,  -30,  -30,    7,    7,  775,
  432,    0,    0,   35,  432,   86,   35,    0,  848,  432,
    0,    0,    0,    0,  -89,  432,    0,  157,  158,    0,
  698,    0,  -57,    0,  907,  177,    0,    0,  432, -209,
   35,    0,    0,  179,  165,  181,  148,    0,    0,   35,
  432,  432,    0,    0,  872,  883,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  265,    0,  143,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  228,    0,    0,  247,
    0,  247,    0,    0,    0,  248,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -45,    4,    0,    0,    0,
    0,    0,    0,  -11,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    4,    4,    4,    4,    4,
    4,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  919,    0,  438,    0,    0,    0,    0,
    0,    0,    0,    4,  -45,    4,    4,  240,    0,    0,
    4,    4,    0,    0,    4,    4,    4,  103,   23,  325,
  383,  112,    0,    0,    0,    0,    0,    0,    0,    4,
    4,    4,    4,    4,    4,    4,    4,    4,    4,    4,
    4,    4,    4,    0,    4,   42,  -45,    0,    4,    0,
    0,    0,    0,    0,    4,   15,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -37,   84,  -20,  163,  514,
  830,  929,  991, 1030,  141,  378,  402,  593,  789,    0,
  -32,    0,    0,  -45,    4,    0,  -45,    0,    0,    4,
    0,    0,    0,    0,    0,    4,    0,    0,  259,    0,
    0,    0,  -33,    0,   65,    0,    0,    0,  -31,    0,
  -45,    0,    0,    0,    0,    0,    0,    0,    0,  -45,
    4,    4,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  299,  296,    3,   40,    0,    0,    0,  277,    0,
   21,    0, -132,  -94,    0,    0,    0,    0,    0,    0,
    0,  596, 1233, 1019,    0,    0,  122,    0,    0,  -77,
    0,    0,    0,  182,
};
final static int YYTABLESIZE=1445;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         90,
  141,   90,   90,   38,  172,    1,   90,    5,   82,   40,
   28,   90,   28,   40,   28,  134,  181,  131,    7,  180,
   61,   38,  129,   61,  148,   90,  134,  130,  182,   10,
   90,  180,   33,   70,   33,   69,   68,   61,   61,    9,
   71,  190,   44,  131,  193,   66,   24,   92,  129,  127,
   22,  128,  134,  130,   26,   84,   48,   25,   84,   43,
  135,   45,  205,   65,   67,   30,   65,   70,  209,   69,
   68,  135,   61,   62,   71,  138,  139,  214,   43,   66,
   65,   65,   43,   43,   43,   43,   43,   43,   43,   90,
   31,   90,   32,  189,   39,  100,   41,  135,   67,   43,
   43,   43,   43,   43,  204,   83,   40,   70,   83,   69,
   68,   94,   42,   95,   71,   65,   96,   97,  101,   66,
  102,  103,  104,   42,   60,   72,  105,   60,   70,  106,
   69,   68,   43,  107,   43,   71,  120,  136,   67,   63,
   66,   60,   60,   63,   63,   63,   63,   63,   64,   63,
  115,  116,   64,   64,   64,   64,   64,   42,   64,   67,
   63,   63,   63,  117,   63,  118,  119,  144,  154,   64,
   64,   64,  149,   64,  150,  145,   60,   51,  171,  175,
  178,   51,   51,   51,   51,   51,  186,   51,   31,   12,
   13,   14,   15,   16,   17,   63,  196,  198,   51,   51,
   51,  180,   51,   58,   64,   27,   58,   29,  192,   38,
   12,   13,   14,   15,   16,   17,  201,  203,   99,  210,
   58,   58,  211,   90,   90,   90,   90,   90,   90,   90,
   90,   90,   90,   51,   90,   90,   90,   90,  212,   90,
   42,   90,   90,   90,   90,   90,   90,   90,   90,   90,
   90,   90,   90,   42,   42,   58,   90,   12,   13,   14,
   15,   16,   17,   47,    1,   15,   48,   61,   49,   50,
   51,   52,  213,   53,   42,   54,   55,   56,   57,   58,
   59,   60,   61,   62,   63,   64,    5,   20,   19,   42,
   65,   12,   13,   14,   15,   16,   17,   47,   91,   81,
   48,    6,   49,   50,   51,   52,   20,   53,   37,   54,
   55,   56,   57,   58,   59,   60,   61,   62,   63,   64,
  173,  206,    0,    0,   65,    0,    0,    0,   43,   43,
    0,    0,   43,   43,   43,   43,  113,    0,    0,    0,
   48,    0,   49,    0,    0,   52,    0,    0,    0,    0,
    0,   56,   57,   19,    0,   60,   61,   62,   63,   64,
    0,   48,    0,   49,   65,   66,   52,    0,   66,    0,
   60,   60,   56,   57,    0,    0,   60,   61,   62,   63,
   64,    0,   66,   66,    0,   65,    0,    0,    0,   63,
   63,    0,    0,   63,   63,   63,   63,    0,   64,   64,
    0,    0,   64,   64,   64,   64,    0,    0,    0,    0,
    0,    0,    0,    0,   52,    0,    0,   66,   52,   52,
   52,   52,   52,   67,   52,    0,   67,   51,   51,    0,
    0,   51,   51,   51,   51,   52,   52,   52,   53,   52,
   67,   67,   53,   53,   53,   53,   53,    0,   53,   58,
   58,    0,    0,    0,    0,   58,   58,    0,    0,   53,
   53,   53,    0,   53,   70,    0,   69,   68,    0,    0,
   52,   71,    0,    0,   47,   67,   66,    0,   39,   47,
   47,    0,   47,   47,   47,   12,   13,   14,   15,   16,
   17,    0,    0,    0,   53,   67,   39,   47,  131,   47,
    0,    0,    0,  129,  127,    0,  128,  134,  130,    0,
    0,    0,    0,    0,    0,    0,    0,   18,    0,  137,
    0,  133,    0,  132,    0,  131,    0,    0,   47,  155,
  129,  127,    0,  128,  134,  130,  131,    0,    0,    0,
  174,  129,  127,    0,  128,  134,  130,    0,  133,    0,
  132,    0,  135,    0,   59,    0,    0,   59,  131,  133,
    0,  132,  176,  129,  127,    0,  128,  134,  130,    0,
    0,   59,   59,    0,    0,    0,    0,    0,    0,  135,
    0,  133,    0,  132,    0,    0,    0,    0,  131,    0,
  135,    0,  177,  129,  127,    0,  128,  134,  130,  131,
    0,    0,    0,  183,  129,  127,   59,  128,  134,  130,
    0,  133,  135,  132,    0,    0,    0,    0,    0,    0,
    0,  131,  133,    0,  132,  184,  129,  127,    0,  128,
  134,  130,    0,   57,    0,    0,   57,    0,    0,    0,
    0,   84,  135,    0,  133,    0,  132,    0,    0,  131,
   57,   57,    0,  135,  129,  127,  185,  128,  134,  130,
    0,    0,    0,    0,   52,   52,    0,    0,   52,   52,
   52,   52,  133,    0,  132,  135,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   57,    0,    0,   53,   53,
   84,    0,   53,   53,   53,   53,    0,   48,    0,   49,
    0,    0,   52,  135,    0,    0,    0,    0,   56,   57,
    0,    0,   60,   61,   62,   63,   64,    0,    0,    0,
    0,   65,    0,    0,   47,   47,    0,    0,   47,   47,
   47,   47,   84,    0,  131,    0,    0,    0,    0,  129,
  127,    0,  128,  134,  130,    0,    0,    0,  121,  122,
    0,    0,  123,  124,  125,  126,  199,  133,    0,  132,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   84,
    0,    0,   84,    0,    0,  121,  122,    0,    0,  123,
  124,  125,  126,    0,    0,    0,  121,  122,  135,    0,
  123,  124,  125,  126,   84,    0,   84,    0,    0,    0,
   59,   59,    0,    0,    0,   84,   59,   59,  121,  122,
    0,  131,  123,  124,  125,  126,  129,  127,    0,  128,
  134,  130,    0,    0,    0,    0,    0,    0,    0,   56,
    0,    0,   56,    0,  133,    0,  132,    0,  121,  122,
    0,    0,  123,  124,  125,  126,   56,   56,    0,  121,
  122,    0,    0,  123,  124,  125,  126,    0,    0,    0,
    0,    0,    0,    0,    0,  135,    0,  187,    0,    0,
   54,  121,  122,   54,    0,  123,  124,  125,  126,   57,
   57,   56,    0,    0,  131,   57,   57,   54,   54,  129,
  127,    0,  128,  134,  130,    0,    0,    0,    0,  121,
  122,    0,    0,  123,  124,  125,  126,  133,  131,  132,
    0,    0,    0,  129,  127,    0,  128,  134,  130,  131,
    0,    0,   54,    0,  129,  127,    0,  128,  134,  130,
  217,  133,    0,  132,    0,    0,    0,    0,  135,    0,
  194,  218,  133,  131,  132,    0,    0,    0,  129,  127,
    0,  128,  134,  130,    0,   46,    0,    0,    0,    0,
   46,   46,  135,   46,   46,   46,  133,    0,  132,   55,
    0,    0,   55,  135,    0,    0,    0,    0,   46,    0,
   46,    0,    0,    0,  121,  122,   55,   55,  123,  124,
  125,  126,    0,  131,    0,    0,    0,  135,  129,  127,
    0,  128,  134,  130,    0,  131,    0,    0,    0,   46,
  129,  127,    0,  128,  134,  130,  133,  131,  132,    0,
    0,   55,  129,  127,    0,  128,  134,  130,  133,    0,
  132,   49,    0,   49,   49,   49,    0,    0,    0,    0,
  133,    0,  132,    0,    0,    0,    0,  135,   49,   49,
   49,    0,   49,    0,    0,    0,    0,    0,    0,  135,
    0,  121,  122,    0,   86,  123,  124,  125,  126,    0,
   50,  135,   50,   50,   50,   56,   56,    0,    0,    0,
    0,   56,   56,   49,    0,    0,    0,   50,   50,   50,
    0,   50,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   86,    0,    0,   54,   54,    0,    0,
    0,    0,   50,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  121,  122,    0,    0,  123,  124,
  125,  126,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   86,    0,    0,  121,  122,
    0,    0,  123,  124,  125,  126,    0,    0,    0,  121,
  122,    0,    0,  123,  124,  125,  126,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   86,  121,  122,   86,    0,  123,  124,  125,
  126,    0,    0,    0,    0,   46,   46,    0,    0,   46,
   46,   46,   46,    0,    0,   55,   55,   86,    0,   86,
    0,    0,    0,    0,    0,    0,    0,    0,   86,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  121,    0,    0,    0,  123,  124,  125,
  126,    0,    0,    0,    0,    0,    0,    0,    0,  123,
  124,  125,  126,    0,    0,    0,    0,    0,    0,    0,
    0,  123,  124,    0,    0,    0,    0,   49,   49,   90,
    0,   49,   49,   49,   49,    0,   98,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  108,  109,
  110,  111,  112,  114,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   50,   50,    0,    0,
   50,   50,   50,   50,    0,    0,  140,    0,  142,  143,
    0,    0,    0,  146,  146,    0,    0,  151,  152,  153,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  156,  157,  158,  159,  160,  161,  162,  163,
  164,  165,  166,  167,  168,  169,    0,  170,    0,    0,
    0,   90,    0,    0,    0,    0,    0,  179,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  146,    0,    0,    0,  191,    0,    0,
    0,    0,  195,    0,    0,    0,    0,    0,  197,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  215,  216,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   95,   35,   36,   41,  137,  262,   40,  286,   41,   41,
   91,   45,   91,   59,   91,   46,   41,   37,  267,   44,
   41,   59,   42,   44,  102,   59,   46,   47,   41,  123,
   64,   44,   30,   33,   32,   35,   36,   58,   59,  286,
   40,  174,   40,   37,  177,   45,  286,   59,   42,   43,
   11,   45,   46,   47,   59,   41,  266,   18,   44,   39,
   91,   41,  272,   41,   64,   40,   44,   33,  201,   35,
   36,   91,   93,  283,   40,  264,  265,  210,   37,   45,
   58,   59,   41,   42,   43,   44,   45,   46,   47,  123,
   93,  125,   40,  171,   41,   56,   41,   91,   64,   58,
   59,   60,   61,   62,  199,   41,   44,   33,   44,   35,
   36,   40,  123,   40,   40,   93,   40,   40,   40,   45,
   40,   40,   40,  123,   41,  125,   40,   44,   33,   40,
   35,   36,   91,   40,   93,   40,   61,  286,   64,   37,
   45,   58,   59,   41,   42,   43,   44,   45,   37,   47,
   59,   59,   41,   42,   43,   44,   45,  123,   47,   64,
   58,   59,   60,   59,   62,   59,   59,   40,  286,   58,
   59,   60,   41,   62,   41,   91,   93,   37,   40,   59,
   41,   41,   42,   43,   44,   45,   41,   47,   93,  257,
  258,  259,  260,  261,  262,   93,  286,   41,   58,   59,
   60,   44,   62,   41,   93,  286,   44,  286,  123,  286,
  257,  258,  259,  260,  261,  262,  274,   41,  286,   41,
   58,   59,   58,  257,  258,  259,  260,  261,  262,  263,
  264,  265,  266,   93,  268,  269,  270,  271,   58,  273,
  286,  275,  276,  277,  278,  279,  280,  281,  282,  283,
  284,  285,  286,  286,  286,   93,  290,  257,  258,  259,
  260,  261,  262,  263,    0,  123,  266,  288,  268,  269,
  270,  271,  125,  273,  286,  275,  276,  277,  278,  279,
  280,  281,  282,  283,  284,  285,   59,   41,   41,  286,
  290,  257,  258,  259,  260,  261,  262,  263,   59,   41,
  266,    3,  268,  269,  270,  271,   11,  273,   32,  275,
  276,  277,  278,  279,  280,  281,  282,  283,  284,  285,
  139,  200,   -1,   -1,  290,   -1,   -1,   -1,  287,  288,
   -1,   -1,  291,  292,  293,  294,  262,   -1,   -1,   -1,
  266,   -1,  268,   -1,   -1,  271,   -1,   -1,   -1,   -1,
   -1,  277,  278,  125,   -1,  281,  282,  283,  284,  285,
   -1,  266,   -1,  268,  290,   41,  271,   -1,   44,   -1,
  287,  288,  277,  278,   -1,   -1,  281,  282,  283,  284,
  285,   -1,   58,   59,   -1,  290,   -1,   -1,   -1,  287,
  288,   -1,   -1,  291,  292,  293,  294,   -1,  287,  288,
   -1,   -1,  291,  292,  293,  294,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   37,   -1,   -1,   93,   41,   42,
   43,   44,   45,   41,   47,   -1,   44,  287,  288,   -1,
   -1,  291,  292,  293,  294,   58,   59,   60,   37,   62,
   58,   59,   41,   42,   43,   44,   45,   -1,   47,  287,
  288,   -1,   -1,   -1,   -1,  293,  294,   -1,   -1,   58,
   59,   60,   -1,   62,   33,   -1,   35,   36,   -1,   -1,
   93,   40,   -1,   -1,   37,   93,   45,   -1,   41,   42,
   43,   -1,   45,   46,   47,  257,  258,  259,  260,  261,
  262,   -1,   -1,   -1,   93,   64,   59,   60,   37,   62,
   -1,   -1,   -1,   42,   43,   -1,   45,   46,   47,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  289,   -1,   58,
   -1,   60,   -1,   62,   -1,   37,   -1,   -1,   91,   41,
   42,   43,   -1,   45,   46,   47,   37,   -1,   -1,   -1,
   41,   42,   43,   -1,   45,   46,   47,   -1,   60,   -1,
   62,   -1,   91,   -1,   41,   -1,   -1,   44,   37,   60,
   -1,   62,   41,   42,   43,   -1,   45,   46,   47,   -1,
   -1,   58,   59,   -1,   -1,   -1,   -1,   -1,   -1,   91,
   -1,   60,   -1,   62,   -1,   -1,   -1,   -1,   37,   -1,
   91,   -1,   41,   42,   43,   -1,   45,   46,   47,   37,
   -1,   -1,   -1,   41,   42,   43,   93,   45,   46,   47,
   -1,   60,   91,   62,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   37,   60,   -1,   62,   41,   42,   43,   -1,   45,
   46,   47,   -1,   41,   -1,   -1,   44,   -1,   -1,   -1,
   -1,   46,   91,   -1,   60,   -1,   62,   -1,   -1,   37,
   58,   59,   -1,   91,   42,   43,   44,   45,   46,   47,
   -1,   -1,   -1,   -1,  287,  288,   -1,   -1,  291,  292,
  293,  294,   60,   -1,   62,   91,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   93,   -1,   -1,  287,  288,
   95,   -1,  291,  292,  293,  294,   -1,  266,   -1,  268,
   -1,   -1,  271,   91,   -1,   -1,   -1,   -1,  277,  278,
   -1,   -1,  281,  282,  283,  284,  285,   -1,   -1,   -1,
   -1,  290,   -1,   -1,  287,  288,   -1,   -1,  291,  292,
  293,  294,  137,   -1,   37,   -1,   -1,   -1,   -1,   42,
   43,   -1,   45,   46,   47,   -1,   -1,   -1,  287,  288,
   -1,   -1,  291,  292,  293,  294,   59,   60,   -1,   62,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  174,
   -1,   -1,  177,   -1,   -1,  287,  288,   -1,   -1,  291,
  292,  293,  294,   -1,   -1,   -1,  287,  288,   91,   -1,
  291,  292,  293,  294,  199,   -1,  201,   -1,   -1,   -1,
  287,  288,   -1,   -1,   -1,  210,  293,  294,  287,  288,
   -1,   37,  291,  292,  293,  294,   42,   43,   -1,   45,
   46,   47,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   41,
   -1,   -1,   44,   -1,   60,   -1,   62,   -1,  287,  288,
   -1,   -1,  291,  292,  293,  294,   58,   59,   -1,  287,
  288,   -1,   -1,  291,  292,  293,  294,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   91,   -1,   93,   -1,   -1,
   41,  287,  288,   44,   -1,  291,  292,  293,  294,  287,
  288,   93,   -1,   -1,   37,  293,  294,   58,   59,   42,
   43,   -1,   45,   46,   47,   -1,   -1,   -1,   -1,  287,
  288,   -1,   -1,  291,  292,  293,  294,   60,   37,   62,
   -1,   -1,   -1,   42,   43,   -1,   45,   46,   47,   37,
   -1,   -1,   93,   -1,   42,   43,   -1,   45,   46,   47,
   59,   60,   -1,   62,   -1,   -1,   -1,   -1,   91,   -1,
   93,   59,   60,   37,   62,   -1,   -1,   -1,   42,   43,
   -1,   45,   46,   47,   -1,   37,   -1,   -1,   -1,   -1,
   42,   43,   91,   45,   46,   47,   60,   -1,   62,   41,
   -1,   -1,   44,   91,   -1,   -1,   -1,   -1,   60,   -1,
   62,   -1,   -1,   -1,  287,  288,   58,   59,  291,  292,
  293,  294,   -1,   37,   -1,   -1,   -1,   91,   42,   43,
   -1,   45,   46,   47,   -1,   37,   -1,   -1,   -1,   91,
   42,   43,   -1,   45,   46,   47,   60,   37,   62,   -1,
   -1,   93,   42,   43,   -1,   45,   46,   47,   60,   -1,
   62,   41,   -1,   43,   44,   45,   -1,   -1,   -1,   -1,
   60,   -1,   62,   -1,   -1,   -1,   -1,   91,   58,   59,
   60,   -1,   62,   -1,   -1,   -1,   -1,   -1,   -1,   91,
   -1,  287,  288,   -1,   46,  291,  292,  293,  294,   -1,
   41,   91,   43,   44,   45,  287,  288,   -1,   -1,   -1,
   -1,  293,  294,   93,   -1,   -1,   -1,   58,   59,   60,
   -1,   62,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   95,   -1,   -1,  287,  288,   -1,   -1,
   -1,   -1,   93,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  287,  288,   -1,   -1,  291,  292,
  293,  294,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  137,   -1,   -1,  287,  288,
   -1,   -1,  291,  292,  293,  294,   -1,   -1,   -1,  287,
  288,   -1,   -1,  291,  292,  293,  294,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  174,  287,  288,  177,   -1,  291,  292,  293,
  294,   -1,   -1,   -1,   -1,  287,  288,   -1,   -1,  291,
  292,  293,  294,   -1,   -1,  287,  288,  199,   -1,  201,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  210,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  287,   -1,   -1,   -1,  291,  292,  293,
  294,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  291,
  292,  293,  294,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  291,  292,   -1,   -1,   -1,   -1,  287,  288,   47,
   -1,  291,  292,  293,  294,   -1,   54,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   66,   67,
   68,   69,   70,   71,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  287,  288,   -1,   -1,
  291,  292,  293,  294,   -1,   -1,   94,   -1,   96,   97,
   -1,   -1,   -1,  101,  102,   -1,   -1,  105,  106,  107,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  120,  121,  122,  123,  124,  125,  126,  127,
  128,  129,  130,  131,  132,  133,   -1,  135,   -1,   -1,
   -1,  139,   -1,   -1,   -1,   -1,   -1,  145,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  171,   -1,   -1,   -1,  175,   -1,   -1,
   -1,   -1,  180,   -1,   -1,   -1,   -1,   -1,  186,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  211,  212,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=296;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,"'#'","'$'","'%'",null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'",
"';'","'<'","'='","'>'",null,"'@'",null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"VOID","BOOL","INT","COMPLEX",
"STRING","CLASS","DO","OD","DOOD","NULL","EXTENDS","THIS","WHILE","FOR","CASE",
"DEFAULT","IF","ELSE","RETURN","BREAK","NEW","SUPER","PRINT","PRINT_COMP",
"READ_INTEGER","READ_LINE","LITERAL","DCOPY","SCOPY","IDENTIFIER","AND","OR",
"STATIC","INSTANCEOF","LESS_EQUAL","GREATER_EQUAL","EQUAL","NOT_EQUAL","UMINUS",
"EMPTY",
};
final static String yyrule[] = {
"$accept : Program",
"Program : ClassList",
"ClassList : ClassList ClassDef",
"ClassList : ClassDef",
"VariableDef : Variable ';'",
"Variable : Type IDENTIFIER",
"Type : INT",
"Type : VOID",
"Type : BOOL",
"Type : COMPLEX",
"Type : STRING",
"Type : CLASS IDENTIFIER",
"Type : Type '[' ']'",
"ClassDef : CLASS IDENTIFIER ExtendsClause '{' FieldList '}'",
"ExtendsClause : EXTENDS IDENTIFIER",
"ExtendsClause :",
"FieldList : FieldList VariableDef",
"FieldList : FieldList FunctionDef",
"FieldList :",
"Formals : VariableList",
"Formals :",
"VariableList : VariableList ',' Variable",
"VariableList : Variable",
"FunctionDef : STATIC Type IDENTIFIER '(' Formals ')' StmtBlock",
"FunctionDef : Type IDENTIFIER '(' Formals ')' StmtBlock",
"StmtBlock : '{' StmtList '}'",
"StmtList : StmtList Stmt",
"StmtList :",
"Stmt : VariableDef",
"Stmt : SimpleStmt ';'",
"Stmt : IfStmt",
"Stmt : WhileStmt",
"Stmt : ForStmt",
"Stmt : ReturnStmt ';'",
"Stmt : PrintStmt ';'",
"Stmt : BreakStmt ';'",
"Stmt : StmtBlock",
"Stmt : DOODStmt ';'",
"SimpleStmt : LValue '=' Expr",
"SimpleStmt : Call",
"SimpleStmt :",
"Receiver : Expr '.'",
"Receiver :",
"LValue : Receiver IDENTIFIER",
"LValue : Expr '[' Expr ']'",
"Call : Receiver IDENTIFIER '(' Actuals ')'",
"Expr : LValue",
"Expr : Call",
"Expr : Constant",
"Expr : Expr '+' Expr",
"Expr : Expr '-' Expr",
"Expr : Expr '*' Expr",
"Expr : Expr '/' Expr",
"Expr : Expr '%' Expr",
"Expr : Expr EQUAL Expr",
"Expr : Expr NOT_EQUAL Expr",
"Expr : Expr '<' Expr",
"Expr : Expr '>' Expr",
"Expr : Expr LESS_EQUAL Expr",
"Expr : Expr GREATER_EQUAL Expr",
"Expr : Expr AND Expr",
"Expr : Expr OR Expr",
"Expr : '(' Expr ')'",
"Expr : '-' Expr",
"Expr : '!' Expr",
"Expr : '@' Expr",
"Expr : '$' Expr",
"Expr : '#' Expr",
"Expr : READ_INTEGER '(' ')'",
"Expr : READ_LINE '(' ')'",
"Expr : THIS",
"Expr : SUPER",
"Expr : NEW IDENTIFIER '(' ')'",
"Expr : NEW Type '[' Expr ']'",
"Expr : INSTANCEOF '(' Expr ',' IDENTIFIER ')'",
"Expr : '(' CLASS IDENTIFIER ')' Expr",
"Expr : CASE '(' Expr ')' '{' CaseList DefaultExpr '}'",
"Expr : DCOPY '(' Expr ')'",
"Expr : SCOPY '(' Expr ')'",
"Constant : LITERAL",
"Constant : NULL",
"Actuals : ExprList",
"Actuals :",
"ExprList : ExprList ',' Expr",
"ExprList : Expr",
"WhileStmt : WHILE '(' Expr ')' Stmt",
"ForStmt : FOR '(' SimpleStmt ';' Expr ';' SimpleStmt ')' Stmt",
"BreakStmt : BREAK",
"IfStmt : IF '(' Expr ')' Stmt ElseClause",
"ElseClause : ELSE Stmt",
"ElseClause :",
"ReturnStmt : RETURN Expr",
"ReturnStmt : RETURN",
"PrintStmt : PRINT '(' ExprList ')'",
"PrintStmt : PRINT_COMP '(' ExprList ')'",
"CaseList : CaseList CaseDef",
"CaseList :",
"CaseDef : Constant ':' Expr ';'",
"DefaultExpr : DEFAULT ':' Expr ';'",
"DOODStmt : DO DOODBranch OD",
"DOODBranch : DOODBranch DOOD DOODSubStmt",
"DOODBranch : DOODSubStmt",
"DOODSubStmt : Expr ':' Stmt",
};

//#line 504 "Parser.y"
    
	/**
	 * 打印当前归约所用的语法规则<br>
	 * 请勿修改。
	 */
    public boolean onReduce(String rule) {
		if (rule.startsWith("$$"))
			return false;
		else
			rule = rule.replaceAll(" \\$\\$\\d+", "");

   	    if (rule.endsWith(":"))
    	    System.out.println(rule + " <empty>");
   	    else
			System.out.println(rule);
		return false;
    }
    
    public void diagnose() {
		addReduceListener(this);
		yyparse();
	}
//#line 713 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    //if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      //if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        //if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        //if (yychar < 0)    //it it didn't work/error
        //  {
        //  yychar = 0;      //change it to default string (no -1!)
          //if (yydebug)
          //  yylexdebug(yystate,yychar);
        //  }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        //if (yydebug)
          //debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      //if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0 || valptr<0)   //check for under & overflow here
            {
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            //if (yydebug)
              //debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            //if (yydebug)
              //debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0 || valptr<0)   //check for under & overflow here
              {
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        //if (yydebug)
          //{
          //yys = null;
          //if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          //if (yys == null) yys = "illegal-symbol";
          //debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          //}
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    //if (yydebug)
      //debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    if (reduceListener == null || reduceListener.onReduce(yyrule[yyn])) // if intercepted!
      switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 52 "Parser.y"
{
						tree = new Tree.TopLevel(val_peek(0).clist, val_peek(0).loc);
					}
break;
case 2:
//#line 58 "Parser.y"
{
						yyval.clist.add(val_peek(0).cdef);
					}
break;
case 3:
//#line 62 "Parser.y"
{
                		yyval.clist = new ArrayList<Tree.ClassDef>();
                		yyval.clist.add(val_peek(0).cdef);
                	}
break;
case 5:
//#line 72 "Parser.y"
{
						yyval.vdef = new Tree.VarDef(val_peek(0).ident, val_peek(1).type, val_peek(0).loc);
					}
break;
case 6:
//#line 78 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.INT, val_peek(0).loc);
					}
break;
case 7:
//#line 82 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.VOID, val_peek(0).loc);
                	}
break;
case 8:
//#line 86 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.BOOL, val_peek(0).loc);
                	}
break;
case 9:
//#line 90 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.COMPLEX, val_peek(0).loc);
                	}
break;
case 10:
//#line 94 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.STRING, val_peek(0).loc);
                	}
break;
case 11:
//#line 98 "Parser.y"
{
                		yyval.type = new Tree.TypeClass(val_peek(0).ident, val_peek(1).loc);
                	}
break;
case 12:
//#line 102 "Parser.y"
{
                		yyval.type = new Tree.TypeArray(val_peek(2).type, val_peek(2).loc);
                	}
break;
case 13:
//#line 108 "Parser.y"
{
						yyval.cdef = new Tree.ClassDef(val_peek(4).ident, val_peek(3).ident, val_peek(1).flist, val_peek(5).loc);
					}
break;
case 14:
//#line 114 "Parser.y"
{
						yyval.ident = val_peek(0).ident;
					}
break;
case 15:
//#line 118 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 16:
//#line 124 "Parser.y"
{
						yyval.flist.add(val_peek(0).vdef);
					}
break;
case 17:
//#line 128 "Parser.y"
{
						yyval.flist.add(val_peek(0).fdef);
					}
break;
case 18:
//#line 132 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.flist = new ArrayList<Tree>();
                	}
break;
case 20:
//#line 140 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.vlist = new ArrayList<Tree.VarDef>(); 
                	}
break;
case 21:
//#line 147 "Parser.y"
{
						yyval.vlist.add(val_peek(0).vdef);
					}
break;
case 22:
//#line 151 "Parser.y"
{
                		yyval.vlist = new ArrayList<Tree.VarDef>();
						yyval.vlist.add(val_peek(0).vdef);
                	}
break;
case 23:
//#line 158 "Parser.y"
{
						yyval.fdef = new MethodDef(true, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 24:
//#line 162 "Parser.y"
{
						yyval.fdef = new MethodDef(false, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 25:
//#line 168 "Parser.y"
{
						yyval.stmt = new Block(val_peek(1).slist, val_peek(2).loc);
					}
break;
case 26:
//#line 174 "Parser.y"
{
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 27:
//#line 178 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.slist = new ArrayList<Tree>();
                	}
break;
case 28:
//#line 185 "Parser.y"
{
						yyval.stmt = val_peek(0).vdef;
					}
break;
case 29:
//#line 190 "Parser.y"
{
                		if (yyval.stmt == null) {
                			yyval.stmt = new Tree.Skip(val_peek(0).loc);
                		}
                	}
break;
case 38:
//#line 206 "Parser.y"
{
						yyval.stmt = new Tree.Assign(val_peek(2).lvalue, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 39:
//#line 210 "Parser.y"
{
                		yyval.stmt = new Tree.Exec(val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 40:
//#line 214 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 42:
//#line 221 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 43:
//#line 227 "Parser.y"
{
						yyval.lvalue = new Tree.Ident(val_peek(1).expr, val_peek(0).ident, val_peek(0).loc);
						if (val_peek(1).loc == null) {
							yyval.loc = val_peek(0).loc;
						}
					}
break;
case 44:
//#line 234 "Parser.y"
{
                		yyval.lvalue = new Tree.Indexed(val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 45:
//#line 240 "Parser.y"
{
						yyval.expr = new Tree.CallExpr(val_peek(4).expr, val_peek(3).ident, val_peek(1).elist, val_peek(3).loc);
						if (val_peek(4).loc == null) {
							yyval.loc = val_peek(3).loc;
						}
					}
break;
case 46:
//#line 249 "Parser.y"
{
						yyval.expr = val_peek(0).lvalue;
					}
break;
case 49:
//#line 255 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.PLUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 50:
//#line 259 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MINUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 51:
//#line 263 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MUL, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 52:
//#line 267 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.DIV, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 53:
//#line 271 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MOD, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 54:
//#line 275 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.EQ, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 55:
//#line 279 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.NE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 56:
//#line 283 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 57:
//#line 287 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 58:
//#line 291 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 59:
//#line 295 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 60:
//#line 299 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.AND, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 61:
//#line 303 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.OR, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 62:
//#line 307 "Parser.y"
{
                		yyval = val_peek(1);
                	}
break;
case 63:
//#line 311 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NEG, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 64:
//#line 315 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NOT, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 65:
//#line 319 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.RE, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 66:
//#line 323 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.IM, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 67:
//#line 327 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.COMPCAST, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 68:
//#line 331 "Parser.y"
{
                		yyval.expr = new Tree.ReadIntExpr(val_peek(2).loc);
                	}
break;
case 69:
//#line 335 "Parser.y"
{
                		yyval.expr = new Tree.ReadLineExpr(val_peek(2).loc);
                	}
break;
case 70:
//#line 339 "Parser.y"
{
                		yyval.expr = new Tree.ThisExpr(val_peek(0).loc);
                	}
break;
case 71:
//#line 343 "Parser.y"
{
                		yyval.expr = new Tree.SuperExpr(val_peek(0).loc);
                	}
break;
case 72:
//#line 347 "Parser.y"
{
                		yyval.expr = new Tree.NewClass(val_peek(2).ident, val_peek(3).loc);
                	}
break;
case 73:
//#line 351 "Parser.y"
{
                		yyval.expr = new Tree.NewArray(val_peek(3).type, val_peek(1).expr, val_peek(4).loc);
                	}
break;
case 74:
//#line 355 "Parser.y"
{
                		yyval.expr = new Tree.TypeTest(val_peek(3).expr, val_peek(1).ident, val_peek(5).loc);
                	}
break;
case 75:
//#line 359 "Parser.y"
{
                		yyval.expr = new Tree.TypeCast(val_peek(2).ident, val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 76:
//#line 363 "Parser.y"
{
                		yyval.expr = new Tree.Case(val_peek(5).expr, val_peek(2).slist, val_peek(1).stmt, val_peek(7).loc);
                	}
break;
case 77:
//#line 367 "Parser.y"
{
                		yyval.expr = new Tree.Dcopy(val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 78:
//#line 371 "Parser.y"
{
                		yyval.expr = new Tree.Scopy(val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 79:
//#line 377 "Parser.y"
{
						yyval.expr = new Tree.Literal(val_peek(0).typeTag, val_peek(0).literal, val_peek(0).loc);
					}
break;
case 80:
//#line 381 "Parser.y"
{
						yyval.expr = new Null(val_peek(0).loc);
					}
break;
case 82:
//#line 388 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.elist = new ArrayList<Tree.Expr>();
                	}
break;
case 83:
//#line 395 "Parser.y"
{
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 84:
//#line 399 "Parser.y"
{
                		yyval.elist = new ArrayList<Tree.Expr>();
						yyval.elist.add(val_peek(0).expr);
                	}
break;
case 85:
//#line 406 "Parser.y"
{
						yyval.stmt = new Tree.WhileLoop(val_peek(2).expr, val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 86:
//#line 412 "Parser.y"
{
						yyval.stmt = new Tree.ForLoop(val_peek(6).stmt, val_peek(4).expr, val_peek(2).stmt, val_peek(0).stmt, val_peek(8).loc);
					}
break;
case 87:
//#line 418 "Parser.y"
{
						yyval.stmt = new Tree.Break(val_peek(0).loc);
					}
break;
case 88:
//#line 424 "Parser.y"
{
						yyval.stmt = new Tree.If(val_peek(3).expr, val_peek(1).stmt, val_peek(0).stmt, val_peek(5).loc);
					}
break;
case 89:
//#line 430 "Parser.y"
{
						yyval.stmt = val_peek(0).stmt;
					}
break;
case 90:
//#line 434 "Parser.y"
{
						yyval = new SemValue();
					}
break;
case 91:
//#line 440 "Parser.y"
{
						yyval.stmt = new Tree.Return(val_peek(0).expr, val_peek(1).loc);
					}
break;
case 92:
//#line 444 "Parser.y"
{
                		yyval.stmt = new Tree.Return(null, val_peek(0).loc);
                	}
break;
case 93:
//#line 450 "Parser.y"
{
						yyval.stmt = new Print(val_peek(1).elist, val_peek(3).loc);
					}
break;
case 94:
//#line 454 "Parser.y"
{
						yyval.stmt = new PrintComp(val_peek(1).elist, val_peek(3).loc);
					}
break;
case 95:
//#line 460 "Parser.y"
{
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 96:
//#line 464 "Parser.y"
{
                		yyval.slist = new ArrayList<Tree>();
					}
break;
case 97:
//#line 470 "Parser.y"
{
						yyval.stmt = new Tree.Casedef(val_peek(3).typeTag, val_peek(3).literal, val_peek(1).expr, val_peek(3).loc);
					}
break;
case 98:
//#line 476 "Parser.y"
{
						yyval.stmt = new Tree.DefaultCase(val_peek(1).expr, val_peek(3).loc);
					}
break;
case 99:
//#line 482 "Parser.y"
{
						yyval.stmt = new Tree.DOODStmt(val_peek(1).slist, val_peek(2).loc);
					}
break;
case 100:
//#line 488 "Parser.y"
{
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 101:
//#line 492 "Parser.y"
{
						yyval.slist = new ArrayList<Tree>();
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 102:
//#line 498 "Parser.y"
{
						yyval.stmt = new Tree.DOODSubStmt(val_peek(2).expr, val_peek(0).stmt, val_peek(2).loc);
					}
break;
//#line 1403 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    //if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      //if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        //if (yychar<0) yychar=0;  //clean, if necessary
        //if (yydebug)
          //yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      //if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
//## The -Jnoconstruct option was used ##
//###############################################################



}
//################### END OF CLASS ##############################
