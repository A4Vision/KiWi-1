#!/bin/bash

# Experiment 0
echo "Experiments Results" > results.txt
# itemsRange=2000000 threadsAmount=* preFillAmount=1000000 mapNumber=* experimentIndex=0 experimentsCount=5 testDurationSeconds=3.00 warmUpSeconds=1.00

for experimentIndex in 0 1
do
    echo "experiment $experimentIndex"
    echo "==========================="
    for threadsNumber in 1 2 4 8 16 32
    do
        for mapNumber in 0 1 2 3
        do
            java -ea benchmarks.Main 2000000 $threadsNumber 1000000 $mapNumber $experimentIndex 5 5 3 >> results.txt
        done
    done
done

for experimentIndex in 2 3
do
    echo "experiment $experimentIndex"
    echo "==========================="
    for threadsNumber in 1 2 4 8 16 32
    do
        for mapNumber in 0 1
        do
            java -ea benchmarks.Main 1000000 $threadsNumber 500000 $mapNumber $experimentIndex 5 5 3 >> results.txt
        done
    done
done
