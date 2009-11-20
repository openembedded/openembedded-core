HOMEPAGE = "http://www.packagekit.org/"
PR = "r4"

SRC_URI = "http://hal.freedesktop.org/releases/PolicyKit-0.9.tar.gz"


DEPENDS = "pam expat dbus-glib"
RDEPENDS = "pam"
EXTRA_OECONF = "--with-authfw=pam --with-os-type=moblin --disable-man-pages --disable-gtk-doc"

S = "${WORKDIR}/PolicyKit-${PV}"

inherit autotools_stage pkgconfig
