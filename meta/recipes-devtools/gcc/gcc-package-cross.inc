INHIBIT_PACKAGE_STRIP = "1"

# Compute how to get from libexecdir to bindir in python (easier than shell)
BINRELPATH = "${@oe.path.relative(d.expand("${libexecdir}/gcc/${TARGET_SYS}/${BINV}"), d.expand("${STAGING_DIR_NATIVE}${prefix_native}/bin/${MULTIMACH_TARGET_SYS}"))}"

do_install () {
	oe_runmake 'DESTDIR=${D}' install-host

	install -d ${D}${target_base_libdir}
	install -d ${D}${target_libdir}
    
	# Link gfortran to g77 to satisfy not-so-smart configure or hard coded g77
	# gfortran is fully backwards compatible. This is a safe and practical solution. 
	ln -sf ${STAGING_DIR_NATIVE}${prefix_native}/bin/${TARGET_PREFIX}gfortran ${STAGING_DIR_NATIVE}${prefix_native}/bin/${TARGET_PREFIX}g77 || true

	
	# Insert symlinks into libexec so when tools without a prefix are searched for, the correct ones are
	# found. These need to be relative paths so they work in different locations.
	dest=${D}${libexecdir}/gcc/${TARGET_SYS}/${BINV}/
	install -d $dest
	for t in ar as ld nm objcopy objdump ranlib strip g77 gcc cpp gfortran; do
		ln -sf ${BINRELPATH}/${TARGET_PREFIX}$t $dest$t
		ln -sf ${BINRELPATH}/${TARGET_PREFIX}$t ${dest}${TARGET_PREFIX}$t
	done

	# Remove things we don't need but keep share/java
	for d in info man share/doc share/locale share/man share/info; do
		rm -rf ${D}${STAGING_DIR_NATIVE}${prefix_native}/$d
	done

	# We use libiberty from binutils
	find ${D}${exec_prefix}/lib -name libiberty.a | xargs rm -f
	find ${D}${exec_prefix}/lib -name libiberty.h | xargs rm -f

	# gcc-runtime installs libgcc into a special location in staging since it breaks doing a standalone build
	case ${PN} in
		*gcc-cross|*gcc-crosssdk)
			dest=${D}/${includedir}/gcc-build-internal-${MULTIMACH_TARGET_SYS}
			cp -fpPR . $dest
		;;
	esac
}

do_package[noexec] = "1"
do_packagedata[noexec] = "1"
do_package_write_ipk[noexec] = "1"
do_package_write_rpm[noexec] = "1"
do_package_write_deb[noexec] = "1"
