/* Program with multiple function definitions */
int main (int arg){
    int x = randomNegInt(); /* empty exprList */
    return x;
}

/* Function with no params */
int randomNegInt(){ 
    return randomInt(100) * -1;
}
