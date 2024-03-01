Q main(int arg) {
    return makeStuff(arg);
}

Ref makeStuff (int count){
    if(count == 0) return nil;
    Ref tiny = makeStuff(count - 1);
    return  tiny . tiny;
}

/* the funciton returns 1 iff x equals y */
int equals (Q x, Q y){
    /* fixme line is there to calm the compiler */
    if(isAtom(x) == 1){
        if(isAtom(y) == 0) return 0;
        if(isNil(x) == 1) return isNil(y);
        if(isNil(y) == 0 ){
            if((int)x == (int)y) return 1;
        }
        return 0;
    }
    if(isAtom(y) == 1) return 0;
    Q xleft = left((Ref)x);
    Q xright = right((Ref)x);
    if( equals(xleft, left(y)) == 1 && equals(xright, right(y)) == 1) return 1;
    return 0 
}

Ref makeListBetter(int pos, int length){
    if(pos > length) return nil;
    return pos . makeListBetter(pos+1, length)
}