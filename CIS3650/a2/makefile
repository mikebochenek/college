parsedec : parsedec.o lex.yy.o
	cc parsedec.o -o parsedec lex.yy.o 

parsedec.o : parsedec.c parsedec.h
	cc -c parsedec.c

lex.yy.o : lex.yy.c y.tab.h
	cc -c lex.yy.c -o lex.yy.o

lex.yy.c : parsedec.lex
	lex parsedec.lex

clean : 
	rm -f lex.yy.c *.o parsedec core

