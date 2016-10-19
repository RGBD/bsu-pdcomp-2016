CC=g++
CFLAGS=-O2 -Wall -Wextra -std=c++11 -fopenmp
LD=g++
LDFLAGS=

SOURCES=$(wildcard *.cpp)
EXECUTABLES=$(SOURCES:.cpp=.bin)

.PHONY: all depend run debug test clean

default:
	echo 'not implemented'

all: $(SOURCES) $(EXECUTABLES)

%.bin: %.cpp
	$(CC) $(CFLAGS) $(LDFLAGS) $^ -o $@

clean:
	rm -f *.bin
