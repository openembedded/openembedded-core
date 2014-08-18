require libtool-${PV}.inc

PR = "${INC_PR}.1"
PACKAGES = ""
SRC_URI += "file://prefix.patch"
SRC_URI += "file://fixinstall.patch"

datadir = "${STAGING_DIR_TARGET}${target_datadir}"

do_configure_prepend () {
	# Remove any existing libtool m4 since old stale versions would break
	# any upgrade
	rm -f ${STAGING_DATADIR}/aclocal/libtool.m4
	rm -f ${STAGING_DATADIR}/aclocal/lt*.m4
}

do_install () {
	install -d ${D}${bindir_crossscripts}/
	install -m 0755 ${HOST_SYS}-libtool ${D}${bindir_crossscripts}/${HOST_SYS}-libtool
	install -d ${D}${bindir_crossscripts}/
	install -m 0755 libtoolize ${D}${bindir_crossscripts}/
	install -d ${D}${target_datadir}/libtool/config/
	install -d ${D}${target_datadir}/aclocal/
	install -c ${S}/libltdl/config/compile ${D}${target_datadir}/libtool/config/
	install -c ${S}/libltdl/config/config.guess ${D}${target_datadir}/libtool/config/
	install -c ${S}/libltdl/config/config.sub ${D}${target_datadir}/libtool/config/
	install -c ${S}/libltdl/config/depcomp ${D}${target_datadir}/libtool/config/
	install -c ${S}/libltdl/config/install-sh ${D}${target_datadir}/libtool/config/
	install -c ${S}/libltdl/config/missing ${D}${target_datadir}/libtool/config/
	install -c -m 0644 ${S}/libltdl/config/ltmain.sh ${D}${target_datadir}/libtool/config/
	install -c -m 0644 ${S}/libltdl/m4/*.m4 ${D}${target_datadir}/aclocal/
}

SYSROOT_PREPROCESS_FUNCS += "libtoolcross_sysroot_preprocess"

libtoolcross_sysroot_preprocess () {
	sysroot_stage_dir ${D}${bindir_crossscripts} ${SYSROOT_DESTDIR}${bindir_crossscripts}
	sysroot_stage_dir ${D}${target_datadir} ${SYSROOT_DESTDIR}${target_datadir}
}

SSTATE_SCAN_FILES += "libtoolize *-libtool"
