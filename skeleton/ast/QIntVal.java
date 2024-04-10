package ast;

public class QIntVal extends QVal {
    public long value;

    public QIntVal(long value) {
        super(false);
        this.value = value;
    }

    public QIntVal(Boolean mutable, long value) {
        super(mutable);
        this.value = value;
    }

    public long getInt(){
        return this.value;
    }

    @Override
    public String toString() {
        return Long.toString(this.value);
    }

    }
