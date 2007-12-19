DESCRIPTION = "Open Hardware Manager"
HOMEPAGE = "http://freedesktop.org/Software/ohm"
LICENSE = "LGPL"

DEPENDS = "gtk+ dbus-glib intltool-native hal"
RDEPENDS_${PN} += "udev hal-info"
SRC_URI = "git://anongit.freedesktop.org/git/ohm/;protocol=git"

PV = "0.1.2+git-locrev${SRCREV}"
PR = "r0"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF = "--with-distro=debian \
        --without-xauth \
        --disable-gtk-doc \
        --disable-docbook-docs"

do_configure_prepend() {
        touch gtk-doc.make
}

OE_LT_RPATH_ALLOW=":${libdir}/libohm:"
OE_LT_RPATH_ALLOW[export]="1"

PACKAGES =+ "libohm ohm-plugin-x11"

FILES_${PN}-dev += "${libdir}/ohm/*.a"

FILES_${PN} = "${sysconfdir} \
               ${bindir}/* \
               ${sbindir}/* \
               ${libdir}/ohm/*.so \
               "

FILES_libohm = "${libdir}/libohm.so.*"
FILES_ohm-plugin-x11 = "${libdir}/ohm/libohm_x*.so \
                        ${libdir}/ohm/libohm_idle.so \ 
                        ${sysconfdir}/ohm/plugins.d/x* \
                        ${sysconfdir}/ohm/plugins.d/idle* \
                        "
