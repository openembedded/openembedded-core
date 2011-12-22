SUMMARY = "PolicyKit Authorization Framework"
DESCRIPTION = "The polkit package is an application-level toolkit for defining and handling the policy that allows unprivileged processes to speak to privileged processes."
HOMEPAGE = "http://code.google.com/p/polkit/"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=155db86cdbafa7532b41f390409283eb \
                    file://src/polkit/polkit.h;beginline=1;endilne=20;md5=9f797b8912dac8a806be820c14e783f8 \
                    file://docs/polkit/html/license.html;md5=d85a36709a446c10f4ee123f9dda0e38"

SRC_URI = "http://hal.freedesktop.org/releases/polkit-${PV}.tar.gz \
           file://introspection.patch \
           ${@base_contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)}"

SRC_URI[md5sum] = "aaacf2ef18774ea8a825a426a7cfe763"
SRC_URI[sha256sum] = "36607c8ce576980ee55bd12bab1944f13217eb8e9d04ab56022545861ece3bc5"

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

