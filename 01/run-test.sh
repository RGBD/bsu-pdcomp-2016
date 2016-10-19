#!/bin/zsh

run_test() {
  log_file=$1
  shift
  n_tests=$1
  shift
  for i in $(seq 1 $n_tests); do
    {
      echo -n $@ "| " 1>&2
      /usr/bin/time -f '%e %S %U' $@
    } 2>> $log_file
  done
}

make all

rm -f naive.txt
for n in $(cat sizes.txt); do
  echo $n
  run_test naive.txt 3 ./main-ns.bin $n
done

rm -f parallel.txt
for n in $(cat sizes.txt); do
  echo $n
  run_test parallel.txt 3 ./main-np-i.bin $n
done

rm -f blocky.txt
for n in $(cat block-sizes.txt); do
  echo $n
  run_test blocky.txt 3 ./main-bs.bin 1995 $n
done

rm -f block-optimal.txt
for n in $(cat sizes.txt); do
  echo $n
  run_test block-optimal.txt 3 ./main-bs.bin $n 50
done

rm -f fast.txt
for n in $(cat sizes.txt); do
  echo $n
  run_test fast.txt 3 ./main-bp-ii.bin $n 50
done
