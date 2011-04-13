SUMMARY = "GNOME Authentication Agent for PolicyKit"
DESCRIPTION = "PolicyKit-gnome provides an Authentication Agent for PolicyKit that integrates well with the GNOME desktop environment"
HOMEPAGE = "http://www.packagekit.org/"
BUGTRACKER = "http://bugzilla.gnome.org/"
DEPENDS = "polkit dbus-glib gconf gtk+"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=74579fab173e4c5e12aac0cd83ee98ec \
                    file://src/main.c;beginline=1;endline=20;md5=aba145d1802f2329ba561e3e48ecb795"

SRC_URI = "http://hal.freedesktop.org/releases/polkit-gnome-${PV}.tar.bz2 \
          "

PR = "r0"

EXTRA_OECONF = " --disable-scrollkeeper \
                 --disable-man-pages \
                 --disable-examples \
                 --disable-gtk-doc \
                 --disable-introspection "

inherit autotools pkgconfig

FILES_${PN} += " ${datadir}/dbus-1 \
                 ${datadir}/PolicyKit \
               "
SRC_URI[md5sum] = "da6aaff473ed80f8958fd6f67a59defe"
SRC_URI[sha256sum] = "f1452e37b6681a0ade2c0cbb3734e6f78c421b958ab4d4eb6a2cd3b9f50db8d0"
