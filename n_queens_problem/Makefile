CC = gcc
CFLAGS = -W -Wextra -Wall -Werror -Wunreachable-code
EXEC = n_queens_problem.exe

#Header's directory
HEAD_D = lib
HEADERS = $(wildcard $(HEAD_D)/*.h)

#Source's directory
SRC_D = src
SOURCES = $(wildcard $(SRC_D)/*.c)

MODULES = $(basename  $(notdir $(SOURCES))) 

#Object's directory
OBJ_D = obj
OBJECTS = $(addprefix $(OBJ_D)/, $(addsuffix .o,$(MODULES)))


all : $(EXEC)

$(EXEC): $(OBJECTS)
	@echo ' === LINKING [ $@ ] ==='
	$(CC) $^ -o $@

$(OBJ_D)/%.o: $(SRC_D)/%.c
	@echo ' === COMPILING [ $@ ] ==='
	$(CC) -I $(HEAD_D) -c $^ $(CFLAGS) -o $@ 
#-I to specifie the directory of the headers.

clean ::
	@echo ' ===> CLEANING <==='
	rm -f $(OBJ_D)/*.o

mrproper: clean
	@echo ' ===> rm executable <==='
	rm -f $(EXEC)
