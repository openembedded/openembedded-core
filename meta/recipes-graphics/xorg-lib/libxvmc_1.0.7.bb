SUMMARY = "XvMC: X Video Motion Compensation extension library"

DESCRIPTION = "XvMC extends the X Video extension (Xv) and enables \
hardware rendered motion compensation support."

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=0a207f08d4961489c55046c9a5e500da \
                    file://wrapper/XvMCWrapper.c;endline=26;md5=5151daa8172a3f1bb0cb0e0ff157d9de"

DEPENDS += "libxext libxv videoproto"

PR = "r0"
PE = "1"

XORG_PN = "libXvMC"

SRC_URI[md5sum] = "3340c99ff556ea2457b4be47f5cb96fa"
SRC_URI[sha256sum] = "28f085fc8518a3dadfe355360705d50153051f09898093e69af806c0b437cea3"
