require xorg-lib-common.inc

SUMMARY = "Xpm: X Pixmap extension library"

DESCRIPTION = "libXpm provides support and common operation for the XPM \
pixmap format, which is commonly used in legacy X applications.  XPM is \
an extension of the monochrome XBM bitmap specificied in the X \
protocol."

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=3e07763d16963c3af12db271a31abaa5"
DEPENDS += "libxext libsm libxt"
PR = "r3"
PE = "1"

XORG_PN = "libXpm"

PACKAGES =+ "sxpm cxpm"
FILES_cxpm = "${bindir}/cxpm"
FILES_sxpm = "${bindir}/sxpm"

SRC_URI[md5sum] = "2de3a1b9541f4b3a6f9d84b69d25530e"
SRC_URI[sha256sum] = "8add01029cab0598ca86e01a2f7781a636f74b757abe6b50ba61b2a6e2fd621e"

BBCLASSEXTEND = "native"
