SUMMARY = "PolicyKit Authorization Framework"
DESCRIPTION = "The polkit package is an application-level toolkit for defining and handling the policy that allows unprivileged processes to speak to privileged processes."
HOMEPAGE = "http://code.google.com/p/polkit/"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=155db86cdbafa7532b41f390409283eb \
                    file://src/polkit/polkit.h;beginline=1;endilne=20;md5=9f797b8912dac8a806be820c14e783f8 \
                    file://docs/polkit/html/license.html;md5=07ddbf5f29e44c80c99be19c1690ec1f"

SRC_URI = "http://hal.freedesktop.org/releases/polkit-${PV}.tar.gz \
           file://introspection.patch \
           ${@base_contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)}"

PAM_SRC_URI = "file://polkit-1_pam.patch"
PR = "r1"
DEPENDS = "libpam expat dbus-glib eggdbus intltool"
RDEPENDS_${PN} = "libpam"
EXTRA_OECONF = "--with-authfw=pam --with-os-type=moblin --disable-man-pages --disable-gtk-doc --disable-introspection"

inherit autotools pkgconfig

FILES_${PN} += "${libdir}/${PN}-1/extensions/*.so \
                ${datadir}/${PN}-1/actions/* \
                ${datadir}/dbus-1/system-services/*"
FILES_${PN}-dbg += "${libdir}/${PN}-1/extensions/.debug/*.so"

SRC_URI[md5sum] = "f925ac93aba3c072977370c1e27feb7f"
SRC_URI[sha256sum] = "927f65760e4fce23d7cdeae90245c22986eb0a39335a344915302158f73f9f1b"
