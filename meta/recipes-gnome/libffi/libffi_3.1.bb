SUMMARY = "A portable foreign function interface library"
DESCRIPTION = "The `libffi' library provides a portable, high level programming interface to various calling \
conventions.  This allows a programmer to call any function specified by a call interface description at run \
time. FFI stands for Foreign Function Interface.  A foreign function interface is the popular name for the \
interface that allows code written in one language to call code written in another language.  The `libffi' \
library really only provides the lowest, machine dependent layer of a fully featured foreign function interface.  \
A layer must exist above `libffi' that handles type conversions for values passed between the two languages."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3610bb17683a0089ed64055416b2ae1b"

SRC_URI = "ftp://sourceware.org/pub/libffi/${BP}.tar.gz \
           file://fix-libffi.la-location.patch"

SRC_URI[md5sum] = "f5898b29bbfd70502831a212d9249d10"
SRC_URI[sha256sum] = "97feeeadca5e21870fa4433bc953d1b3af3f698d5df8a428f68b73cd60aef6eb"

EXTRA_OECONF += "--disable-builddir"

inherit autotools texinfo

FILES_${PN}-dev += "${libdir}/libffi-${PV}"

BBCLASSEXTEND = "native nativesdk"
