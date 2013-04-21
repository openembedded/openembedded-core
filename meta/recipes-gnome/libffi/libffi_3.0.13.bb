SUMMARY = "A portable foreign function interface library"
DESCRIPTION = "The `libffi' library provides a portable, high level programming interface to various calling \
conventions.  This allows a programmer to call any function specified by a call interface description at run \
time. FFI stands for Foreign Function Interface.  A foreign function interface is the popular name for the \
interface that allows code written in one language to call code written in another language.  The `libffi' \
library really only provides the lowest, machine dependent layer of a fully featured foreign function interface.  \
A layer must exist above `libffi' that handles type conversions for values passed between the two languages."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e54c573c49435ccbbd3f6dc9e49a065e"

SRC_URI = "ftp://sourceware.org/pub/libffi/${BPN}-${PV}.tar.gz \
           file://fix-libffi.la-location.patch"

SRC_URI[md5sum] = "45f3b6dbc9ee7c7dfbbbc5feba571529"
SRC_URI[sha256sum] = "1dddde1400c3bcb7749d398071af88c3e4754058d2d4c0b3696c2f82dc5cf11c"

EXTRA_OECONF += "--disable-builddir"

inherit autotools

FILES_${PN}-dev += "${libdir}/libffi-${PV}"

BBCLASSEXTEND = "native nativesdk"
