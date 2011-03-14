SUMMARY = "UPnP framework"
DESCRIPTION = "GUPnP is an elegant, object-oriented open source framework for creating UPnP  devices and control points, written in C using GObject and libsoup. The GUPnP API is intended to be easy to use, efficient and flexible. It provides the same set of features as libupnp, but shields the developer from most of UPnP's internals."
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://libgupnp/gupnp.h;beginline=1;endline=20;md5=28c49b17d623afc3335efc2e511879e1"
DEPENDS = "e2fsprogs gssdp libsoup-2.4 libxml2 gnome-icon-theme"

SRC_URI = "http://gupnp.org/sites/all/files/sources/${BPN}-${PV}.tar.gz \
           file://introspection.patch"

SRC_URI[md5sum] = "8598922256faa5adb28657d0c10a3e3e"
SRC_URI[sha256sum] = "6ac13efe46c6f1e5d05e48e1041bf37dfa702839ce5fbb01e0f314f3904b6a16"

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

