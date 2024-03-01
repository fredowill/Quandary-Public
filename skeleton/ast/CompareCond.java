package ast;

public class CompareCond extends Cond {

    public static final int LT = 1;
    public static final int GT = 2;
    public static final int LE = 3;
    public static final int GE = 4;
    public static final int EQ = 5;
    public static final int NE = 6;
    final Expr e1;
    final Expr e2;
    final int comp; 

    public CompareCond(Expr expr1, int comparator, Expr expr2, Location loc) {
        super(loc);
        this.e1 = expr1;
        this.e2 = expr2;
        this.comp = comparator;
    }

    public Expr getLeftExpr(){
        return this.e1;
    } 
    public Expr getRightExpr(){
        return this.e2;
    } 

    public int getComparator(){
        return this.comp;
    }

    @Override
    public String toString(){
        String s = null;
        switch (this.comp) {
            case LT:  s = "<"; break;
            case GT: s = ">"; break;
            case LE: s = "<="; break;
            case GE: s = ">="; break;
            case EQ: s = "=="; break;
            case NE: s = "!="; break;
        }
        return e1 + " " + s + " " + e2;
    }

}
