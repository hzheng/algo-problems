#!/bin/bash

# Test GCJ programs

usage() {
    echo "Usage: `basename $0` [-o] [java_file... [input_file...]]"
    echo "-h print this message"
    echo "-o ouput to STDOUT"
    exit 1
}

run_test() {
    local java_class="${1%.*}"
    if ! test -f $java_class.class; then
        echo Error: class "$java_class.class" not found
        exit 1
    fi

    local input="$2"
    if ! test -f "$input"; then
        echo "Error: cannot access input file: $input"
        exit 1
    fi

    if [[ $stdout -eq 0 ]]; then
        output="${input%.*}".out
        expected="${input%.*}".expected
    fi
    echo -e "\nTesting $java_class on input: $input output: ${output-STDOUT}..."
    java $java_class $input $output
    #java -Dgcj.submit $java_class < $input > $output

    if [[ -z "$expected" ]]; then
        :
    elif ! test -f $expected; then
        echo "Done without comparison"
    elif diff $output $expected &> /dev/null; then
        echo "PASS"
    else
        echo "FAIL: $output does NOT match $expected"
        exit
    fi
}

if [ "$1" == '-h' ]; then
    usage
fi

stdout=0
if [ "$1" == '-o' ]; then
    stdout=1
    shift
fi

time=0
if [ "$1" == '-t' ]; then
    time=1
    shift
fi

java_srcs=()
for i in "$@"; do
    if [[ "${i##*.}" == 'java' ]]; then
        java_srcs+=($i)
        shift
    else
        break
    fi
done
if [ ${#java_srcs[@]} -eq 0 ]; then
    java_srcs=(*.java)
fi

inputs="$*"
if [ -z "$inputs" ]; then
    inputs="*.in"
fi

for java_src in "${java_srcs[@]}"; do
    echo compiling $java_src...
    javac $java_src
    if [[ $? -ne 0 ]]; then
        echo Compile failed
        exit 1
    fi
    echo Done
    for input in $inputs; do
        if [[ $time -eq 0 ]]; then
            run_test $java_src "$input"
        else
            time run_test $java_src "$input"
        fi
    done
done
