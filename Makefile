JAVAC = javac
JAVA = java
SRC = src
BIN = bin
LIB = lib/gson-2.10.1.jar

SOURCES = $(wildcard $(SRC)/*.java)
CLASSPATH = $(BIN):$(LIB)

all: build run

build:
	mkdir -p $(BIN)
	$(JAVAC) -cp $(LIB) -d $(BIN) $(SOURCES)

run:
	$(JAVA) -cp $(CLASSPATH) Simulation

clean:
	rm -rf $(BIN)/*
