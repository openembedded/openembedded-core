SRC_URI = "ftp://ftp.gnu.org/gnu/aspell/aspell-${PV}.tar.gz"
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
