DESCRIPTION = "Clutter GTK+"
HOMEPAGE = "http://www.clutter-project.org/"
LICENSE = "LGPLv2"

DEPENDS = "gtk+"

FILESPATH = "${FILE_DIRNAME}/clutter-gtk"

PACKAGES =+ "${PN}-examples"
FILES_${PN}-examples = "${bindir}/*"

inherit autotools pkgconfig gtk-doc

