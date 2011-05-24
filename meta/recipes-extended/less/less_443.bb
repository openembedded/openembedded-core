SUMMARY = "Text file viewer similar to more"
DESCRIPTION = "Less is a program similar to more, i.e. a terminal \
based program for viewing text files and the output from other \
programs. Less offers many features beyond those that more does."
HOMEPAGE = "http://www.greenwoodsoftware.com/"
SECTION = "console/utils"

# (GPLv2+ (<< 418), GPLv3+ (>= 418)) | less
# less is a 2-clause BSD-like permissive license
LICENSE = "GPLv3+ | less"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://LICENSE;md5=1237c0f825bb36122b0b2b486ffbe6be"
DEPENDS = "ncurses"
PR = "r0"

SRC_URI = "http://www.greenwoodsoftware.com/${BPN}/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "47db098fb3cdaf847b3c4be05ee954fc"
SRC_URI[sha256sum] = "a4c3e8af81fd0944941ee7c74eecc7759422a227df52335e899e69de5eae30ca"


inherit autotools update-alternatives

do_install () {
        oe_runmake 'bindir=${D}${bindir}' 'mandir=${D}${mandir}' install
        mv ${D}${bindir}/less ${D}${bindir}/less.${PN}
}

ALTERNATIVE_NAME = "less"
ALTERNATIVE_PATH = "less.${PN}"
ALTERNATIVE_PRIORITY = "100"
