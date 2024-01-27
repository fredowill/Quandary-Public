Q main (int x){
    return sumTotals (makeList(x));
}
    Ref sumTotals (Ref list){
        if (isNil(list) == 1)return nil; 
        return sum ((int) left(list)) . sumTotals((Ref)right(list));
    }

    Ref doubles (Ref list) {
        if (isNil(list) == 1)return nil; 
        return double ((int) left(list)) . doubles((Ref)right(list));        
    }

int double(int i ){
    return i *2;
}

    Ref makeList (int x){
        if (x==0) return nil;
        return x . makeList(x-1);
    }