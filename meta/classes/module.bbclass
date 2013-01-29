RDEPENDS_${PN} += "kernel-image"
DEPENDS += "virtual/kernel"

inherit module-base

addtask make_scripts after do_patch before do_compile
do_make_scripts[lockfiles] = "${TMPDIR}/kernel-scripts.lock"
do_make_scripts[deptask] = "do_populate_sysroot"

module_do_compile() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake KERNEL_PATH=${STAGING_KERNEL_DIR}   \
		   KERNEL_SRC=${STAGING_KERNEL_DIR}    \
		   KERNEL_VERSION=${KERNEL_VERSION}    \
		   CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
		   AR="${KERNEL_AR}" \
		   ${MAKE_TARGETS}
}

module_do_install() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake DEPMOD=echo INSTALL_MOD_PATH="${D}" \
	           KERNEL_SRC=${STAGING_KERNEL_DIR} \
	           CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
	           modules_install
}

pkg_postinst_${PN}_append () {
if [ -z "$D" ]; then
	depmod -a ${KERNEL_VERSION}
else
	depmod -a -b $D -F ${STAGING_KERNEL_DIR}/System.map-${KERNEL_VERSION} ${KERNEL_VERSION}
fi
}

pkg_postrm_${PN}_append () {
if [ -z "$D" ]; then
	depmod -a ${KERNEL_VERSION}
else
	depmod -a -b $D -F ${STAGING_KERNEL_DIR}/System.map-${KERNEL_VERSION} ${KERNEL_VERSION}
fi
}

EXPORT_FUNCTIONS do_compile do_install

FILES_${PN} = "/etc /lib/modules"
