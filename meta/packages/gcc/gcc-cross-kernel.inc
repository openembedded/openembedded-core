# Cut-down gcc for kernel builds
# Only installs ${TARGET_PREFIX}gcc-${PV}, not ${TARGET_PREFIX}gcc.

PROVIDES = "virtual/${TARGET_PREFIX}gcc-${PV}"

do_install () {
	:
}

do_stage () {
	cd gcc
	oe_runmake install-common install-headers install-libgcc
	install -m 0755 xgcc ${CROSS_DIR}/bin/${TARGET_PREFIX}gcc-${PV}
}
