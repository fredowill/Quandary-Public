package ast;

public class CallExpr extends Expr {

    final String ident;
    final ExprList exprList;

    public CallExpr(String id, ExprList el, Location loc) {
        super(loc);
        this.ident = id;
        this.exprList = el;
    }

    public String getIdent() {
        return this.ident;
    }
    
    public ExprList getExprList() {
        return this.exprList;
    }

    public static long randomInt(long n) {
        return (long) (n*Math.random());
    }

}
