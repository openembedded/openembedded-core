HOMEPAGE = "http://www.packagekit.org/"
DEPENDS = "policykit dbus-glib gconf gtk+"

SRC_URI = "http://hal.freedesktop.org/releases/PolicyKit-gnome-${PV}.tar.bz2 \
          "

EXTRA_OECONF = " --disable-scrollkeeper \
                 --disable-man-pages \
                 --disable-examples \
                 --disable-gtk-doc"

S = "${WORKDIR}/PolicyKit-gnome-${PV}"

inherit autotools_stage pkgconfig

FILES_${PN} += " ${datadir}/dbus-1 \
                 ${datadir}/PolicyKit \
               "

