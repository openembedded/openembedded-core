DESCRIPTION = "Less is a program similar to more, i.e. a terminal \
based program for viewing text files and the output from other \
programs. Less offers many features beyond those that more does."
HOMEPAGE = "http://www.greenwoodsoftware.com/"
SECTION = "console/utils"

# (GPLv2+ (<< 418), GPLv3+ (>= 418)) | less
# less is a 2-clause BSD-like permissive license
LICENSE = "GPLv3+ | less"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://LICENSE;md5=fd5ccf3de28f72a0254fa8768ec8180c"
DEPENDS = "ncurses"
PR = "r0"

SRC_URI = "http://www.greenwoodsoftware.com/${PN}/${PN}-${PV}.tar.gz"


inherit autotools update-alternatives

do_install () {
        oe_runmake 'bindir=${D}${bindir}' 'mandir=${D}${mandir}' install
        mv ${D}${bindir}/less ${D}${bindir}/less.${PN}
}

ALTERNATIVE_NAME = "less"
ALTERNATIVE_PATH = "less.${PN}"
ALTERNATIVE_PRIORITY = "100"
