LICENSE = "LGPL"
DEPENDS = "e2fsprogs gssdp libsoup-2.4 libxml2"

SRC_URI = "http://gupnp.org/sources/${PN}/${PN}-${PV}.tar.gz"
PR = "r1"

inherit autotools_stage pkgconfig

FILES_${PN} = "${libdir}/*.so.*"
FILES_${PN}-dev += "${bindir}/gupnp-binding-tool"

do_stage_append () {
	install ${S}/tools/gupnp-binding-tool ${STAGING_BINDIR_NATIVE}
}