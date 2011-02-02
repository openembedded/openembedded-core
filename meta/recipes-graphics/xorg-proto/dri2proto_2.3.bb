require xorg-proto-common.inc

SUMMARY = "DRI2: Direct Rendering Infrastructure 2 headers"

DESCRIPTION = "This package provides the wire protocol for the Direct \
Rendering Ifnrastructure 2.  DIR is required for may hardware \
accelerated OpenGL drivers."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=2e396fa91834f8786032cad2da5638f3 \
                    file://dri2proto.h;endline=31;md5=22f28bf68d01b533f26195e94b3ed8ca"

PR = "r0"

SRC_URI[md5sum] = "3407b494d5e90d584c9af52aa8f9f028"
SRC_URI[sha256sum] = "b2141892a0db35feffa5e952ff5e1d2727c4436b07d7e2e9dd2ed89c8bb3e677"
