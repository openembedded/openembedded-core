DESCRIPTION = "Less is a program similar to more, i.e. a terminal \
based program for viewing text files and the output from other \
programs. Less offers many features beyond those that more does."
HOMEPAGE = "http://www.greenwoodsoftware.com/"
SECTION = "console/utils"
LICENSE = "BSD"
DEPENDS = "ncurses"

SRC_URI = "http://www.greenwoodsoftware.com/less/less-406.tar.gz "

inherit autotools update-alternatives

do_install () {
        oe_runmake 'bindir=${D}${bindir}' 'mandir=${D}${mandir}' install
        mv ${D}${bindir}/less ${D}${bindir}/less.${PN}
}

ALTERNATIVE_NAME = "less"
ALTERNATIVE_PATH = "less.${PN}"
ALTERNATIVE_PRIORITY = "100"
