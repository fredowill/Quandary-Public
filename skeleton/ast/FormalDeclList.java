package ast;

public class FormalDeclList extends ASTNode {

    final VarDecl var;
    final FormalDeclList formaldeclList;

    public FormalDeclList(VarDecl v, FormalDeclList fdl, Location loc) {
        super(loc);
        this.var = v;
        this.formaldeclList = fdl;
    }

    public VarDecl getVarDecl() {
        return this.var;
    }

    public FormalDeclList getFuncParams() {
        return this.formaldeclList;
    }
    

}
