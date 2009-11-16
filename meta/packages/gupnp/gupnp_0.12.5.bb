LICENSE = "LGPL"
DEPENDS = "e2fsprogs gssdp libsoup-2.4 libxml2"

SRC_URI = "http://gupnp.org/sources/${PN}/${PN}-${PV}.tar.gz"
PR = "r1"

inherit autotools_stage pkgconfig

FILES_${PN} = "${libdir}/*.so.*"
FILES_${PN}-dev += "${bindir}/gupnp-binding-tool"

SYSROOT_PREPROCESS_FUNCS += "gupnp_sysroot_preprocess"

gupnp_sysroot_preprocess () {
	install -d ${SYSROOT_DESTDIR}${STAGING_BINDIR_CROSS}/
	install -m 755 ${bindir}/gupnp-binding-tool ${SYSROOT_DESTDIR}${STAGING_BINDIR_CROSS}/
}
