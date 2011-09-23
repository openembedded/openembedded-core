SUMMARY = "A portable foreign function interface library"
DESCRIPTION = "The `libffi' library provides a portable, high level programming interface to various calling \
conventions.  This allows a programmer to call any function specified by a call interface description at run \
time. FFI stands for Foreign Function Interface.  A foreign function interface is the popular name for the \
interface that allows code written in one language to call code written in another language.  The `libffi' \
library really only provides the lowest, machine dependent layer of a fully featured foreign function interface.  \
A layer must exist above `libffi' that handles type conversions for values passed between the two languages."
SRC_URI = "ftp://sourceware.org/pub/libffi/${BPN}-${PV}.tar.gz"

PR = "r1"

SRC_URI[md5sum] = "1f300a7a7f975d4046f51c3022fa5ff1"
SRC_URI[sha256sum] = "589d25152318bc780cd8919b14670793f4971d9838dab46ed38c32b3ee92c452"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fa09cb778aaba64dc9eac37ab7e4e5d8"
inherit autotools

FILES_${PN}-dev += "${libdir}/libffi-${PV}"
BBCLASSEXTEND = "native"
