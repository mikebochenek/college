all : Main.class DataDictionary.class TableDef.class SelectStructure.class Predicate.class PlanGenerator.class

Main.class : Main.java
	javac Main.java

DataDictionary.class : DataDictionary.java
	javac DataDictionary.java

TableDef.class : TableDef.java
	javac TableDef.java

SelectStructure.class : SelectStructure.java
	javac SelectStructure.java

Predicate.class : Predicate.java
	javac Predicate.java

PlanGenerator.class : PlanGenerator.java
	javac PlanGenerator.java

clean :
	rm -f *.class core

run :
	java Main

