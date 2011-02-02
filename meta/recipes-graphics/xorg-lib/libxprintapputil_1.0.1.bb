require xorg-lib-common.inc

SUMMARY = "Xprint: Xprint job utility library"

DESCRIPTION = "libxprintapputil provides utility Xpau APIs allowing \
client applications to access information about and control Xprint jobs \
from an Xprint server."

DEPENDS += "libxp libxt libxprintutil"
PR = "r1"
LIC_FILES_CHKSUM = "file://src/xpapputil.c;beginline=2;endline=27;md5=be811cca90200613a672dc96206a767b"
PE = "1"

XORG_PN = "libXprintAppUtil"

SRC_URI[md5sum] = "d2de510570aa6714681109b2ba178365"
SRC_URI[sha256sum] = "24606446003379dbf499ef57e9294ce622c0f7f8a8f10834db61dc59ef690aa5"
