#include <stdio.h>
//follow is a common line
void swap(char *p/* = NULL */)
{

	printf("this is a function /*is not comments*/");
}
int main(void)
{
	int a = 10;
	int b;
	char *p = NULL;
	swap(p);  //common line
	if (10 == a)
	{
		printf("is not function;//is not comments");
	}
	//pure comments/*no use*/
	/*a = 5;
	printf("not use\n");*/

	b = 3;/*i'm a comment*/ a = 5; /*comments line
								   pure //comments line;"*/
	printf("test \" escape char");
	//and the follow,maybe affect the count of function
	if (*p == '{')
	{
		printf("the '{' is a question\n");
	}
	//
	return 0;
}