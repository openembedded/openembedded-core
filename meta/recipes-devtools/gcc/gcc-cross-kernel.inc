# Cut-down gcc for kernel builds
# Only installs ${TARGET_PREFIX}gcc-${PV}, not ${TARGET_PREFIX}gcc.

PROVIDES = "virtual/${TARGET_PREFIX}gcc-${PV}"

do_install () {
	cd gcc
	oe_runmake 'DESTDIR=${D}' install-common install-headers install-libgcc
	install -m 0755 xgcc  ${D}${bindir}/${TARGET_PREFIX}gcc-${PV}

    # Insert symlinks into libexec so when tools without a prefix are searched for, the correct ones are
    # found. These need to be relative paths so they work in different locations.
    dest=${D}${libexecdir}/gcc/${TARGET_SYS}/${BINV}/
    install -d $dest
    for t in ar as ld nm objcopy objdump ranlib strip cpp; do
        ln -sf ${BINRELPATH}/${TARGET_PREFIX}$t $dest$t
        ln -sf ${BINRELPATH}/${TARGET_PREFIX}$t ${dest}${TARGET_PREFIX}$t
    done
}
