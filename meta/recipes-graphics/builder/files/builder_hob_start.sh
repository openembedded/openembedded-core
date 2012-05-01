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

#uncomment the settings for BB_NUMBER_THREADS and PARALLEL_MAKE
sed -i 's/^#BB_NUMBER_THREADS =/BB_NUMBER_THREADS =/g' conf/local.conf
sed -i 's/^#PARALLEL_MAKE =/PARALLEL_MAKE =/g' conf/local.conf

hob &

matchbox-terminal&

/etc/mini_x/please_wait_dialog.py &
