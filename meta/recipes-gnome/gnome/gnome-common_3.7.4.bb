SUMMARY = "Common macros for building GNOME applications"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SECTION = "x11/gnome"
inherit gnomebase allarch

GNOME_COMPRESS_TYPE = "xz"

SRC_URI[archive.md5sum] = "974cd54626f1d1787481288ef26bd4ba"
SRC_URI[archive.sha256sum] = "2af2d97010d2a9aeb3b99dd2ce07d1ef023e15a27b412ffe4f067f016ff7117b"

EXTRA_AUTORECONF = ""
DEPENDS = ""

FILES_${PN} += "${datadir}/aclocal"
FILES_${PN}-dev = ""

BBCLASSEXTEND = "native"
