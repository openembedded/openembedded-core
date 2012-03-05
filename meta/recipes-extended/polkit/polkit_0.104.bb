SUMMARY = "PolicyKit Authorization Framework"
DESCRIPTION = "The polkit package is an application-level toolkit for defining and handling the policy that allows unprivileged processes to speak to privileged processes."
HOMEPAGE = "http://code.google.com/p/polkit/"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=155db86cdbafa7532b41f390409283eb \
                    file://src/polkit/polkit.h;beginline=1;endline=20;md5=0a8630b0133176d0504c87a0ded39db4 \
                    file://docs/polkit/html/license.html;md5=d85a36709a446c10f4ee123f9dda0e38"

DEPENDS = "expat dbus-glib eggdbus intltool-native"

PACKAGECONFIG = "${@base_contains('DISTRO_FEATURES', 'pam', 'pam', '', d)}"
PACKAGECONFIG[pam] = "--with-authfw=pam,--with-authfw=shadow,libpam,libpam"

PR = "r5"

PAM_SRC_URI = "file://polkit-1_pam.patch"
SRC_URI = "http://hal.freedesktop.org/releases/polkit-${PV}.tar.gz \
           file://introspection.patch \
           ${@base_contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)} \
           file://0001-PolkitUnixSession-Set-error-if-we-cannot-find-a-sess.patch \
           file://0002-PolkitUnixSession-Actually-return-TRUE-if-a-session-.patch \
          "

SRC_URI[md5sum] = "e380b4c6fb1e7bccf854e92edc0a8ce1"
SRC_URI[sha256sum] = "6b0a13d8381e4a7b7e37c18a54595191b50757e0fcd186cd9918e9ad0f18c7f9"

EXTRA_OECONF = "--with-os-type=moblin --disable-man-pages --disable-gtk-doc --disable-introspection"

inherit autotools pkgconfig

do_install_append() {
	rm -f ${D}${libdir}/${PN}-1/extensions/*.a
}

FILES_${PN} += "${libdir}/${PN}-1/extensions/*.so \
                ${datadir}/${PN}-1/actions/* \
                ${datadir}/dbus-1/system-services/*"
FILES_${PN}-dbg += "${libdir}/${PN}-1/extensions/.debug/*.so"
FILES_${PN}-dev += "${libdir}/${PN}-1/extensions/*.la "

