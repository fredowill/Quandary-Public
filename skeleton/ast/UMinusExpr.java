package ast;

public class UMinusExpr extends Expr {

    final Expr expr;

    public UMinusExpr(Expr expr, Location loc) {
        super(loc);
        this.expr = expr;
        
    }

    public Expr getUMinusExpr() {
        return expr;
    }


    @Override
    public String toString() {
        return "- " + expr;
    }
}
