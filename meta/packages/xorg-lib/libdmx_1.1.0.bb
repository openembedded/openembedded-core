require xorg-lib-common.inc

DESCRIPTION = "X11 Distributed Multihead extension library"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641deddaa80fe7ca88e944e1fd94a94 \
                    file://src/dmx.c;endline=33;md5=79ff7aacf716dafea0d490316f998a11"

DEPENDS += "libxext dmxproto"
PR = "r1"
PE = "1"
