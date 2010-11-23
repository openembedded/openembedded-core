require xorg-lib-common.inc

DESCRIPTION = "Xprint job utility client library"
DEPENDS += "libxp libxt libxprintutil"
PR = "r1"
LIC_FILES_CHKSUM = "file://src/xpapputil.c;beginline=2;endline=27;md5=be811cca90200613a672dc96206a767b"
PE = "1"

XORG_PN = "libXprintAppUtil"
