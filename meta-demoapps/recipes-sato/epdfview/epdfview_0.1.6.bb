DESCRIPTION = "A minimal PDF viewer based on gtk and poppler"
HOMEPAGE = "http://trac.emma-soft.com/epdfview/"
LICENSE = "GPLv2"
SECTION = "x11/applications"
DEPENDS = "poppler gtk+ libowl"
PR="r0"

SRC_URI = "http://trac.emma-soft.com/epdfview/chrome/site/releases/epdfview-${PV}.tar.bz2 \
           file://owl-menus.patch \
           file://epdfview.desktop \
           file://epdfview-ui.xml \
           file://epdfview-ui-print.xml"

inherit autotools gettext

do_install_prepend() {
	install ${WORKDIR}/epdfview-ui.xml ${S}/data/epdfview-ui.xml
	install ${WORKDIR}/epdfview-ui-print.xml ${S}/data/epdfview-ui-print.xml
	install ${WORKDIR}/epdfview.desktop ${S}/data/epdfview.desktop
}

