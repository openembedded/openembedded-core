#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

def gnome_verdir(v):
    # GNOME dirs use the first two version components, e.g. 0.21.8.2 -> 0.21
    parts = v.split(".")
    return ".".join(parts[:2] if len(parts) >= 3 else parts[:-1]) or v


GNOME_COMPRESS_TYPE ?= "xz"
SECTION ?= "x11/gnome"
GNOMEBN ?= "${BPN}"
SRC_URI = "${GNOME_MIRROR}/${GNOMEBN}/${@gnome_verdir("${PV}")}/${GNOMEBN}-${PV}.tar.${GNOME_COMPRESS_TYPE};name=archive"

S = "${UNPACKDIR}/${GNOMEBN}-${PV}"

FILES:${PN} += "${datadir}/application-registry  \
                ${datadir}/mime-info \
                ${datadir}/mime/packages \
                ${datadir}/mime/application \
                ${datadir}/gnome-2.0 \
                ${datadir}/polkit* \
                ${datadir}/GConf \
                ${datadir}/glib-2.0/schemas \
                ${datadir}/appdata \
                ${datadir}/icons \
"

FILES:${PN}-doc += "${datadir}/devhelp"

GNOMEBASEBUILDCLASS ??= "meson"
inherit pkgconfig
inherit_defer ${GNOMEBASEBUILDCLASS}

do_install:append() {
	rm -rf ${D}${localstatedir}/lib/scrollkeeper/*
	rm -rf ${D}${localstatedir}/scrollkeeper/*
	rm -f ${D}${datadir}/applications/*.cache
}
