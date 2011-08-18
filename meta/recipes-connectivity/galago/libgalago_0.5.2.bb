SUMMARY =       "Desktop presence framework"
DESCRIPTION =	"Galago is a desktop presence framework, designed to transmit presence information between programs."
HOMEPAGE =	"http://www.galago-project.org/"
LICENSE =	"LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34 \
                    file://libgalago/galago.h;endline=21;md5=141785cb9ec62067398dda136a7bb401"

DEPENDS = 	"dbus glib-2.0 dbus-glib"

SRC_URI =	"http://www.galago-project.org/files/releases/source/${BPN}/${BPN}-${PV}.tar.gz \
                 file://mkdir.patch \
                 file://pkgconfig.patch "

SRC_URI[md5sum] = "7ec92f2ecba1309ac4b71b4b4d8d0a0d"
SRC_URI[sha256sum] = "9b7c9845e2f661dbf5c2595e67bc7afd48f090ac2c033726c89d7f0e90791dfa"
PR = "r2"

inherit autotools pkgconfig gettext

EXTRA_OECONF = "--disable-tests --disable-check"

do_configure_prepend_libc-uclibc() {
        touch ${S}/config.rpath
}
