package ast;

public class VarDeclStmt extends Stmt {

    final Expr rValue;
    final VarDecl lValue;

    public VarDeclStmt(VarDecl var, Expr expr, Location loc) {
        super(loc);
        this.rValue = expr;
        this.lValue = var;
    }

    public Expr getExpr(){
        return this.rValue;
    }

    public VarDecl getVarDecl(){
        return this.lValue;
    }

    @Override
    public String toString(){
        return lValue + " = " + rValue;
    }

}
