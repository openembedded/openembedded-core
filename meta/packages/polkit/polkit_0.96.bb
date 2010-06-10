DESCRIPTION = "The polkit package is an application-level toolkit for defining and handling the policy that allows unprivileged processes to speak to privileged processes."
HOMEPAGE = "http://code.google.com/p/polkit/"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=155db86cdbafa7532b41f390409283eb \
                    file://src/polkit/polkit.h;md5=8aa8924570fc5570d20e4a4ad5d2db51 \
                    file://docs/polkit/html/license.html;md5=4c17ef1587e0f096c82157160d4e340e"

SRC_URI = "http://hal.freedesktop.org/releases/polkit-${PV}.tar.gz"
PR="r1"
DEPENDS = "pam expat dbus-glib eggdbus intltool"
RDEPENDS = "pam"
EXTRA_OECONF = "--with-authfw=pam --with-os-type=moblin --disable-man-pages --disable-gtk-doc --disable-introspection"

inherit autotools pkgconfig
