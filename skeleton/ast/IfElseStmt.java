package ast;

public class IfElseStmt extends Stmt {

    final Cond c;
    final Stmt s1;
    final Stmt s2;

    public IfElseStmt(Cond condition, Stmt stmt1, Stmt stmt2, Location loc) {
        super(loc);
        this.c = condition;
        this.s1 = stmt1;
        this.s2 = stmt2;
    }

    public Cond getCond(){
        return this.c;
    }

    public Stmt getStmt1(){
        return this.s1;
    }

    public Stmt getStmt2(){
        return this.s2;
    }

    @Override
    public String toString(){
        return "if (" + this.c + "){ \n\r\t" + this.s1 + "\n\r\t} else{ " + "\n\r\t" + this.s2 + "\n\r\t}"; 
    }

}
