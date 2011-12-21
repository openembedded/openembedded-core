#!/bin/sh
#This script will be called via mini X session on behalf of file owner, after
#installed in /etc/mini_x/session.d/. Any auto start jobs including X apps can
#be put here

# start hob here
#cd /intel/poky/poky
#. ./oe-init-build-env
#../scripts/hob

matchbox-terminal&
