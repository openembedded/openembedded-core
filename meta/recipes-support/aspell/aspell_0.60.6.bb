SRC_URI = "${GNU_MIRROR}/aspell/aspell-${PV}.tar.gz"

SRC_URI[md5sum] = "bc80f0198773d5c05086522be67334eb"
SRC_URI[sha256sum] = "4f5b1520e26cf6bbb1b5ca8a2f9f22948c3ca38ac4c2dd9e02df042fee7c0e36"
DESCRIPTION = "GNU Aspell spell-checker"
SECTION = "console/utils"
LICENSE="LGPLv2 | LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

PACKAGES += "libaspell libpspell libpspell-dev aspell-utils"

FILES_${PN}-dbg += "${libdir}/aspell-0.60/.debu*"
FILES_libaspell = "${libdir}/libaspell.so.* ${libdir}/aspell*"
FILES_aspell-utils = "${bindir}/word-list-compress ${bindir}/aspell-import ${bindir}/run-with-aspell ${bindir}/pre*"
FILES_${PN} = "${bindir}/aspell"
FILES_libpspell = "${libdir}/libpspell.so.*"
FILES_libpspell-dev = "${libdir}/libpspell* ${bindir}/pspell-config ${includedir}/pspell"

inherit autotools gettext
