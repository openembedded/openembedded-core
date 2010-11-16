require xorg-proto-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bd0f26ecf86d0f24922125195d5b70a \
                    file://xfixesproto.h;endline=43;md5=27614675897bb1cdc611ba7de506cddc"

CONFLICTS = "fixesext"
PR = "r0"
PE = "1"

BBCLASSEXTEND = "nativesdk"
