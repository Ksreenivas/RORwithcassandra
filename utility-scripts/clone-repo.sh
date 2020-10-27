#!/bin/bash

if [ "${DEBUG_MODE}" = false ] ; then
    set +x
fi

## Clone repo 
git clone https://github.com/Ksreenivas/RORwithcassandra.git
pwd
cd RORwithcassandra/cassandra-example-using-rails-master
