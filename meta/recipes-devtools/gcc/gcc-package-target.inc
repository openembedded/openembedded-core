PACKAGES = "\
  ${PN} ${PN}-symlinks \
  g++ g++-symlinks \
  cpp cpp-symlinks \
  g77 g77-symlinks \
  gfortran gfortran-symlinks \
  gcov gcov-symlinks \
  ${PN}-doc \
  ${PN}-dev \
  ${PN}-dbg \
"

FILES_${PN} = "\
  ${bindir}/${TARGET_PREFIX}gcc \
  ${bindir}/${TARGET_PREFIX}gccbug \
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
"
FILES_${PN}-dev = "\
  ${gcclibdir}/${TARGET_SYS}/${BINV}/plugin/include/ \
  ${gcclibdir}/${TARGET_SYS}/${BINV}/lib*${SOLIBSDEV} \
  ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/lib*${SOLIBSDEV} \
"
FILES_${PN}-symlinks = "\
  ${bindir}/cc \
  ${bindir}/gcc \
  ${bindir}/gccbug \
"

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

do_install () {
	oe_runmake 'DESTDIR=${D}' install-host

	# Cleanup some of the ${libdir}{,exec}/gcc stuff ...
	rm -r ${D}${libdir}/gcc/${TARGET_SYS}/${BINV}/install-tools
	rm -r ${D}${libexecdir}/gcc/${TARGET_SYS}/${BINV}/install-tools
	rm -rf ${D}${libexecdir}/gcc/${TARGET_SYS}/${BINV}/*.la

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
	find ${D}${exec_prefix}/lib -name libiberty.a | xargs rm -f
	find ${D}${exec_prefix}/lib -name libiberty.h | xargs rm -f

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
	ln -sf g++ c++
	ln -sf gcc cc

	chown -R root:root ${D}
}
