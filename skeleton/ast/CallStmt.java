package ast;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CallStmt extends Stmt {

    final String ident;
    final ExprList exprList;
    private static final Lock lock = new ReentrantLock();


    public CallStmt(String id, ExprList el, Location loc) {
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

    public static QVal left(QRefVal ref){
        return ref.getLeft();
    }

    public static QVal right(QRefVal ref){
        return ref.getRight();
    }
    public static QIntVal isNil(QVal x){
        if(( x instanceof QRefVal && ((QRefVal)x).isNil() == true)){
            return new QIntVal(1);
        } 
        return new QIntVal(0);
    }

    public static QIntVal isAtom(QVal x){
        if(x instanceof QIntVal){
            return new QIntVal(1);
        } else if(x instanceof QRefVal && ((QRefVal)x).isNil() == true){
            return new QIntVal(1);
        }
        return new QIntVal(0);
    }

    public static QIntVal setRight(QRefVal r, QVal value) {
        r.objVal.right = value;
        return new QIntVal(1);
    }

    public static QIntVal setLeft(QRefVal r, QVal value) {
        r.objVal.left = value;
        return new QIntVal(1);
    }
    public static QIntVal acq(QRefVal r) {
        r.lock.lock();
        return new QIntVal(1);
    }
    public static QIntVal rel(QRefVal r) {
        r.lock.unlock();
        return new QIntVal(1);
    }
}
