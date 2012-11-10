all : p1 p2 

p1 : a1.c a1.h common.h other.o insert.o delete.o search.o fileio.o search_on_disk.o
	gcc -Wall other.o insert.o delete.o search.o fileio.o search_on_disk.o a1.c -o p1

p2 : p2.c a1.h common.h other.o insert.o delete.o search.o fileio.o search_on_disk.o
	gcc -Wall other.o insert.o delete.o search.o fileio.o search_on_disk.o p2.c -o p2

other.o : other.c other.h insert.h a1.h common.h
	gcc -Wall other.c -c

insert.o : insert.c other.h insert.h a1.h common.h
	gcc -Wall insert.c -c

delete.o : delete.c other.h delete.h a1.h common.h
	gcc -Wall delete.c -c

search.o : search.c other.h search.h a1.h common.h
	gcc -Wall search.c -c

search_on_disk.o : search_on_disk.c other.c search_on_disk.h a1.h common.h
	gcc -Wall search_on_disk.c -c

fileio.o : fileio.c other.h fileio.h a1.h common.h
	gcc -Wall fileio.c -c

makeandrun :
	./p1

clean :
	rm -rf core tree.dat *.o p1 p2
