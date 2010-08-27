DESCRIPTION = "Dark GTK+ theme"
LICENSE = "GPL"
DEPENDS = "gtk-engines"
RDEPENDS = "gtk-engine-clearlooks gnome-icon-theme"
SECTION = "x11/base"
PACKAGE_ARCH = "all"

PR = "r1"

#SRC_URI = "http://art.gnome.org/download/themes/gtk2/1285/GTK2-Darkilouche.tar.bz2"
SRC_URI = "git://people.freedesktop.org/~jimmac/Darkilouche.git;protocol=git \
           file://change-colours.diff;patch=1"

S = "${WORKDIR}/git"
PACKAGES = ${PN}

do_install () {
   install -d ${D}${datadir}/themes/Darkilouche/gtk-2.0
   install -m 0644 ${S}/index.theme ${D}${datadir}/themes/Darkilouche
   install -m 0644 ${S}/gtk-2.0/gtkrc ${D}${datadir}/themes/Darkilouche/gtk-2.0
}

FILES_${PN} = "${datadir}/themes/Darkilouche/"
