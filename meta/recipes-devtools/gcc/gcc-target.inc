GCCMULTILIB = "--enable-multilib"
require gcc-configure-common.inc

EXTRA_OECONF_PATHS = "\
    --with-sysroot=/ \
    --with-build-sysroot=${STAGING_DIR_TARGET} \
    --with-native-system-header-dir=${STAGING_DIR_TARGET}${target_includedir} \
    --with-gxx-include-dir=${includedir}/c++/${BINV} \
"

EXTRA_OECONF_append_linuxstdbase = " --enable-clocale=gnu"

EXTRA_OECONF_GCC_FLOAT = "${@get_gcc_float_setting(bb, d)}"

PACKAGES = "\
    ${PN} ${PN}-plugins ${PN}-symlinks \
    g++ g++-symlinks \
    cpp cpp-symlinks \
    g77 g77-symlinks \
    gfortran gfortran-symlinks \
    gcov gcov-symlinks \
    ${PN}-plugin-dev \
    ${PN}-doc \
    ${PN}-dev \
    ${PN}-dbg \
"

FILES_${PN} = "\
    ${bindir}/${TARGET_PREFIX}gcc* \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/collect2 \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/cc* \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/lto* \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/lib*${SOLIBS} \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/liblto*${SOLIBSDEV} \
    ${gcclibdir}/${TARGET_SYS}/${BINV}/*.o \
    ${gcclibdir}/${TARGET_SYS}/${BINV}/specs \
    ${gcclibdir}/${TARGET_SYS}/${BINV}/lib*${SOLIBS} \
    ${gcclibdir}/${TARGET_SYS}/${BINV}/include \
    ${gcclibdir}/${TARGET_SYS}/${BINV}/include-fixed \
"
INSANE_SKIP_${PN} += "dev-so"

FILES_${PN}-dbg += "\
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/.debug/ \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/plugin/.debug/ \
"
FILES_${PN}-dev = "\
    ${gcclibdir}/${TARGET_SYS}/${BINV}/lib*${SOLIBSDEV} \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/lib*${SOLIBSDEV} \
"
FILES_${PN}-plugin-dev = "\
    ${gcclibdir}/${TARGET_SYS}/${BINV}/plugin/include/ \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/plugin/gengtype \
    ${gcclibdir}/${TARGET_SYS}/${BINV}/plugin/gtype.state \
"
FILES_${PN}-symlinks = "\
    ${bindir}/cc \
    ${bindir}/gcc \
    ${bindir}/gccbug \
"

FILES_${PN}-plugins = "\
    ${gcclibdir}/${TARGET_SYS}/${BINV}/plugin \
"
ALLOW_EMPTY_${PN}-plugins = "1"

FILES_g77 = "\
    ${bindir}/${TARGET_PREFIX}g77 \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/f771 \
"
FILES_g77-symlinks = "\
    ${bindir}/g77 \
    ${bindir}/f77 \
"
FILES_gfortran = "\
    ${bindir}/${TARGET_PREFIX}gfortran \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/f951 \
"
FILES_gfortran-symlinks = "\
    ${bindir}/gfortran \
    ${bindir}/f95"

FILES_cpp = "\
    ${bindir}/${TARGET_PREFIX}cpp \
    ${base_libdir}/cpp \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/cc1"
FILES_cpp-symlinks = "${bindir}/cpp"

FILES_gcov = "${bindir}/${TARGET_PREFIX}gcov"
FILES_gcov-symlinks = "${bindir}/gcov"

FILES_g++ = "\
    ${bindir}/${TARGET_PREFIX}g++ \
    ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/cc1plus \
"
FILES_g++-symlinks = "\
    ${bindir}/c++ \
    ${bindir}/g++ \
"

FILES_${PN}-doc = "\
    ${infodir} \
    ${mandir} \
    ${gcclibdir}/${TARGET_SYS}/${BINV}/include/README \
"

do_compile () {
	oe_runmake all-host
}

do_install () {
	oe_runmake 'DESTDIR=${D}' install-host

	# Add unwind.h, it comes from libgcc which we don't want to build again
	install ${STAGING_LIBDIR_NATIVE}/${TARGET_SYS}/gcc/${TARGET_SYS}/${BINV}/include/unwind.h ${D}${libdir}/gcc/${TARGET_SYS}/${BINV}/include/

	# Info dir listing isn't interesting at this point so remove it if it exists.
	if [ -e "${D}${infodir}/dir" ]; then
		rm -f ${D}${infodir}/dir
	fi

	# Cleanup some of the ${libdir}{,exec}/gcc stuff ...
	rm -r ${D}${libdir}/gcc/${TARGET_SYS}/${BINV}/install-tools
	rm -r ${D}${libexecdir}/gcc/${TARGET_SYS}/${BINV}/install-tools
	rm -rf ${D}${libexecdir}/gcc/${TARGET_SYS}/${BINV}/*.la
	rmdir ${D}${includedir}
	rm -rf ${D}${libdir}/gcc/${TARGET_SYS}/${BINV}/finclude

	# Hack around specs file assumptions
	test -f ${D}${libdir}/gcc/${TARGET_SYS}/${BINV}/specs && sed -i -e '/^*cross_compile:$/ { n; s/1/0/; }' ${D}${libdir}/gcc/${TARGET_SYS}/${BINV}/specs

	# Cleanup manpages..
	rm -rf ${D}${mandir}/man7

	cd ${D}${bindir}

	# We care about g++ not c++
	rm -f *c++

	# We don't care about the gcc-<version> ones for this
	rm -f *gcc-?.?*

	# We use libiberty from binutils
	find ${D}${libdir} -name libiberty.a | xargs rm -f
	find ${D}${libdir} -name libiberty.h | xargs rm -f

	# Not sure why we end up with these but we don't want them...
	rm -f ${TARGET_PREFIX}${TARGET_PREFIX}*

	# Symlinks so we can use these trivially on the target
	if [ -e ${TARGET_PREFIX}g77 ]; then
		ln -sf ${TARGET_PREFIX}g77 g77 || true
		ln -sf g77 f77 || true
	fi
	if [ -e ${TARGET_PREFIX}gfortran ]; then
		ln -sf ${TARGET_PREFIX}gfortran gfortran || true
		ln -sf gfortran f95 || true
	fi
	ln -sf ${TARGET_PREFIX}g++ g++
	ln -sf ${TARGET_PREFIX}gcc gcc
	ln -sf ${TARGET_PREFIX}cpp cpp
	install -d ${D}${base_libdir}
	ln -sf ${bindir}/${TARGET_PREFIX}cpp ${D}${base_libdir}/cpp
	ln -sf g++ c++
	ln -sf gcc cc

	chown -R root:root ${D}
}

# Installing /usr/lib/gcc/* means we'd have two copies, one from gcc-cross
# and one from here. These can confuse gcc cross where includes use #include_next
# and builds track file dependencies (e.g. perl and its makedepends code).
# For determinism we don't install this ever and rely on the copy from gcc-cross.
# [YOCTO #7287]
sysroot_stage_dirs_append () {
	rm -rf $to${libdir}/gcc
}
