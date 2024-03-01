package ast;

import java.io.PrintStream;

public class Program extends ASTNode {

    final FuncDef main;

    public Program(FuncDef main, Location loc) {
        super(loc);
        this.main = main;
    }

    public FuncDef getFunc() {
        return this.main;
    }

    public void println(PrintStream ps) {
        ps.println(this.main);
    }
}
