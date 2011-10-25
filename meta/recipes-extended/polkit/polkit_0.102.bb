SUMMARY = "PolicyKit Authorization Framework"
DESCRIPTION = "The polkit package is an application-level toolkit for defining and handling the policy that allows unprivileged processes to speak to privileged processes."
HOMEPAGE = "http://code.google.com/p/polkit/"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=155db86cdbafa7532b41f390409283eb \
                    file://src/polkit/polkit.h;beginline=1;endilne=20;md5=9f797b8912dac8a806be820c14e783f8 \
                    file://docs/polkit/html/license.html;md5=570dd8c794dc8df913cb03b86e78d936"

SRC_URI = "http://hal.freedesktop.org/releases/polkit-${PV}.tar.gz \
           file://introspection.patch \
           ${@base_contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)}"

PAM_SRC_URI = "file://polkit-1_pam.patch"
PR = "r0"
DEPENDS = "libpam expat dbus-glib eggdbus intltool-native"
RDEPENDS_${PN} = "libpam"
EXTRA_OECONF = "--with-authfw=pam --with-os-type=moblin --disable-man-pages --disable-gtk-doc --disable-introspection"

inherit autotools pkgconfig

FILES_${PN} += "${libdir}/${PN}-1/extensions/*.so \
                ${datadir}/${PN}-1/actions/* \
                ${datadir}/dbus-1/system-services/*"
FILES_${PN}-dbg += "${libdir}/${PN}-1/extensions/.debug/*.so"
FILES_${PN}-dev += "${libdir}/${PN}-1/extensions/*.la "

do_install_append() {
	rm -f ${D}${libdir}/${PN}-1/extensions/*.a
}

SRC_URI[md5sum] = "a3726bdb9728c103e58f62131e26693a"
SRC_URI[sha256sum] = "0a6573da841c6f5c428218f1456aed45724a0127932af0de563d568bb9058641"
