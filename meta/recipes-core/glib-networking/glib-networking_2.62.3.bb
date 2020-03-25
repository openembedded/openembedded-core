SUMMARY = "GLib networking extensions"
DESCRIPTION = "glib-networking contains the implementations of certain GLib networking features that cannot be implemented directly in GLib itself because of their dependencies."
HOMEPAGE = "https://gitlab.gnome.org/GNOME/glib-networking/"
BUGTRACKER = "http://bugzilla.gnome.org"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

SECTION = "libs"
DEPENDS = "glib-2.0"

SRC_URI[archive.md5sum] = "a758ca62bd54982a798b39c744cbf783"
SRC_URI[archive.sha256sum] = "8ca1f86f23a76b5c7640624f7d5490705c78e81375e1741c9a1c41ce7f8f7ff7"

PACKAGECONFIG ??= "gnutls"

PACKAGECONFIG[gnutls] = "-Dgnutls=enabled,-Dgnutls=disabled,gnutls"
PACKAGECONFIG[openssl] = "-Dopenssl=enabled,-Dopenssl=disabled,openssl"
PACKAGECONFIG[libproxy] = "-Dlibproxy=enabled,-Dlibproxy=disabled,libproxy"

EXTRA_OEMESON = "-Dgnome_proxy=disabled"

GNOMEBASEBUILDCLASS = "meson"
inherit gnomebase gettext upstream-version-is-even gio-module-cache

FILES_${PN} += "\
                ${libdir}/gio/modules/libgio*.so \
                ${datadir}/dbus-1/services/ \
                ${systemd_user_unitdir} \
                "
FILES_${PN}-dev += "${libdir}/gio/modules/libgio*.la"
FILES_${PN}-staticdev += "${libdir}/gio/modules/libgio*.a"

BBCLASSEXTEND = "native"
