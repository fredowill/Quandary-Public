package ast;

public class FuncDef extends ASTNode {

    final VarDecl funcIdentifier;
    final VarDecl funcArgument;
    final StmtList stList;

    public FuncDef(VarDecl funcId, VarDecl funcArg, StmtList sl, Location loc) {
        super(loc);
        this.funcIdentifier = funcId;
        this.funcArgument = funcArg;
        this.stList = sl;
    }

    public VarDecl getFuncIdentifier() {
        return funcIdentifier;
    }

    public VarDecl getFuncArgs() {
        return funcArgument;
    }

    public StmtList getStmtList(){
        return stList;
    }

    @Override
    public String toString(){
        return this.funcIdentifier.toString() + "(" + this.funcArgument.toString() + ") \n\r\t" + this.stList.toString();
    }

}
