DESCRIPTION = "Clutter GTK+"
HOMEPAGE = "http://www.clutter-project.org/"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

DEPENDS = "gtk+"

FILESPATH = "${FILE_DIRNAME}/clutter-gtk"

PACKAGES =+ "${PN}-examples"
FILES_${PN}-examples = "${bindir}/*"

inherit autotools pkgconfig gtk-doc

