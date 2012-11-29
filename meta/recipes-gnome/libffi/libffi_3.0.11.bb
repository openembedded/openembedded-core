SUMMARY = "A portable foreign function interface library"
DESCRIPTION = "The `libffi' library provides a portable, high level programming interface to various calling \
conventions.  This allows a programmer to call any function specified by a call interface description at run \
time. FFI stands for Foreign Function Interface.  A foreign function interface is the popular name for the \
interface that allows code written in one language to call code written in another language.  The `libffi' \
library really only provides the lowest, machine dependent layer of a fully featured foreign function interface.  \
A layer must exist above `libffi' that handles type conversions for values passed between the two languages."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e54c573c49435ccbbd3f6dc9e49a065e"

PR = "r1"

SRC_URI = "ftp://sourceware.org/pub/libffi/${BPN}-${PV}.tar.gz \
           file://0001-libffi-update-for-3.0.11.patch \
           file://add-aarch64-support.patch \
           file://aarch64-adding-build-support.patch \
"

SRC_URI[md5sum] = "f69b9693227d976835b4857b1ba7d0e3"
SRC_URI[sha256sum] = "70bfb01356360089aa97d3e71e3edf05d195599fd822e922e50d46a0055a6283"

EXTRA_OECONF += "--disable-builddir"

inherit autotools

FILES_${PN}-dev += "${libdir}/libffi-${PV}"

BBCLASSEXTEND = "native nativesdk"
