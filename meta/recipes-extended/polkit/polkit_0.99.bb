SUMMARY = "PolicyKit Authorization Framework"
DESCRIPTION = "The polkit package is an application-level toolkit for defining and handling the policy that allows unprivileged processes to speak to privileged processes."
HOMEPAGE = "http://code.google.com/p/polkit/"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=155db86cdbafa7532b41f390409283eb \
                    file://src/polkit/polkit.h;beginline=1;endilne=20;md5=9f797b8912dac8a806be820c14e783f8 \
                    file://docs/polkit/html/license.html;md5=54750ec6ece444c50b823234d445dd99"

SRC_URI = "http://hal.freedesktop.org/releases/polkit-${PV}.tar.gz"
PR = "r0"
DEPENDS = "libpam expat dbus-glib eggdbus intltool"
RDEPENDS = "libpam"
EXTRA_OECONF = "--with-authfw=pam --with-os-type=moblin --disable-man-pages --disable-gtk-doc --disable-introspection"

inherit autotools pkgconfig

FILES_${PN} += "${libdir}/${PN}-1/extensions/*.so \
                ${datadir}/${PN}-1/actions/* \
                ${datadir}/dbus-1/system-services/*"
FILES_${PN}-dbg += "${libdir}/${PN}-1/extensions/.debug/*.so"

SRC_URI[md5sum] = "fcc4d7b19c08ad54d3ce0eae0ab12398"
SRC_URI[sha256sum] = "f612c7c26ec822f67751420057a4ae113fc50ab51070758faacf2ad30bb3583f"
