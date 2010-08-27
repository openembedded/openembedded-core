DESCRIPTION = "X Video Motion Compensation extension library"

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641deddaa80fe7ca88e944e1fd94a94 \
                    file://src/XvMCWrapper.c;endline=26;md5=5151daa8172a3f1bb0cb0e0ff157d9de"

DEPENDS += "libxext libxv videoproto"

PR = "r0"
PE = "1"

XORG_PN = "libXvMC"
