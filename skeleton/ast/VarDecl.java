package ast;

public class VarDecl extends ASTNode {

    final Type type;
    final String id;

    public VarDecl(Type varType, String Ident, Location loc) {
        super(loc);
        this.type = varType;
        this.id = Ident;
    }

    public Type getVarType(){
        return this.type;
    }

    public String getVarName(){
        return this.id + "";
    }
    
    @Override
    public String toString(){
        return type.toString() + " " + id;
    }

}
