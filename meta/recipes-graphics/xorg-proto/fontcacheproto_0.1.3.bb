require xorg-proto-common.inc

SUMMARY = "XFontCache: X Font Cache extension headers"

DESCRIPTION = "This package provides the wire protocol for the X Font \
Cache extension.  This extension is used by X-TrueType to cache \
information about fonts."

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=ba9fcd8fe6d09af9f733daccc1a21857 \
                    file://fontcacheP.h;endline=28;md5=85c80bfa96c802ee85a570862ee60214"

PR = "r1"
PE = "1"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "a8a50e5e995bfacb0359575faf7f6906"
SRC_URI[sha256sum] = "1dcaa659d416272ff68e567d1910ccc1e369768f13b983cffcccd6c563dbe3cb"
