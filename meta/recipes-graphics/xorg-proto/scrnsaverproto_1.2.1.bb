require xorg-proto-common.inc

SUMMARY = "Xscrnsaver: X Screen Saver extension headers"

DESCRIPTION = "This package provides the wire protocol for the X Screen \
Saver extension.  This extension allows an external \"screen saver\" \
client to detect when the alternative image is to be displayed and to \
provide the graphics."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=eed49b78b15b436c933b6b8b054e3901 \
                    file://saverproto.h;endline=26;md5=a84c0637305159f3c0ab173aaeede48d"

PR = "r0"
PE = "1"

EXTRA_OECONF_append = " --enable-specs=no"

SRC_URI[md5sum] = "6af0f2e3369f5f74e69345e214f5fd0d"
SRC_URI[sha256sum] = "fad2f73cac136fcddd311d27a14ac7e519a9bec6ab272490d9c9b363556024f1"

