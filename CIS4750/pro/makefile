all : Main.class DrawArea.class Unit.class GraphicalUnit.class Const.class Program.class OptionInterface.class Option.class

Main.class : Main.java
	javac Main.java
DrawArea.class : DrawArea.java
	javac DrawArea.java
Unit.class : Unit.java GraphicalUnit.java
	javac Unit.java
GraphicalUnit.class : GraphicalUnit.java
	javac GraphicalUnit.java
Const.class : Const.java
	javac Const.java
Program.class : Program.java
	javac Program.java
OptionInterface.class : OptionInterface.java
	javac OptionInterface.java
Option.class : Option.java
	javac Option.java

run :
	java Program	

clean :
	rm -f *.class
