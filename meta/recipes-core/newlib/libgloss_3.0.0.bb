require newlib.inc

DEPENDS += "newlib"

FILESEXTRAPATHS_prepend := "${THISDIR}/libgloss:"

SRC_URI_append_powerpc = " file://fix-rs6000-crt0.patch"
SRC_URI_append_arm = " file://fix_makefile_include_arm_h.patch"

do_configure() {
	${S}/libgloss/configure ${EXTRA_OECONF}
}

do_install_prepend() {
	# install doesn't create this itself, avoid install error
	install -d ${D}${prefix}/${TARGET_SYS}/lib
}

do_install_append() {
	# Move libs to default directories so they can be picked up later
	mv -v ${D}${prefix}/${TARGET_SYS}/lib ${D}${libdir}

	# Remove original directory
	rmdir ${D}${prefix}/${TARGET_SYS}
}

# Split packages correctly
FILES_${PN} += "${libdir}/*.ld ${libdir}/*.specs"
FILES_${PN}-dev += "${libdir}/cpu-init/*"

INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
