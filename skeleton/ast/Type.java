package ast;

public class Type extends ASTNode {

    public static final int INT = 1;
    public static final int Q = 2;
    public static final int REF = 3;

    public final int varType;

    public Type(int type, Location loc) {
        super(loc);
        this.varType = type;
    }

    public int getVarType() {
        return this.varType;
    }

    @Override
    public String toString(){
        String s = null;
        switch (varType) {
            case INT:  s = "int"; break;
            case Q: s = "Q"; break;
            case REF: s = "Ref"; break;
        }
        return s;
    }

}
