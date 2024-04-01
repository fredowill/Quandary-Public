package ast;

public class FuncDef extends ASTNode {

    final VarDecl funcIdentifier;
    final FormalDeclList functionArguments;
    final StmtList stList;

    public FuncDef(VarDecl funcId, FormalDeclList fdl, StmtList sl, Location loc) {
        super(loc);
        this.funcIdentifier = funcId;
        this.functionArguments = fdl;
        this.stList = sl;
    }

    public VarDecl getFuncIdentifier() {
        return funcIdentifier;
    }


    public FormalDeclList getFuncParams() {
        return this.functionArguments;
    }

    public StmtList getStmtList(){
        return stList;
    }

   /*  @Override
    public String toString(){
        return this.funcIdentifier.toString() + "(" + this.functionArguments.toString() + ") \n\r\t" + this.stList.toString();
    }*/

}
