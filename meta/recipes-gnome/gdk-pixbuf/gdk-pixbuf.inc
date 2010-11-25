DESCRIPTION = "Image loading library for GTK+"
HOMEPAGE = "http://www.gtk.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://gdk-pixbuf/gdk-pixbuf.h;endline=26;md5=5066b71daefeff678494fffa3040aba9"

SECTION = "libs"
PRIORITY = "optional"

DEPENDS = "libpng gettext glib-2.0"

SRC_URI = "http://ftp.acc.umu.se/pub/GNOME/sources/gdk-pixbuf/2.22/gdk-pixbuf-${PV}.tar.gz \
           file://hardcoded_libtool.patch;patch=1 \
           file://configure_fix.patch;patch=1 \
           "

SRC_URI[md5sum] = "fcfc854e9aec7dbb2bb3059484d44556"
SRC_URI[sha256sum] = "bbb57364ffba70d64f5fcfe6eda1d67249b3d58844edb06dc0f94d1ad599b4ec"

inherit autotools pkgconfig

LIBV = "2.10.0"

EXTRA_OECONF = "\
  --without-libtiff \
  --with-libpng \
"

