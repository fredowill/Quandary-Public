mutable Q main(int arg) {
    if (arg == 1) {
        /* Case 1: Allocate memory until out of memory */
        return case_1();
    } else if (arg == 2) {
        /*  Case 2: Reference counting */
        mutable int n = 15;
        while(n > 1){
            case_2();
            n = n -1;
        }
    } else if (arg == 3) {
        /* Case 3: Mark-sweep  */
        return case_3(20);
    } else if (arg == 4) {
        /* Case 4: Explicit memory management */
        mutable int n = 15;
        return case_4(n);
    }
    /*  Dummy return */
    return 0;
}

mutable int case_1() {
    int heap_size = 15;
    int allocated_memory = 0;
    Ref list = generate_list(15);
    return 1;
}

mutable int case_2() {
    mutable Ref x = 3.5;
    mutable Ref y = 5.x;
    mutable Ref z = 9.y;
    setRight(y,z);
    return 1;
}

mutable int case_3(mutable int n) {
    Ref r = (nil . nil);
    mutable Ref temp = r;
    while (n > 0) {
        setRight(temp, (nil . nil));
        Ref prev = temp;
        temp = (Ref)right(temp);
        free(prev);

        n = n - 1;
    }
    return 1;
 
}

mutable int case_4(mutable int n) {
    while(n > 1){
    mutable Ref x = 3.5;
    mutable Ref y = 5.x;
    mutable Ref z = 9.y;
    setRight(y,z);
    n = n -1;
    }

    return 1;
}

Ref generate_list(int length) {
    if (length < 0) {
        return length.nil;
    }
    return length . generate_list(length - 1);
}