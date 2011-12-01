SUMMARY = "A portable foreign function interface library"
DESCRIPTION = "The `libffi' library provides a portable, high level programming interface to various calling \
conventions.  This allows a programmer to call any function specified by a call interface description at run \
time. FFI stands for Foreign Function Interface.  A foreign function interface is the popular name for the \
interface that allows code written in one language to call code written in another language.  The `libffi' \
library really only provides the lowest, machine dependent layer of a fully featured foreign function interface.  \
A layer must exist above `libffi' that handles type conversions for values passed between the two languages."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0caa055e49a3fb6c57780595e995e2ab"

PR = "r0"

SRC_URI = "ftp://sourceware.org/pub/libffi/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "79390673f5d07a8fb342bc09b5055b6f"
SRC_URI[sha256sum] = "f01eb9027e9eb56aeaeef636649877756d475d714ef8b47f627f65bc5f3b492f"

EXTRA_OECONF += "--disable-builddir"

inherit autotools

FILES_${PN}-dev += "${libdir}/libffi-${PV}"

BBCLASSEXTEND = "native nativesdk"
