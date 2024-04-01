package ast;

public class FuncDefList extends ASTNode {

    final FuncDef func;
    final FuncDefList funcList;

    public FuncDefList(FuncDef f, FuncDefList fdl, Location loc) {
        super(loc);
        this.func = f;
        this.funcList = fdl;
    }

    public FuncDef getFunc() {
        return this.func;
    }

    public FuncDefList getFuncList() {
        return this.funcList;
    }


}
