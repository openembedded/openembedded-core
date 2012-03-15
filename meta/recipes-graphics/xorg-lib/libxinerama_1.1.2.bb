require xorg-lib-common.inc

SUMMARY = "Xinerama: Xinerama extension library"

DESCRIPTION = "Xinerama is a simple library designed to interface the \
Xinerama Extension for retrieving information about physical output \
devices which may be combined into a single logical X screen."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=6f4f634d1643a2e638bba3fcd19c2536 \
                    file://src/Xinerama.c;beginline=2;endline=25;md5=fcef273bfb66339256411dd06ea79c02"

DEPENDS += "libxext xineramaproto"
PROVIDES = "xinerama"
PR = "r0"
PE = "1"

XORG_PN = "libXinerama"

SRC_URI[md5sum] = "cb45d6672c93a608f003b6404f1dd462"
SRC_URI[sha256sum] = "a4e77c2fd88372e4ae365f3ca0434a23613da96c5b359b1a64bf43614ec06aac"
