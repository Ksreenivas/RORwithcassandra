#!/bin/sh

rails g cequel:configuration
rails cequel:keyspace:create
rails cequel:migrate
rails s