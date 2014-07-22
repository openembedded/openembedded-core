SUMMARY = "Common macros for building GNOME applications"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SECTION = "x11/gnome"
inherit gnomebase allarch

GNOME_COMPRESS_TYPE = "xz"

SRC_URI[archive.md5sum] = "da903a1c89b0a24e062227fa97d42fe7"
SRC_URI[archive.sha256sum] = "18712bc2df6b2dd88a11b9f7f874096d1c0c6e7ebc9cfc0686ef963bd590e1d8"

EXTRA_AUTORECONF = ""
DEPENDS = ""

FILES_${PN} += "${datadir}/aclocal"
FILES_${PN}-dev = ""

BBCLASSEXTEND = "native"
