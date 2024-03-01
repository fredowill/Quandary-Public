package ast;

public class StmtList extends Stmt {

    final Stmt s;
    final StmtList sl;

    public StmtList(Stmt s, StmtList sl, Location loc) {
        super(loc);
        this.s = s;
        this.sl = sl;
    }

    public StmtList getStmtList() {
        return sl;
    }

    public Stmt getStmt() {
        return s;
    }

    @Override
    public String toString(){
        String ret = s + " "+ sl;
        return ret;
    }

}
