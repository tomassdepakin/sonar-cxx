void func1() {
   return;
}

int func2(int a) {
   if( a ) {
      return 1;
   } else {
      return 0;
   }
}

int func3(int a) {
   if( a ) {        // +1
      if ( b ) {    // +2 nesting=1
        return 1;
      }
   } else {
      return 0;
   }
}

string getWords(int number) {
    switch (number) { // +1
    case 1:
        return "one";
        break;
    case 2:
        return "a couple";
        break;
    case 3:
        return "a few";
        break;
    default:
        return "lots";
        break;
    }
}