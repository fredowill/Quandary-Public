package ast;

public class BinaryExpr extends Expr {

    public static final int PLUS = 1;
    public static final int MINUS = 2;
    public static final int TIMES = 3;
    public static final int DOT = 4;

    final Expr expr1;
    final int operator;
    final Expr expr2;
    final Boolean concurrent;

    public BinaryExpr(Boolean isConcurrent, Expr expr1, int operator, Expr expr2, Location loc) {
        super(loc);
        this.expr1 = expr1;
        this.operator = operator;
        this.expr2 = expr2;
        this.concurrent = isConcurrent;
    }

    public Expr getLeftExpr() {
        return expr1;
    }

    public int getOperator() {
        return operator;
    }
    
    public Expr getRightExpr() {
        return expr2;
    }

    public Boolean isConcurrent() {
        return this.concurrent;
    }

    @Override
    public String toString() {
        String s = null;
        switch (operator) {
            case PLUS:  s = "+"; break;
            case MINUS: s = "-"; break;
            case TIMES: s = "*"; break;
            case DOT: s = "."; break;
        }
        return "(" + expr1 + " " + s + " " + expr2 + ")";
    }
}
