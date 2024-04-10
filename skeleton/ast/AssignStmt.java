package ast;

public class AssignStmt extends Stmt {

    final String ident;
    final Expr rValue;

    public AssignStmt(String ident, Expr expr, Location loc) {
        super(loc);
        this.rValue = expr;
        this.ident = ident;
    }

    public Expr getExpr(){
        return this.rValue;
    }

    public String getIdent(){
        return this.ident;
    }

    @Override
    public String toString(){
        return this.ident + " = " + this.rValue;
    }

}
