package interpreter;

import java.io.*;
import java.util.HashMap;
import java.util.Random;

import parser.ParserWrapper;
import ast.*;

public class Interpreter {

    // Process return codes
    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_PARSING_ERROR = 1;
    public static final int EXIT_STATIC_CHECKING_ERROR = 2;
    public static final int EXIT_DYNAMIC_TYPE_ERROR = 3;
    public static final int EXIT_NIL_REF_ERROR = 4;
    public static final int EXIT_QUANDARY_HEAP_OUT_OF_MEMORY_ERROR = 5;
    public static final int EXIT_DATA_RACE_ERROR = 6;
    public static final int EXIT_NONDETERMINISM_ERROR = 7;

    static private Interpreter interpreter;

    public static Interpreter getInterpreter() {
        return interpreter;
    }

    public static void main(String[] args) {
        String gcType = "NoGC"; // default for skeleton, which only supports NoGC
        long heapBytes = 1 << 14;
        int i = 0;
        String filename;
        long quandaryArg;
        try {
            for (; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("-")) {
                    if (arg.equals("-gc")) {
                        gcType = args[i + 1];
                        i++;
                    } else if (arg.equals("-heapsize")) {
                        heapBytes = Long.valueOf(args[i + 1]);
                        i++;
                    } else {
                        throw new RuntimeException("Unexpected option " + arg);
                    }
                } else {
                    if (i != args.length - 2) {
                        throw new RuntimeException("Unexpected number of arguments");
                    }
                    break;
                }
            }
            filename = args[i];
            quandaryArg = Long.valueOf(args[i + 1]);
        } catch (Exception ex) {
            System.out.println("Expected format: quandary [OPTIONS] QUANDARY_PROGRAM_FILE INTEGER_ARGUMENT");
            System.out.println("Options:");
            System.out.println("  -gc (MarkSweep|Explicit|NoGC)");
            System.out.println("  -heapsize BYTES");
            System.out.println("BYTES must be a multiple of the word size (8)");
            return;
        }

        Program astRoot = null;
        Reader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            astRoot = ParserWrapper.parse(reader);
        } catch (Exception ex) {
            ex.printStackTrace();
            Interpreter.fatalError("Uncaught parsing error: " + ex, Interpreter.EXIT_PARSING_ERROR);
        }
        //uncommented for debugging
        
    //    astRoot.println(System.out);
        interpreter = new Interpreter(astRoot);
        interpreter.initMemoryManager(gcType, heapBytes);
        String returnValueAsString = interpreter.executeRoot(astRoot, quandaryArg).toString();
        System.out.println("Interpreter returned " + returnValueAsString);
        
    }

    final Program astRoot;
    final Random random;

    private Interpreter(Program astRoot) {
        this.astRoot = astRoot;
        this.random = new Random();
    }

    void initMemoryManager(String gcType, long heapBytes) {
        if (gcType.equals("Explicit")) {
            throw new RuntimeException("Explicit not implemented");            
        } else if (gcType.equals("MarkSweep")) {
            throw new RuntimeException("MarkSweep not implemented");            
        } else if (gcType.equals("RefCount")) {
            throw new RuntimeException("RefCount not implemented");            
        } else if (gcType.equals("NoGC")) {
            // Nothing to do
        }
    }

    Object executeRoot(Program astRoot, long arg) {
        HashMap<String, QVal> context = new HashMap<String, QVal>();
        HashMap<String, FuncDef> funcDefMap = new HashMap<String, FuncDef>();
        FuncDefList funcList = astRoot.getFuncDefList();
        FuncDef func = astRoot.getFunc();
        buildFuncDefMap(funcList, funcDefMap);
        FuncDef main = funcDefMap.get("main");
        QIntVal QArg = new QIntVal(arg);
        QArg.mutable = main.getFuncParams().getVarDecl().isMutable();
        context.put(main.getFuncParams().getVarDecl().getVarName(),QArg );
        return executeFunc(main, context, funcDefMap);
    }
    
    void buildFuncDefMap(FuncDefList funcList, HashMap<String, FuncDef> funcDefMap){
        FuncDef temp;
        FuncDefList list = funcList;
        while(list != null ){
            temp = list.getFunc();
            String funcArgIdent =  temp.getFuncIdentifier().getVarName();
            funcDefMap.put(funcArgIdent, temp);
            list = list.getFuncList();
        }
    }

    QVal executeFunc(FuncDef func, HashMap<String, QVal> context, HashMap<String, FuncDef> funcDefMap){
        StmtList stmtList = func.getStmtList();
        QVal ret = executeStmtList(stmtList, context, funcDefMap);
       // System.out.println("Running func:" + func);
        return ret;
    }

    QVal executeStmtList(StmtList slist, HashMap<String, QVal> context, HashMap<String, FuncDef> funcs){
        QVal ret ;
        HashMap<String, QVal> copiedMap = (HashMap<String, QVal>) context.clone();
        while (slist != null){
            Stmt s = slist.getStmt();
            slist = slist.getStmtList();
            ret = executeStmt(s, copiedMap, funcs);
            if(ret != null){
                return ret;
            }
            updateMap(context, copiedMap);
        }
        return null;
    }

    void updateMap(HashMap<String, QVal> originalMap, HashMap<String, QVal> copiedMap) {
        for (HashMap.Entry<String, QVal> entry : copiedMap.entrySet()) {
            String key = entry.getKey();
            QVal copiedValue = entry.getValue();
    
            // Check if the key exists in the original map
            if (originalMap.containsKey(key)) {
                // Update the value in the original map
                originalMap.remove(key);
                originalMap.put(key, copiedValue);
            }
        }
    }
    //add print stmt class 
    QVal executeStmt(Stmt s, HashMap<String, QVal> context, HashMap<String, FuncDef> funcs){
        if (s instanceof VarDeclStmt){
            VarDeclStmt varstmt = (VarDeclStmt)s;
           // System.out.println("varstmt expr: "+varstmt.getExpr());
            QVal value = evaluateExpr(varstmt.getExpr(), context, funcs);
            VarDecl vardecl = varstmt.getVarDecl();
            String varName = vardecl.getVarName();
            value.mutable = vardecl.isMutable();
            context.put(varName, value); //check if it already exisits
            return null;
        } else if(s instanceof AssignStmt) {
            AssignStmt a = (AssignStmt) s;
            String id = a.getIdent();
            Expr e = a.getExpr();
            QVal val = evaluateExpr(e, context, funcs);
            if(context.containsKey(id)){
                QVal prevVal = context.remove(id);
                val.mutable = prevVal.mutable;
                context.put(id, val);
            } else {
                throw new RuntimeException("Variable not declared");
            }
            return null;
        } else if(s instanceof CallStmt){
            CallStmt cStmt = (CallStmt) s;
            String id = cStmt.getIdent();
            ExprList elist = cStmt.getExprList();
            if(id.equals("randomInt")){
                CallExpr.randomInt(( (QIntVal) evaluateExpr(elist.getExpr(), context, funcs)).getInt());
            } else if(id.equals("left")){
                CallStmt.left(((QRefVal) evaluateExpr(elist.getExpr(), context, funcs)));
            } else if(id.equals("right")){
                CallStmt.right(((QRefVal) evaluateExpr(elist.getExpr(), context, funcs)));
            } else if(id.equals("isAtom")){
                CallStmt.isAtom(evaluateExpr(elist.getExpr(), context, funcs));
            } else if(id.equals("isNil")){
                CallStmt.isNil(evaluateExpr(elist.getExpr(), context, funcs));
            } else if(id.equals("setLeft")){
                CallStmt.setLeft((QRefVal)evaluateExpr(elist.getExpr(), context, funcs), evaluateExpr(elist.getExprList().getExpr(), context, funcs));
            } else if(id.equals("setRight")){
                CallStmt.setRight((QRefVal)evaluateExpr(elist.getExpr(), context, funcs), evaluateExpr(elist.getExprList().getExpr(), context, funcs));               
            } else {
                FuncDef f = funcs.get(id);
                HashMap<String, QVal> innerScope = new HashMap<String, QVal>();
                executeExprList(f.getFuncParams(), elist, innerScope, context, funcs);
                executeFunc(f, innerScope, funcs);
            }
            return null;

        }else if (s instanceof IfStmt) {
            IfStmt i = (IfStmt)s;
            Cond cond = i.getCond();
            Stmt sBlock = i.getStmt();
            if(evaluateCond(cond, context, funcs)){
                return executeStmt(sBlock, context, funcs);
            }
            return null;
        } else if (s instanceof IfElseStmt){
            IfElseStmt i = (IfElseStmt)s;
            Cond cond = i.getCond();
            Stmt sIfBlock = i.getStmt1();
            Stmt sElseBlock = i.getStmt2();
            if(evaluateCond(cond, context, funcs)){
                return executeStmt(sIfBlock, context, funcs);
            } else {
                return executeStmt(sElseBlock, context, funcs);
            } 
        } else if (s instanceof WhileStmt){
            WhileStmt w = (WhileStmt)s;
            Cond cond = w.getCond();
            Stmt stmt = w.getStmt();
            QVal ret = null;
            while(evaluateCond(cond, context, funcs)){
                ret = executeStmt(stmt, context, funcs);
                if (ret != null) break;
            }
            return ret;
        } else if( s instanceof ReturnStmt){
            ReturnStmt r = (ReturnStmt) s;
            return evaluateExpr(r.getExpr(), context, funcs);
        } else if (s instanceof StmtList){
            return executeStmtList((StmtList)s, context, funcs);
        }else if( s instanceof PrintStmt){
            PrintStmt p = (PrintStmt) s;
            String str = evaluateExpr(p.getExpr(), context, funcs).toString();
            System.out.println(str);
            return null;
        } else {
            throw new RuntimeException("Unhandled Stmt type");
        }

    }

    //dont forget breaks if using switch 
    //Longs are objects, dont use ==, use .equals



    Boolean evaluateCond(Cond condition, HashMap<String, QVal> context, HashMap<String, FuncDef> funcs){
        if (condition instanceof CompareCond){
            CompareCond c = (CompareCond) condition;
            Expr leftSide = c.getLeftExpr();
            Expr rightSide = c.getRightExpr();
            int comparator = c.getComparator();
            switch(comparator) {
                case CompareCond.LT: return ((QIntVal)evaluateExpr(leftSide, context, funcs)).getInt() < ((QIntVal)evaluateExpr(rightSide, context, funcs)).getInt();
                case CompareCond.GT: return ((QIntVal)evaluateExpr(leftSide, context, funcs)).getInt() > ((QIntVal)evaluateExpr(rightSide, context, funcs)).getInt();
                case CompareCond.LE: return ((QIntVal)evaluateExpr(leftSide, context, funcs)).getInt() <= ((QIntVal)evaluateExpr(rightSide, context, funcs)).getInt();
                case CompareCond.EQ: return ((QIntVal)evaluateExpr(leftSide, context, funcs)).getInt() == (((QIntVal)evaluateExpr(rightSide, context, funcs)).getInt());
                case CompareCond.GE: return ((QIntVal)evaluateExpr(leftSide, context, funcs)).getInt() >= ((QIntVal)evaluateExpr(rightSide, context, funcs)).getInt();
                case CompareCond.NE: return ((QIntVal)evaluateExpr(leftSide, context, funcs)).getInt() != ((QIntVal)evaluateExpr(rightSide, context, funcs)).getInt();
            } 
            
        } else if(condition instanceof LogicalCond){
            LogicalCond c = (LogicalCond) condition;
            Cond leftSide = c.getCond1();
            Cond rightSide = c.getCond2();
            int comparator = c.getComparator();
            switch(comparator) {
                case LogicalCond.AND: return evaluateCond(leftSide, context, funcs) && evaluateCond(rightSide, context, funcs);
                case LogicalCond.NOT: return !evaluateCond(leftSide, context, funcs);
                case LogicalCond.OR: return evaluateCond(leftSide, context, funcs) || evaluateCond(rightSide, context, funcs);
            } 

        } 
            throw new RuntimeException("Unexpected Cond Type");
        
    }

    //EXPRESSIONS

    QVal evaluateExpr(Expr expr, HashMap<String, QVal> context, HashMap<String, FuncDef> funcs) {
        if (expr instanceof ConstExpr) {
            return ((ConstExpr)expr).getValue();
        } else if(expr instanceof VarExpr ) {
            VarExpr v = (VarExpr) expr;
            String vname = v.getVariableName();
            if(context.containsKey(vname)){
                return context.get(vname);
            }
        } else if ( expr instanceof UMinusExpr) {
            UMinusExpr uexpr = (UMinusExpr)expr; 
                return new QIntVal(0 - ((QIntVal)evaluateExpr(uexpr.getUMinusExpr(), context, funcs)).getInt());
        } else if (expr instanceof BinaryExpr) {
            BinaryExpr binaryExpr = (BinaryExpr)expr;
            switch (binaryExpr.getOperator()) {
                case BinaryExpr.PLUS: return new QIntVal(((QIntVal)evaluateExpr(binaryExpr.getLeftExpr(), context, funcs)).getInt() + ((QIntVal)evaluateExpr(binaryExpr.getRightExpr(), context, funcs)).getInt());
                case BinaryExpr.MINUS: return new QIntVal(((QIntVal)evaluateExpr(binaryExpr.getLeftExpr(), context, funcs)).getInt() - ((QIntVal)evaluateExpr(binaryExpr.getRightExpr(), context, funcs)).getInt());
                case BinaryExpr.TIMES: return new QIntVal(((QIntVal)evaluateExpr(binaryExpr.getLeftExpr(), context, funcs)).getInt() * ((QIntVal)evaluateExpr(binaryExpr.getRightExpr(), context, funcs)).getInt());
                case BinaryExpr.DOT : return new QRefVal(false, new QObj(evaluateExpr(binaryExpr.getLeftExpr(), context, funcs), evaluateExpr(binaryExpr.getRightExpr(), context, funcs)));
                default: throw new RuntimeException("Unhandled operator");
            }
        } else if (expr instanceof CallExpr){
            CallExpr callexpr = (CallExpr)expr;
            String ident = callexpr.getIdent();
            ExprList elist = callexpr.getExprList();
            if(ident.equals("randomInt")){
                long intValue = ((QIntVal) evaluateExpr(elist.getExpr(), context, funcs)).getInt();
                return new QIntVal(CallExpr.randomInt(intValue));
            } else if(ident.equals("left")){
                return CallStmt.left(((QRefVal) evaluateExpr(elist.getExpr(), context, funcs)));
            } else if(ident.equals("right")){
                return CallStmt.right(((QRefVal) evaluateExpr(elist.getExpr(), context, funcs)));
            } else if(ident.equals("isAtom")){
                return CallStmt.isAtom(evaluateExpr(elist.getExpr(), context, funcs));
            } else if(ident.equals("isNil")){
                return CallStmt.isNil(evaluateExpr(elist.getExpr(), context, funcs));
            } else if(ident.equals("setLeft")){
                return CallStmt.setLeft((QRefVal)evaluateExpr(elist.getExpr(), context, funcs), evaluateExpr(elist.getExprList().getExpr(), context, funcs));
            } else if(ident.equals("setRight")){
                return CallStmt.setRight((QRefVal)evaluateExpr(elist.getExpr(), context, funcs), evaluateExpr(elist.getExprList().getExpr(), context, funcs));               
            } 
            if(funcs.containsKey(ident)){
                FuncDef func = funcs.get(ident);
                HashMap<String, QVal> newScope = new HashMap<String, QVal>();
                //System.out.println("expr: " + callexpr);
                //System.out.println("elist: " + elist);
                executeExprList(func.getFuncParams(), elist, newScope, context, funcs);
                return executeFunc(func, newScope, funcs);
            } else {
                throw new RuntimeException("Unexpected Function call");
            }
        }else if (expr instanceof CastExpr){
            CastExpr c = (CastExpr)expr;
            Expr expr1 = c.getExpr();
            Type type = c.getType();
            QVal val = evaluateExpr(expr1, context, funcs);
            switch(type.getVarType()){
                case Type.INT:
                if(val instanceof QIntVal) return val;
                return (QIntVal)evaluateExpr(expr1, context, funcs);
                
                case Type.REF:
                if(val instanceof QRefVal) return val;
                return (QRefVal)evaluateExpr(expr1, context, funcs);

                case Type.Q:
                if(val instanceof QVal) return val;
                return (QVal)evaluateExpr(expr1, context, funcs);

                default: throw new RuntimeException("Cast of undefinded type");
            }
        } 
            System.out.println("Expr:" + expr);
            throw new RuntimeException("Unhandled Expr type");
    }

    QVal executeExprList(FormalDeclList formalDeclList, ExprList elist, HashMap<String, QVal> innerContext, HashMap<String, QVal> context, HashMap<String, FuncDef> funcs){
        if(elist != null){
            ExprList neExprList = elist;
           // System.out.println("ExprList: " + elist);
            FormalDeclList fdl = formalDeclList;
            if(fdl !=null){
                //System.out.println(">1 parameter");
                FormalDeclList neFdl = fdl;
                //System.out.println("neFdl: " + neFdl);
               // System.out.println("neExprList: " + neExprList);
                while(neExprList != null){
                    VarDecl var = neFdl.getVarDecl();
                    neFdl = neFdl.getFuncParams();
                    Expr e = neExprList.getExpr();
                    neExprList = neExprList.getExprList();
                    QVal tempCheck = evaluateExpr(e, context, funcs);
                    tempCheck.mutable = var.isMutable();
                    innerContext.put(var.getVarName(), tempCheck);
                }
            }
        }
        //System.out.println("innerContext: " + innerContext);
        return null;
    }

	public static void fatalError(String message, int processReturnCode) {
        System.out.println(message);
        System.exit(processReturnCode);
	}
}
