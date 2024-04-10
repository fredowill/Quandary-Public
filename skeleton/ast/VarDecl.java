package ast;

public class VarDecl extends ASTNode {

    final Type type;
    final String id;
    public final Boolean isMutable;

    public VarDecl(Boolean isMutable, Type varType, String Ident, Location loc) {
        super(loc);
        this.type = varType;
        this.id = Ident;
        this.isMutable = isMutable;
    }

    public Type getVarType(){
        return this.type;
    }

    public Boolean isMutable(){
        return this.isMutable;
    }

    public String getVarName(){
        return this.id + "";
    }
    
    @Override
    public String toString(){
        return type.toString() + " " + id;
    }

}
