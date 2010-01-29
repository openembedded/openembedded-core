HOMEPAGE = "http://www.packagekit.org/"

SRC_URI = "http://hal.freedesktop.org/releases/polkit-${PV}.tar.gz"

DEPENDS = "pam expat dbus-glib eggdbus"
RDEPENDS = "pam"
EXTRA_OECONF = "--with-authfw=pam --with-os-type=moblin --disable-man-pages --disable-gtk-doc --disable-introspection"

inherit autotools_stage pkgconfig
