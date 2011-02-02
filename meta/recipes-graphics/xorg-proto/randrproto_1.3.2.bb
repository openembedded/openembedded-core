require xorg-proto-common.inc

SUMMARY = "XRandR: X Resize, Rotate and Reflect extension headers"

DESCRIPTION = "This package provides the wire protocol for the X Resize, \
Rotate and Reflect extension.  This extension provides the ability to \
resize, rotate and reflect the root window of a screen."

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=5fa3f85d9eefaa3a945071485be11343 \
                    file://randrproto.h;endline=30;md5=3885957c6048fdf3310ac8ba54ca2c3f"

CONFLICTS = "randrext"
PR = "r0"
PE = "1"

BBCLASSEXTEND = "nativesdk"

SRC_URI[md5sum] = "597491c0d8055e2a66f11350c4985775"
SRC_URI[sha256sum] = "d90d00612cc49292bd8d9dc19efb1c9187385fbe87590d7279a02e5e1066dc71"
