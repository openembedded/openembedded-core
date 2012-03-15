require xorg-lib-common.inc

SUMMARY = "DMX: Distributed Multihead X extension library"

DESCRIPTION = "The DMX extension provides support for communication with \
and control of Xdmx(1) server. Attributes of the Xdmx(1) server and of \
the back-end screens attached to the server can be queried and modified \
via this protocol."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=a3c3499231a8035efd0e004cfbd3b72a \
                    file://src/dmx.c;endline=33;md5=c43f19af03c7c8619cadc9724ed9afe1"

DEPENDS += "libxext dmxproto"

PR = "r0"
PE = "1"

SRC_URI[md5sum] = "782ced3a9e754dfeb53a8a006a75eb1a"
SRC_URI[sha256sum] = "a7870b648a8768d65432af76dd11581ff69f3955118540d5967eb1eef43838ba"

