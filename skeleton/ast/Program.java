package ast;

import java.io.PrintStream;

public class Program extends ASTNode {

    final FuncDefList funcdeflist;

    public Program(FuncDefList fdl, Location loc) {
        super(loc);
        this.funcdeflist = fdl;
    }

    public FuncDef getFunc() {
        return null;
    }
    public FuncDefList getFuncDefList() {
        return this.funcdeflist;
    }

    public void println(PrintStream ps) {
        ps.println(this.funcdeflist);
    }
}
