CC = gcc
CFLAGS = -c -Wall
INCLUDE = CIS245.h
OBJECTS = fgetline.o After_Pattern.o Substitute.o Num_Files.o File_Dir.o Error.o Process_Args.o Verify_Args.o Open_Dir.o Process_Articles.o Find_First.o Get_Date.o Match_ID.o BuildDataBase.o BuildInsertQuery.o BuildInsertQuerySpecial.o

dbnews: dbnews.c CIS245.lib
	gcc dbnews.c CIS245.lib -o dbnews -I /usr/local/include -L /usr/local/lib/mysql/ -lmysqlclient -lmysys -lmystrings -lm -Wall

CIS245.lib : $(OBJECTS)
	ar crs CIS245.lib $(OBJECTS)

$(OBJECTS) : CIS245.h

BuildInsertQuerySpecial.o : BuildInsertQuerySpecial.c
	$(CC) $(CFLAGS) BuildInsertQuerySpecial.c

BuildInsertQuery.o : BuildInsertQuery.c
	$(CC) $(CFLAGS) BuildInsertQuery.c

BuildDataBase.o : BuildDataBase.c
	$(CC) $(CFLAGS) BuildDataBase.c

fgetline.o : fgetline.c 
	$(CC) $(CFLAGS) fgetline.c

After_Pattern.o : After_Pattern.c 
	$(CC) $(CFLAGS) After_Pattern.c

Substitute.o : Substitute.c 
	$(CC) $(CFLAGS) Substitute.c

Num_Files.o : Num_Files.c 
	$(CC) $(CFLAGS) Num_Files.c

File_Dir.o : File_Dir.c 
	$(CC) $(CFLAGS) File_Dir.c

Error.o : Error.c
	$(CC) $(CFLAGS) Error.c

Process_Args.o : Process_Args.c
	$(CC) $(CFLAGS) Process_Args.c

Verify_Args.o : Verify_Args.c
	$(CC) $(CFLAGS) Verify_Args.c

Open_Dir.o : Open_Dir.c
	$(CC) $(CFLAGS) Open_Dir.c

Process_Articles.o : Process_Articles.c
	$(CC) $(CFLAGS) Process_Articles.c

Find_First.o : Find_First.c
	$(CC) $(CFLAGS) Find_First.c

Get_Date.o : Get_Date.c
	$(CC) $(CFLAGS) Get_Date.c

Match_ID.o : Match_ID.c
	$(CC) $(CFLAGS) Match_ID.c
