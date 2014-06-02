SUMMARY = "GNOME desktop-wide GSettings schemas"
HOMEPAGE = "http://live.gnome.org/gsettings-desktop-schemas"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"
PR = "r1"

DEPENDS = "glib-2.0 intltool-native gobject-introspection-stub-native"

inherit gnomebase gsettings gettext

GNOME_COMPRESS_TYPE = "xz"

SRC_URI[archive.md5sum] = "f9ffca591a984f19a1dd9caeb96b5f23"
SRC_URI[archive.sha256sum] = "452378c4960a145747ec69f8c6a874e5b7715454df3e2452d1ff1a0a82e76811"
