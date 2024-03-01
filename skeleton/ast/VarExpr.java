package ast;

public class VarExpr extends Expr {

    final String indent;

    public VarExpr(String identifier, Location loc) {
        super(loc);
        this.indent = identifier;
        
    }

    public String getVariableName() {
        return this.indent;
    }


    @Override
    public String toString() {
        return this.indent;
    }
}
