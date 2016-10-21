#!/usr/bin/env python3

from math import sqrt
from random import random, randrange, shuffle
from sys import argv, stderr

maps = list({ 
    'uniform': lambda x: x,
    'square': lambda x: x ** 2,
    'root': lambda x: sqrt(x),
    'archup': lambda x: sqrt(0.5 ** 2 - (x - 0.5) ** 2),
    'archdown': lambda x: 1 - sqrt(0.5 ** 2 - (x - 0.5) ** 2)
}.items())

shuffle(maps)

if __name__ == '__main__':
    if len(argv) != 3:
        print('usage: %s <n_stats> <n_records>' % argv[0], file=stderr)
        exit(1)

    n_stats = int(argv[1])
    add_id_suffix = n_stats > len(maps)
    n_records = int(argv[2])

    for i in range(n_records):
        map_id = randrange(n_stats)
        key = maps[map_id % len(maps)][0] + (
            str(map_id) if add_id_suffix else '')
        value = maps[map_id % len(maps)][1](random())
        print(key + '\t' + '%.7f' % value)
