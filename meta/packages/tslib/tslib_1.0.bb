DESCRIPTION = "tslib is a plugin-based flexible touchscreen access library."
HOMEPAGE = "http://cvs.arm.linux.org.uk/"
AUTHOR = "Russell King w/ plugins by Chris Larson et. al."
SECTION = "base"
LICENSE = "LGPL"

PR = "r3"

SRC_URI = "http://download.berlios.de/tslib/tslib-1.0.tar.bz2 \
           file://ts.conf \
           file://ts.conf-h3600-2.4 \
           file://ts.conf-simpad-2.4 \
           file://ts.conf-corgi-2.4 \
           file://ts.conf-collie-2.4 \
           file://tslib.sh"
SRC_URI_append_mnci += " file://devfs.patch;patch=1"
SRC_URI_append_mnci += " file://event1.patch;patch=1"

inherit autotools pkgconfig

EXTRA_OECONF        = "--enable-shared"
EXTRA_OECONF_mnci   = "--enable-shared --disable-h3600 --enable-input --disable-corgi --disable-collie --disable-mk712 --disable-arctic2 --disable-ucb1x00 "

do_stage() {
	autotools_stage_all
}

do_install_prepend() {
	install -m 0644 ${WORKDIR}/ts.conf ${S}/etc/ts.conf
}

do_install_append() {
	install -d ${D}${sysconfdir}/profile.d/
	install -m 0755 ${WORKDIR}/tslib.sh ${D}${sysconfdir}/profile.d/
	case ${MACHINE} in
	a780 | e680 | h3600 | h3900 | h5xxx | h1940 | h6300 | h2200 | ipaq-pxa270 | hx4700 | hx2000 | blueangel | h4000)
		install -d ${D}${datadir}/tslib
		install -m 0644 ${WORKDIR}/ts.conf-h3600-2.4 ${D}${datadir}/tslib/
		;;
	c7x0 | spitz | akita | tosa )
		install -d ${D}${datadir}/tslib
		install -m 0644 ${WORKDIR}/ts.conf-corgi-2.4 ${D}${datadir}/tslib/
		;;
	collie | poodle )
		install -d ${D}${datadir}/tslib
		install -m 0644 ${WORKDIR}/ts.conf-collie-2.4 ${D}${datadir}/tslib/
		;;

	simpad )
		install -d ${D}${datadir}/tslib
		install -m 0644 ${WORKDIR}/ts.conf-simpad-2.4 ${D}${datadir}/tslib/
		;;
	*)
		;;
	esac
}

SRC_URI_OVERRIDES_PACKAGE_ARCH = "0"

# People should consider using udev's /dev/input/touchscreen0 symlink 
# instead of detect-stylus
RDEPENDS_tslib-conf_h1940 = "detect-stylus"
RDEPENDS_tslib-conf_h3600 = "detect-stylus"
RDEPENDS_tslib-conf_h3900 = "detect-stylus"
RDEPENDS_tslib-conf_h6300 = "detect-stylus"
RDEPENDS_tslib-conf_blueangel = "detect-stylus"
RDEPENDS_tslib-conf_htcuniversal = "detect-stylus"
RDEPENDS_tslib-conf_h4000 = "detect-stylus"
RPROVIDES_tslib-conf = "libts-0.0-conf"

PACKAGE_ARCH_tslib-conf = "${MACHINE_ARCH}"
PACKAGE_ARCH_mnci = "${MACHINE_ARCH}"

PACKAGES =+ "tslib-conf libts-dev tslib-tests tslib-calibrate"
DEBIAN_NOAUTONAME_tslib-conf = "1"
DEBIAN_NOAUTONAME_tslib-tests = "1"
DEBIAN_NOAUTONAME_tslib-calibrate = "1"

RDEPENDS_${PN} = "tslib-conf"


FILES_${PN}-dbg += "${libdir}/ts/.debug*"
FILES_tslib-conf = "${sysconfdir}/ts.conf ${sysconfdir}/profile.d/tslib.sh ${datadir}/tslib"
FILES_${PN} = "${libdir}/*.so.* ${libdir}/ts/*.so*"
FILES_libts-dev = "${FILES_tslib-dev}"
FILES_tslib-calibrate += "${bindir}/ts_calibrate"
FILES_tslib-tests = "${bindir}/ts_harvest ${bindir}/ts_print ${bindir}/ts_print_raw ${bindir}/ts_test"
