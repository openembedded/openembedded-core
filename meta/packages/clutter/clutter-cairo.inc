DESCRIPTION = "Clutter Cairo"
HOMEPAGE = "http://www.clutter-project.org/"
LICENSE = "LGPL"

DEPENDS = "cairo"

FILESPATH = "${FILE_DIRNAME}/clutter-cairo"

PACKAGES =+ "${PN}-examples"
FILES_${PN}-examples = "${bindir}/flowers ${bindir}/clock"

inherit autotools pkgconfig

do_stage () {
        autotools_stage_all
}

