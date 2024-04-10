package ast;

public class QRefVal extends QVal {
    public QObj objVal;
    boolean isNil;
    boolean mutable;

    public QRefVal(Boolean isNil, QObj value) {
        super(false);
        this.isNil = isNil;
        this.objVal = value;
        this.mutable = false;
    }

    public QRefVal(Boolean isMutable, Boolean isNil, QObj value) {
        super(isMutable);
        this.isNil = isNil;
        this.objVal = value;
        this.mutable = isMutable;
    }

    public boolean isMutable(){
        return this.mutable;
    }    

    public boolean isNil(){
        return this.isNil;
    }  

    public QVal getLeft(){
        return this.objVal.left;
    }

    public QVal getRight(){
        return this.objVal.right;
    }

    @Override
    public String toString() {
        if(objVal == null) {
            return "nil";
        }
        return objVal.toString();
    }

    }
