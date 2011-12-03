SUMMARY = "Text file viewer similar to more"
DESCRIPTION = "Less is a program similar to more, i.e. a terminal \
based program for viewing text files and the output from other \
programs. Less offers many features beyond those that more does."
HOMEPAGE = "http://www.greenwoodsoftware.com/"
SECTION = "console/utils"

# (GPLv2+ (<< 418), GPLv3+ (>= 418)) | less
# less is a 2-clause BSD-like permissive license
# Reading LICENSE and COPYING indicate that GPL-3+ is suffient.
# openSuse .spec and Debian seem to agree here. setting to GPL-3+

LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://LICENSE;md5=1237c0f825bb36122b0b2b486ffbe6be"
DEPENDS = "ncurses"
PR = "r1"

SRC_URI = "http://www.greenwoodsoftware.com/${BPN}/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "56f9f76ffe13f70155f47f6b3c87d421"
SRC_URI[sha256sum] = "be64ad3e22d6d4aa19fe7024d998563a1ce1671ee3625f8851d26b16dedcdeeb"

inherit autotools update-alternatives

do_install () {
        oe_runmake 'bindir=${D}${bindir}' 'mandir=${D}${mandir}' install
        mv ${D}${bindir}/less ${D}${bindir}/less.${PN}
}

ALTERNATIVE_NAME = "less"
ALTERNATIVE_PATH = "less.${PN}"
ALTERNATIVE_PRIORITY = "100"
