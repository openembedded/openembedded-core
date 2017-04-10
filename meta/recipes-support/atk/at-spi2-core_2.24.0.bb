SUMMARY = "Assistive Technology Service Provider Interface (dbus core)"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e9f288ba982d60518f375b5898283886"

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/${BPN}/${MAJ_VER}/${BPN}-${PV}.tar.xz \
           file://0001-nls.m4-Take-it-from-gettext-0.15.patch \
           file://0001-build-Add-with-systemduserunitdir.patch \
          "

SRC_URI[md5sum] = "27598276ec58371a6254a4f8b949bf8c"
SRC_URI[sha256sum] = "6179f314fbd903b3cd3477a57b6b1fecca88b76ce769ce777fda0d3e5f61afcd"

DEPENDS = "dbus glib-2.0 virtual/libx11 libxi libxtst intltool-native"

inherit autotools gtk-doc gettext systemd pkgconfig distro_features_check upstream-version-is-even gobject-introspection
# depends on virtual/libx11
REQUIRED_DISTRO_FEATURES = "x11"

EXTRA_OECONF = "--disable-xevie \
                --with-systemduserunitdir=${systemd_user_unitdir} \
                --with-dbus-daemondir=${bindir}"

FILES_${PN} += "${datadir}/dbus-1/services/*.service \
                ${datadir}/dbus-1/accessibility-services/*.service \
                ${datadir}/defaults/at-spi2 \
                ${systemd_user_unitdir}/at-spi-dbus-bus.service \
                "
