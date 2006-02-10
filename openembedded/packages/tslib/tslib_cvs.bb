SECTION = "base"
DESCRIPTION = "tslib is a touchscreen access library."
PV = "0.0+cvs${SRCDATE}"
PR = "r32"

SRC_URI_OVERRIDES_PACKAGE_ARCH = "0"
PACKAGE_ARCH_tslib-conf = "${MACHINE_ARCH}"
PACKAGE_ARCH_mnci = "${MACHINE_ARCH}"

SRC_URI = "cvs://cvs:@pubcvs.arm.linux.org.uk/mnt/src/cvsroot;module=tslib \
	   file://ts.conf \
	   file://ts.conf-h3600 file://ts.conf-h3600-2.4 file://ts.conf-h6300 \
	   file://ts.conf-corgi file://ts.conf-corgi-2.4 \
	   file://ts.conf-simpad file://ts.conf-simpad-2.4 \
	   file://tslib.sh"
SRC_URI_append_mnci += " file://devfs.patch;patch=1"
SRC_URI_append_mnci += " file://event1.patch;patch=1"
S = "${WORKDIR}/tslib"
LICENSE = "LGPL"
CONFFILES_${PN} = "${sysconfdir}/ts.conf"

inherit autotools

PACKAGES = "tslib-conf libts libts-dev tslib-tests tslib-calibrate"
EXTRA_OECONF        = "--enable-shared"
EXTRA_OECONF_mnci   = "--enable-shared --disable-h3600 --enable-input --disable-corgi --disable-collie --disable-mk712 --disable-arctic2 --disable-ucb1x00 "
EXTRA_OECONF_beagle = "--enable-shared --enable-h3600 --disable-input --disable-corgi --disable-collie --disable-mk712 --disable-arctic2 --disable-ucb1x00 "

do_stage () {
autotools_stage_all
}

do_install_prepend () {
	install -m 0644 ${WORKDIR}/ts.conf ${S}/etc/ts.conf
}

do_install_append() {
	install -d ${D}${sysconfdir}/profile.d/
	install -m 0755 ${WORKDIR}/tslib.sh ${D}${sysconfdir}/profile.d/
	case ${MACHINE} in
	h3600 | h3900 | h1940 | h6300 | h2200 | ipaq-pxa270 | blueangel)
		install -d ${D}${datadir}/tslib
		for f in ts.conf-h3600 ts.conf-h3600-2.4 ts.conf-h6300; do
			install -m 0644 ${WORKDIR}/$f ${D}${datadir}/tslib/
		done
		rm -f ${D}${sysconfdir}/ts.conf
		;;
	c7x0 | spitz | akita | tosa | borzoi )
		install -d ${D}${datadir}/tslib
		for f in ts.conf-corgi ts.conf-corgi-2.4; do
			install -m 0644 ${WORKDIR}/$f ${D}${datadir}/tslib/
		done
		rm -f ${D}${sysconfdir}/ts.conf
		;;
	simpad )
		install -d ${D}${datadir}/tslib
		for f in ts.conf-simpad ts.conf-simpad-2.4; do
			install -m 0644 ${WORKDIR}/$f ${D}${datadir}/tslib/
		done
		rm -f ${D}${sysconfdir}/ts.conf
		;;
	*)
		;;
	esac
}

RDEPENDS_libts = "tslib-conf"

FILES_tslib-conf = "${sysconfdir}/ts.conf ${sysconfdir}/profile.d/tslib.sh ${datadir}/tslib"
FILES_libts = "${libdir}/*.so.* ${libdir}/ts/*.so*"
FILES_libts-dev = "${FILES_tslib-dev}"
FILES_tslib-calibrate += "${bindir}/ts_calibrate"
FILES_tslib-tests = "${bindir}/ts_harvest ${bindir}/ts_print ${bindir}/ts_print_raw ${bindir}/ts_test"
