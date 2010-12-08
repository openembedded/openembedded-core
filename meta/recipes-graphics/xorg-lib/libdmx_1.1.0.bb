require xorg-lib-common.inc

DESCRIPTION = "X11 Distributed Multihead extension library"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641deddaa80fe7ca88e944e1fd94a94 \
                    file://src/dmx.c;endline=33;md5=79ff7aacf716dafea0d490316f998a11"

DEPENDS += "libxext dmxproto"
PR = "r1"
PE = "1"

SRC_URI[md5sum] = "a2fcf0382837888d3781b714489a8999"
SRC_URI[sha256sum] = "1904a8f848cc5d76105cb07707890aca095540a37fb0a63d359f71da51d3e2d5"
