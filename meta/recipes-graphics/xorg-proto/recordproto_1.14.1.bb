require xorg-proto-common.inc

SUMMARY = "XRecord: X Record extension headers"

DESCRIPTION = "This package provides the wire protocol for the X Record \
extension.  This extension is used to record and play back event \
sequences."

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=575827a0f554bbed332542976d5f3d40 \
                    file://recordproto.h;endline=19;md5=1cbb0dd45a0b060ff833901620a3e738"

CONFLICTS = "recordext"
PR = "r0"
PE = "1"

SRC_URI[md5sum] = "24541a30b399213def35f48efd926c63"
SRC_URI[sha256sum] = "b27eb043d3e618bc1f8b704a64f8ae218ea00e7f36f9107ac41d41940773098d"
