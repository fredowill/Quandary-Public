Q main(int arg) {
    Ref list1 =(3 . (4 . nil));
    Ref list2 = ((56 . (5 . nil)) . nil) . (26 . (2 . ((8 . nil). nil)));
    Ref results = append(list1,list2);
    Ref results2 = reverse(list1);
    int results3 = isSorted(((3 . (5 . (5 . nil))) . ((2 . (8 . nil)) . ((6 . (7 . (4 . nil))) . ((2. (3 . (56 . (92 . nil))) . nil))))));

    return results2; 
}



int isList(Q list) {
    if (isAtom(list) == 1) return isNil(list);
    return isList(right((Ref)list)); 

}

Ref append(Ref list1, Ref list2){
    if( isNil(list1) == 1) return list2;
    return left(list1) . append((Ref)right(list1), list2);
}

/*Ref appendHelper(Ref list1, Ref list2){
    if(isAtom(list1) == 1 && isNil(list1) == 0) return appendInt(list1, list2);
    return append(list1, list2);

} */

Ref appendInt(Ref list1, int num){
    if( isNil(list1) == 1) return num . nil;
    return left(list1) . appendInt((Ref)right(list1), num);
}

Ref reverse(Ref list){
    if(isNil(list) == 1 || isNil(right(list)) == 1 ) return list;
    if(isAtom(left(list)) == 1 && isNil(left(list)) == 0) return appendInt(reverse((Ref)right(list)), (int)left(list));
    return append(reverse((Ref)right(list)), (Ref)left(list) . nil);
}

int listLength(Q list){
    if(isNil(list) == 1) return 1;
    return listLength(right((Ref)list)) + 1;
}

int isSorted(Ref list){
    if(isNil(list) == 1) return 1;
    int currlistlength = listLength(left(list));
    if(isNil(right(list)) == 1 || currlist <= isSorted((Ref)right(list))) return currlistlength;
    return 0;
}