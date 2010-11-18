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
SRC_URI[md5sum] = "f1a2565083266bc8c05c60aa7d8a0f6a"
SRC_URI[sha256sum] = "73c4d04aa89d9a9687f3650327c0ec1cba704e9d5b7fc193958fa81d621208ba"
