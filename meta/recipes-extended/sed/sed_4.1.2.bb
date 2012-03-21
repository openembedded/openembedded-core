DESCRIPTION = "sed is a Stream EDitor."
HOMEPAGE = "http://www.gnu.org/software/sed/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://sed/sed.h;beginline=1;endline=17;md5=e00ffd1837f298439a214fd197f6a407"
SECTION = "console/utils"
PR = "r5"

SRC_URI = "${GNU_MIRROR}/sed/sed-${PV}.tar.gz \
           file://fix_return_type.patch"

SRC_URI[md5sum] = "928f0e06422f414091917401f1a834d0"
SRC_URI[sha256sum] = "638e837ba765d5da0a30c98b57c2953cecea96827882f594612acace93ceeeab"

inherit autotools update-alternatives gettext

do_install () {
	autotools_do_install
	install -d ${D}${base_bindir}
	mv ${D}${bindir}/sed ${D}${base_bindir}/sed.${PN}
	rmdir ${D}${bindir}/
}

ALTERNATIVE_NAME = "sed"
ALTERNATIVE_PATH = "sed.${PN}"
ALTERNATIVE_LINK = "${base_bindir}/sed"
ALTERNATIVE_PRIORITY = "100"

BBCLASSEXTEND = "native"
