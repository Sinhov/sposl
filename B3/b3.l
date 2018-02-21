%{
/*** Definition section ***/
#include <stdio.h>
int chars=0,words=0,lines=0;
%}

digit [0-9]
character [a-zA-z]
word ({character}|{digit})+

/*** Rules section ***/
%%
\n lines++;
{word} {words++;chars+=yyleng;};
. {chars++;}
%%

/*** C Code section ***/

int yywrap(){
	return 1;
}
int main(void){
    yylex();
	printf("No of characters: %d\n",chars);
	printf("No of words: %d\n",words);
	printf("No of lines: %d\n",lines);
	return 0;
}

/*Steps to run
lex b3.l
gcc lex.yy.c
./a.out
*/
