SUMMARY = "An abstraction layer for touchscreen panel events."
DESCRIPTION = "Tslib is an abstraction layer for touchscreen panel \
events, as well as a filter stack for the manipulation of those events. \
Tslib is generally used on embedded devices to provide a common user \
space interface to touchscreen functionality."
HOMEPAGE = "http://tslib.berlios.de/"

AUTHOR = "Russell King w/ plugins by Chris Larson et. al."
SECTION = "base"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=f30a9716ef3762e3467a2f62bf790f0a"

PR = "r16"

SRC_URI = "http://download.berlios.de/tslib/tslib-${PV}.tar.bz2 \
           file://fix_version.patch;patch=1 \
           file://ts.conf \
           file://ts.conf-simpad-2.4 \
           file://ts.conf-collie-2.4 \
           file://tslib.sh"

SRC_URI[md5sum] = "92b2eb55b1e4ef7e2c0347069389390e"
SRC_URI[sha256sum] = "9c40d914e4f6fe00bdd77137d671c7ce4f211686228f2eb8b2d3c2360bc249c8"
SRC_URI_append_mnci += " file://devfs.patch;patch=1"
SRC_URI_append_mnci += " file://event1.patch;patch=1"
SRC_URI_append_qemumips += " file://32bitBE-support.patch;patch=1"


inherit autotools pkgconfig

EXTRA_OECONF = "--enable-shared --disable-h3600 --enable-input --disable-corgi --disable-collie --disable-mk712 --disable-arctic2 --disable-ucb1x00"

do_install_prepend() {
	install -m 0644 ${WORKDIR}/ts.conf ${S}/etc/ts.conf
}

do_install_append() {
	install -d ${D}${sysconfdir}/profile.d/
	install -m 0755 ${WORKDIR}/tslib.sh ${D}${sysconfdir}/profile.d/
	case ${MACHINE} in
	collie )
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
#RDEPENDS_tslib-conf_weird-machine = "detect-stylus"
RPROVIDES_tslib-conf = "libts-0.0-conf"

# Machines with machine specific patches
PACKAGE_ARCH_mnci = "${MACHINE_ARCH}"
# Machines with machine specific config files (tslib.sh)
PACKAGE_ARCH_tslib-conf_a780 = "${MACHINE_ARCH}"
PACKAGE_ARCH_tslib-conf_collie = "${MACHINE_ARCH}"
PACKAGE_ARCH_tslib-conf_e680 = "${MACHINE_ARCH}"
PACKAGE_ARCH_tslib-conf_jornada56x = "${MACHINE_ARCH}"
PACKAGE_ARCH_tslib-conf_jornada6xx = "${MACHINE_ARCH}"
PACKAGE_ARCH_tslib-conf_jornada7xx = "${MACHINE_ARCH}"
PACKAGE_ARCH_tslib-conf_netbook-pro = "${MACHINE_ARCH}"
PACKAGE_ARCH_tslib-conf_omap1610h2 = "${MACHINE_ARCH}"
PACKAGE_ARCH_tslib-conf_omap5912osk = "${MACHINE_ARCH}"
PACKAGE_ARCH_tslib-conf_simpad = "${MACHINE_ARCH}"

PACKAGES =+ "tslib-conf tslib-tests tslib-calibrate"
DEBIAN_NOAUTONAME_tslib-conf = "1"
DEBIAN_NOAUTONAME_tslib-tests = "1"
DEBIAN_NOAUTONAME_tslib-calibrate = "1"

RDEPENDS_${PN} = "tslib-conf"
RRECOMMENDS_${PN} = "pointercal"

FILES_${PN}-dbg += "${libdir}/ts/.debug*"
FILES_tslib-conf = "${sysconfdir}/ts.conf ${sysconfdir}/profile.d/tslib.sh ${datadir}/tslib"
FILES_${PN} = "${libdir}/*.so.* ${libdir}/ts/*.so*"
FILES_tslib-calibrate += "${bindir}/ts_calibrate"
FILES_tslib-tests = "${bindir}/ts_harvest ${bindir}/ts_print ${bindir}/ts_print_raw ${bindir}/ts_test"
