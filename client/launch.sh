#!/bin/bash

# launch.py is availble in:
# https://github.com/charlesbrandt/moments

# path to launch.py is defined in:
# ~/.bashrc
# example .bashrc is available in moments/editors/ directory

#old way
#export PREFIX="python /c/mindstream/mindstream"
#echo "$PREFIX/launch.py -c $ROOT todo"

export ROOT=/c/bloomington-code/vue

launch.py -c $ROOT todo

echo "Other common options:
launch.py -c $ROOT code

See also:
/c/bloomington-code/vue/instances.txt
(one instance for timetracker there)

see also:
/c/bloomington-code/docker/node-vue/target/fn1 

"

