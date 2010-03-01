PACKAGES = "\
  ${PN} ${PN}-symlinks \
  g++ g++-symlinks \
  cpp cpp-symlinks \
  g77 g77-symlinks \
  gfortran gfortran-symlinks \
  gcov gcov-symlinks \
  libmudflap libmudflap-dev \
  libgcc-dev \
  libstdc++-dev \
  libg2c-dev \
  libgfortran-dev \
  ${PN}-doc \
"

FILES_${PN} = "\
  ${bindir}/${TARGET_PREFIX}gcc \
  ${bindir}/${TARGET_PREFIX}gccbug \
  ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/collect2 \
  ${gcclibdir}/${TARGET_SYS}/${BINV}/*.o \
  ${gcclibdir}/${TARGET_SYS}/${BINV}/specs \
  ${gcclibdir}/${TARGET_SYS}/${BINV}/lib* \
  ${gcclibdir}/${TARGET_SYS}/${BINV}/include \
  ${gcclibdir}/${TARGET_SYS}/${BINV}/include-fixed \
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

FILES_libgcc = "${base_libdir}/libgcc*.so.*"
FILES_libgcc-dev = "${base_libdir}/libgcc*.so"

# Called from within gcc-cross, so libdir is set wrong
FILES_libg2c = "${target_libdir}/libg2c.so.*"
FILES_libg2c-dev = "\
  ${libdir}/libg2c.so \
  ${libdir}/libg2c.a \
  ${libdir}/libfrtbegin.a \
"

FILES_g++ = "\
  ${bindir}/${TARGET_PREFIX}g++ \
  ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/cc1plus \
"
FILES_g++-symlinks = "\
  ${bindir}/c++ \
  ${bindir}/g++ \
"

FILES_libstdc++ = "${libdir}/libstdc++.so.*"
FILES_libstdc++-dev = "\
  ${includedir}/c++/${BINV} \
  ${libdir}/libstdc++.so \
  ${libdir}/libstdc++.la \
  ${libdir}/libstdc++.a \
  ${libdir}/libsupc++.la \
  ${libdir}/libsupc++.a \
"

FILES_libgfortran-dev = "${libdir}/libgfortran.a \
                         ${libdir}/libgfortran.so \
                         ${libdir}/libgfortranbegin.a"

FILES_libmudflap = "${libdir}/libmudflap*.so.*"
FILES_libmudflap-dev = "\
  ${libdir}/libmudflap*.so \
  ${libdir}/libmudflap*.a \
  ${libdir}/libmudflap*.a \
"

FILES_${PN}-doc = "\
  ${infodir} \
  ${mandir} \
  ${gcclibdir}/${TARGET_SYS}/${BINV}/include/README \
"

do_install () {
	autotools_do_install

	# Cleanup some of the ${libdir}{,exec}/gcc stuff ...
	rm -r ${D}${libdir}/gcc/${TARGET_SYS}/${BINV}/install-tools
	rm -r ${D}${libexecdir}/gcc/${TARGET_SYS}/${BINV}/install-tools

	# Hack around specs file assumptions
	test -f ${D}${libdir}/gcc/${TARGET_SYS}/${BINV}/specs && sed -i -e '/^*cross_compile:$/ { n; s/1/0/; }' ${D}${libdir}/gcc/${TARGET_SYS}/${BINV}/specs

	# Move libgcc_s into /lib
	mkdir -p ${D}${base_libdir}
	if [ -f ${D}${libdir}/nof/libgcc_s.so ]; then
		mv ${D}${libdir}/nof/libgcc_s.so.* ${D}${base_libdir}
	else
		mv ${D}${libdir}/libgcc_s.so.* ${D}${base_libdir} || true
	fi
	rm -f ${D}${libdir}/libgcc_s.so || true 
	ln -sf `echo ${libdir}/gcc/${TARGET_SYS}/${BINV} \
		| tr -s / \
		| sed -e 's,^/,,' -e 's,[^/]*,..,g'`/lib/libgcc_s.so.1 \
		      ${D}${libdir}/gcc/${TARGET_SYS}/${BINV}/libgcc_s.so

	# We don't need libtool libraries
	rm -f ${D}${libdir}/libg2c.la &>/dev/null || true

	# Cleanup manpages..
	rm -rf ${D}${mandir}/man7

	# We use libiberty from binutils
	rm -f ${D}${libdir}/libiberty.a

	cd ${D}${bindir}

	# We care about g++ not c++
	rm -f *c++

	# We don't care about the gcc-<version> ones for this
	rm -f *gcc-?.?*

	# These sometimes show up, they are strange, we remove them
	rm -f ${TARGET_ARCH}-*${TARGET_ARCH}-*

	# Symlinks so we can use these trivially on the target
	ln -sf ${TARGET_SYS}-g77 g77 || true
	ln -sf ${TARGET_SYS}-gfortran gfortran || true
	ln -sf ${TARGET_SYS}-g++ g++
	ln -sf ${TARGET_SYS}-gcc gcc
	ln -sf g77 f77 || true
	ln -sf gfortran f95 || true
	ln -sf g++ c++
	ln -sf gcc cc
	ln -sf ${bindir}/${TARGET_SYS}-cpp ${D}${base_libdir}/cpp
	ln -sf ${bindir}/${TARGET_SYS}-cpp ${D}${bindir}/cpp

	# Remove precompiled c++ headers as they are really big
	rm -rf ${D}${includedir}/c++/${BINV}/${TARGET_SYS}/bits/*.gch
}
