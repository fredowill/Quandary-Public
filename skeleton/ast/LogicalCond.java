package ast;

public class LogicalCond extends Cond {

    public static final int AND = 1;
    public static final int OR = 2;
    public static final int NOT = 3;
    final Cond c1;
    final Cond c2;
    final int comp; 

    public LogicalCond(Cond cond1, int comparator, Cond cond2, Location loc) {
        super(loc);
        this.c1 = cond1;
        this.c2 = cond2;
        this.comp = comparator;
    }

    public Cond getCond1() {
        return this.c1;
    }

    public Cond getCond2() {
        return this.c2;
    }

    public int getComparator(){
        return this.comp;
    }

    @Override
    public String toString(){
        String s = null;
        switch (this.comp) {
            case AND:  s = "&&"; break;
            case OR: s = "||"; break;
            case NOT: s = "!"; break;
        }
        return c1 + " " + s + " " + c2;
    }

}
