DESCRIPTION = "Clutter Helix"
HOMEPAGE = "http://www.clutter-project.org/"
LICENSE = "LGPL"

DEPENDS = "helix-libs"

FILESPATH = "${FILE_DIRNAME}/clutter-helix"

PACKAGES =+ "${PN}-examples"
FILES_${PN}-examples = "${bindir}/video-player ${bindir}/audio-player"

inherit autotools pkgconfig gtk-doc

do_stage () {
        autotools_stage_all
}

