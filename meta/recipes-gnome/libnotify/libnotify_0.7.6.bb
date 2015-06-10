SUMMARY = "Library for sending desktop notifications to a notification daemon"
SECTION = "libs"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

DEPENDS = "dbus gtk+3 dbus-glib"

inherit gnomebase distro_features_check gtk-doc

SRC_URI[archive.md5sum] = "a4997019d08f46f3bf57b78e6f795a59"
SRC_URI[archive.sha256sum] = "0ef61ca400d30e28217979bfa0e73a7406b19c32dd76150654ec5b2bdf47d837"

