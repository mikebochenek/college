# Michael Bochenek
# mboche01@uoguelph.ca
# 0041056
# CIS*4400 Distributed Information Systems Architectures
# Assignment 2:  Talking to the magic ORB!


all : BasicComparator.class NeuralNetDataManager NeuralNetDataImpl.class Server.class Client1.class Client2.class

NeuralNetDataManager : NeuralNetData.idl 
	idl2java NeuralNetData.idl

BasicComparator.class : BasicComparator.java
	javac BasicComparator.java

NeuralNetDataImpl.class : NeuralNetDataImpl.java
	vbjc NeuralNetDataImpl.java

Server.class : Server.java
	vbjc Server.java

Client1.class : Client1.java
	vbjc Client1.java

Client2.class : Client2.java
	vbjc Client2.java

clean :
	rm -rf NeuralNetDataManager *.class
