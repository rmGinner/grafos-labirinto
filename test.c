#include <stdio.h>
#include <stdlib.h>

void main(){

	char c;
	scanf("%c",&c);

	printf("Op. com '8': %d \n\n",(c & 8) == 8);
	printf("Op. com '4': %d \n\n",(c & 4) == 4);
	printf("Op. com '2': %d \n\n",(c & 2) == 2);
	printf("Op. com '1': %d \n\n",(c & 1) == 1);
}
