
# This is the make file to build the Test module of the mybooks.com project
# Defining a makefile variable for the Java compiler
JAVAC = javac

# Defining Java compiler flags
JAVAFLAGS = -g -d $(BIN) -cp $(SRC)

# Defining Java packages
PACKAGES = mybooks/com/asset mybooks/com/shared mybooks/com/client

# Defining path for the asset directory
PACKAGEASSETDEST = ./bin/mybooks/com/asset/

# Directory for compiled binaries
BIN = ./bin/

# Directory of source files
SRC = ./src/

# Make directory command
MKDIR_P = mkdir -p

# Creating a .class file
COMPILE = $(JAVAC) $(JAVAFLAGS)

# Defining an empty variable
EMPTY = 

JAVA_FILES = $(subst $(SRC), $(EMPTY), $(wildcard $(SRC)*.java))

ifdef PACKAGES
PACKAGEDIRS = $(addprefix $(SRC), $(PACKAGES))
PACKAGEFILES = $(subst $(SRC), $(EMPTY), $(foreach DIR, $(PACKAGEDIRS), $(wildcard $(DIR)/*.java)))
ALL_FILES = $(PACKAGEFILES) $(JAVA_FILES)

PACKAGEPROPSRC = $(subst $(SRC), $(SRC), $(foreach DIR, $(PACKAGEDIRS), $(wildcard $(DIR)/*.properties)))
PACKAGEXMLSRC = $(subst $(SRC), $(SRC), $(foreach DIR, $(PACKAGEDIRS), $(wildcard $(DIR)/*.xml)))

else
ALL_FILES = $(wildcard $(SRC).java)
endif

# One of these should be the "main" class listed in Runfile
# CLASS_FILES = $(subst $(SRC), $(BIN), $(ALL_FILES:.java=.class))
CLASS_FILES = $(ALL_FILES:.java=.class)

# The first target is the one that is executed when you invoke
# "make". 

all : $(addprefix $(BIN), $(CLASS_FILES))
	${MKDIR_P} $(PACKAGEASSETDEST)
	cp -f $(PACKAGEPROPSRC) $(PACKAGEASSETDEST)
	cp -f $(PACKAGEXMLSRC) $(PACKAGEASSETDEST)
	chmod 777 *.sh
	
# The line describing the action starts with <TAB>
$(BIN)%.class : $(SRC)%.java
	$(COMPILE) $<	

clean : 		
	rm -rf $(BIN)*