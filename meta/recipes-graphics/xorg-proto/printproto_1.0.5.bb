require xorg-proto-common.inc

SUMMARY = "XP: X Printing extension headers"

DESCRIPTION = "This package provides the wire protocol for the X \
Printing extension.  This extension provides a way for client \
applications to render to non-display devices."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=658463213f19b48b81f8672d2696069f \
                    file://Print.h;beginline=2;endline=59;md5=db19e065445b3755c09432864dcd8a9b"

PR = "r0"
PE = "1"

SRC_URI[md5sum] = "99d0e25feea2fead7d8325b7000b41c3"
SRC_URI[sha256sum] = "1298316cf43b987365ab7764d61b022a3d7f180b67b423eed3456862d155911a"
