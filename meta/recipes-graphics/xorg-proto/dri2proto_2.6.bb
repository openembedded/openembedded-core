require xorg-proto-common.inc

SUMMARY = "DRI2: Direct Rendering Infrastructure 2 headers"

DESCRIPTION = "This package provides the wire protocol for the Direct \
Rendering Ifnrastructure 2.  DIR is required for may hardware \
accelerated OpenGL drivers."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=2e396fa91834f8786032cad2da5638f3 \
                    file://dri2proto.h;endline=31;md5=22f28bf68d01b533f26195e94b3ed8ca"

PR = "r0"

SRC_URI[md5sum] = "2eb74959684f47c862081099059a11ab"
SRC_URI[sha256sum] = "ad82c0b28c19fcd3f91ea1f93956cb666526b41b91f239773b5854b9b1a3b909"
