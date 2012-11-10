#include <stdio.h>
#include <conio.h>
#include <dos.h>
#include "view.h"
#include "llong.h"
#include "sshort.h"
main() {
   int X,Y;
   printf ("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
   puttext (1,1,80,25,VIEW);
   Y = 10;
   for (X=10; X<70; X=X+1) {
	gotoxy(X,Y);
	puttext (X,10,X,10,LLONG);
	delay(50);
	gotoxy(X,Y);
	puttext (X,10,X,10,SSHORT);
	delay(50);
	}
   for (X=10; X<70; X=X+1) {
	gotoxy(X,Y);
	puttext (X,12,X,12,LLONG);
	delay(100);
	gotoxy(X,Y);
	puttext (X,12,X,12,SSHORT);
	delay(100);
	}
   for (X=10; X<70; X=X+1) {
	gotoxy(X,Y);
	puttext (X,14,X,14,LLONG);
	delay(250);
	gotoxy(X,Y);
	puttext (X,14,X,14,SSHORT);
	delay(250);
	}
   for (X=10; X<70; X=X+1) {
	gotoxy(X,Y);
	puttext (X,16,X,16,LLONG);
	delay(10);
	gotoxy(X,Y);
	puttext (X,16,X,16,SSHORT);
	delay(10);
	}
   for (X=10; X<70; X=X+1) {
	gotoxy(X,Y);
	puttext (X,18,X,18,LLONG);
	delay(100);
	gotoxy(X,Y);
	puttext (X,18,X,18,SSHORT);
	delay(100);
	}
   printf ("\n\n\n\n\n\n\n\n\n\n\n\n\n");
return(0);
}
