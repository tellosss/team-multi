#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "utils.h"

int main(int argc, char** argv) {
   const char* foo = "wow"; 
   foo = "top"; 
   foo[0] = 1; 
   return 0;
}