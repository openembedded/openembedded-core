DESCRIPTION = "sed is a Stream EDitor."
HOMEPAGE = "http://www.gnu.org/software/sed/"
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949 \
                    file://sed/sed.h;beginline=1;endline=17;md5=767ab3a06d7584f6fd0469abaec4412f"
SECTION = "console/utils"
PR = "r0"

DEPENDS = "gettext"

SRC_URI = "${GNU_MIRROR}/sed/sed-${PV}.tar.gz"

inherit autotools update-alternatives

do_install () {
	autotools_do_install
	install -d ${D}${base_bindir}
	mv ${D}${bindir}/sed ${D}${base_bindir}/sed.${PN}
}

ALTERNATIVE_NAME = "sed"
ALTERNATIVE_PATH = "sed.${PN}"
ALTERNATIVE_PRIORITY = "100"

BBCLASSEXTEND = "native"
