SUMMARY = "UPnP framework"
DESCRIPTION = "GUPnP is an elegant, object-oriented open source framework for creating UPnP  devices and control points, written in C using GObject and libsoup. The GUPnP API is intended to be easy to use, efficient and flexible. It provides the same set of features as libupnp, but shields the developer from most of UPnP's internals."
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://libgupnp/gupnp.h;beginline=1;endline=20;md5=28c49b17d623afc3335efc2e511879e1"
DEPENDS = "e2fsprogs gssdp libsoup-2.4 libxml2 gnome-icon-theme"

SRC_URI = "http://gupnp.org/sites/all/files/sources/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "61b240c1a9fdf8ca82c5c3adde0d6757"
SRC_URI[sha256sum] = "1b77201866b7632fb68bc2a3bdea22ef8ead34ba850b3ee367797432cfc0f04a"

PR = "r0"

EXTRA_OECONF = "--disable-introspection"

inherit autotools pkgconfig

FILES_${PN} = "${libdir}/*.so.*"
FILES_${PN}-dev += "${bindir}/gupnp-binding-tool"

SYSROOT_PREPROCESS_FUNCS += "gupnp_sysroot_preprocess"

gupnp_sysroot_preprocess () {
	install -d ${SYSROOT_DESTDIR}${bindir_crossscripts}/
	install -m 755 ${D}${bindir}/gupnp-binding-tool ${SYSROOT_DESTDIR}${bindir_crossscripts}/
}

