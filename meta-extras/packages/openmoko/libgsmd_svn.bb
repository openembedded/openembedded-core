DESCRIPTION = "GSM libraries and daemons implementing the 07.10 specification"
HOMEPAGE = "http://www.openmoko.org"
LICENSE = "GPL"
SECTION = "libs/gsm"
PROVIDES += "gsmd"
PV = "0.0+svn${SRCDATE}"
PR = "r16"

SRC_URI = "svn://svn.openmoko.org/trunk/src/target;module=gsm;proto=http \
           file://gsmd \
           file://default \
           file://getopt-wait-interpreter-ready.patch;patch=1 \
           file://tihtc-csq-fix.patch;patch=1 \
           file://universal-wcdma.patch;patch=1 \
           file://no-version.patch;patch=1"

S = "${WORKDIR}/gsm"

inherit autotools pkgconfig update-rc.d

INITSCRIPT_NAME = "gsm"
INITSCRIPT_PARAMS = "defaults 35"

do_stage() {
    autotools_stage_all
}

do_install_append() {
	install -d ${D}/${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/gsmd ${D}/${sysconfdir}/init.d/gsm
	install -d ${D}/${sysconfdir}/default
	install ${WORKDIR}/default ${D}/${sysconfdir}/default/gsmd
}

PACKAGES =+ "${PN}-tools gsmd gsmd-plugins"
RDEPENDS_${PN} = "gsmd"
RRECOMMENDS_gsmd = "gsmd-plugins"
FILES_${PN}-tools = "${bindir}/*"
FILES_gsmd = "${sbindir}/gsmd ${sysconfdir}"
FILES_gsmd-plugins = "${libdir}/gsmd/*.so*"
FILES_${PN}-dbg += "${libdir}/gsmd/.debug/*.so"

PACKAGES_DYNAMIC = "libgsmd* gsmd"
