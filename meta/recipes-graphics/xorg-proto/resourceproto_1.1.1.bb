require xorg-proto-common.inc

SUMMARY = "XRes: X Resource extension headers"

DESCRIPTION = "This package provides the wire protocol for the X \
Resource extension.  XRes provides an interface that allows X clients to \
see and monitor X resource usage of various clients."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=e01e66e4b317088cf869bc98e6af4fb6"

CONFLICTS = "resourceext"

PR = "r0"
PE = "1"

SRC_URI[md5sum] = "8ff0525ae7502b48597b78d00bc22284"
SRC_URI[sha256sum] = "8579818e99aa881c00c04eb5f96fcbeec463816ca8e3cda9d3d25562f7a4b8c6"

