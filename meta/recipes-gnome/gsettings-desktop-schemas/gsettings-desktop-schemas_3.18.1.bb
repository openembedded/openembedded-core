SUMMARY = "GNOME desktop-wide GSettings schemas"
HOMEPAGE = "http://live.gnome.org/gsettings-desktop-schemas"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS = "glib-2.0 intltool-native gobject-introspection-stub-native"

inherit gnomebase gsettings gettext

SRC_URI[archive.md5sum] = "e7ec56bb55013e89500c8f3ae6fcf738"
SRC_URI[archive.sha256sum] = "258713b2a3dc6b6590971bcfc81f98d78ea9827d60e2f55ffbe40d9cd0f99a1a"

