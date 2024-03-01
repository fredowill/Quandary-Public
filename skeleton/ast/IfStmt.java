package ast;

public class IfStmt extends Stmt {

    final Cond c;
    final Stmt s;

    public IfStmt(Cond condition, Stmt statement, Location loc) {
        super(loc);
        this.c = condition;
        this.s = statement;
    }

    public Cond getCond(){
        return this.c;
    }

    public Stmt getStmt(){
        return this.s;
    }

    @Override
    public String toString(){
        return "if (" + this.c + ") { \n\r\t" + this.s + "\n\r\t}";
    }

}
