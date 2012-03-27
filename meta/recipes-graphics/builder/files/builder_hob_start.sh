#!/bin/sh
#This script will be called via mini X session on behalf of file owner, after
#installed in /etc/mini_x/session.d/. Any auto start jobs including X apps can
#be put here

# start hob here
export PSEUDO_PREFIX=/usr
export PSEUDO_LOCALSTATEDIR=/home/builder/pseudo
export PSEUDO_LIBDIR=/usr/lib/pseudo/lib64

cd /home/builder/poky
. ./oe-init-build-env
hob &

matchbox-terminal&
