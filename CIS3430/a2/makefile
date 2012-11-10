JC = javac

all : ProductManager.class CustomerManager.class RemoteProductManager.class RemoteCustomerManager.class Server.class ClientGUI.class

RemoteProductManager.class : RemoteProductManager.java BasicComparator.java RemoteDatabaseInterface.java
	$(JC) $?

RemoteCustomerManager.class : RemoteCustomerManager.java BasicComparator.java RemoteDatabaseInterface.java
	$(JC) $?

ProductManager.class : BasicComparator.java DatabaseInterface.java ProductManager.java
	$(JC) $?

CustomerManager.class : BasicComparator.java DatabaseInterface.java CustomerManager.java
	$(JC) $?

Server.class : Server.java ServerThread.java PasswordInterface.java EmailComparator.java
	$(JC) $?

ClientGUI.class : ClientGUI.java
	$(JC) $?

BasicComparator.class : BasicComparator.java
	$(JC) $?

DatabaseInterface.class : DatabaseInterface.java
	$(JC) $?

ServerThread.class : ServerThread.java
	$(JC) $?

PasswordInterface.class : PasswordInterface.java
	$(JC) $?

EmailComparator.class : EmailComparator.java
	$(JC) $?

RemoteDatabaseInterface.class : RemoteDatabaseInterface.java
	$(JC) $?

clean :
	rm *.class -f
