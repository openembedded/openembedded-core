gcclibdir ?= "${libdir}/gcc"
BINV ?= "${PV}"

# libgcc libstdc++ libg2c are listed in our FILES_*, but are actually
# packaged in the respective cross packages.
PACKAGES = "${PN} ${PN}-symlinks \
            g++ g++-symlinks \
	    cpp cpp-symlinks \
            g77 g77-symlinks \
	    gcov gcov-symlinks \
            libstdc++-dev libg2c-dev \
            ${PN}-doc"

FILES_${PN} = "${bindir}/${TARGET_PREFIX}gcc \
	       ${bindir}/${TARGET_PREFIX}gccbug \
	       ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/cc1 \
	       ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/collect2 \
	       ${gcclibdir}/${TARGET_SYS}/${BINV}/*.o \
	       ${gcclibdir}/${TARGET_SYS}/${BINV}/specs \
	       ${gcclibdir}/${TARGET_SYS}/${BINV}/lib* \
	       ${gcclibdir}/${TARGET_SYS}/${BINV}/include"
FILES_${PN}-symlinks = "${bindir}/cc \
			${bindir}/gcc \
			${bindir}/gccbug"

FILES_g77 = "${bindir}/${TARGET_PREFIX}g77 \
		   ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/f771"
FILES_g77-symlinks = "${bindir}/g77 \
			    ${bindir}/f77"

FILES_cpp = "${bindir}/${TARGET_PREFIX}cpp \
	     ${base_libdir}/cpp"
FILES_cpp-symlinks = "${bindir}/cpp"

FILES_gcov = "${bindir}/${TARGET_PREFIX}gcov"
FILES_gcov-symlinks = "${bindir}/gcov"

PACKAGE_ARCH_libg2c-dev = "${TARGET_ARCH}"
# Called from within gcc-cross, so libdir is set wrong
FILES_libg2c-dev = "${libdir}/libg2c.so \
		    ${libdir}/libg2c.a \
		    ${libdir}/libfrtbegin.a"

FILES_g++ = "${bindir}/${TARGET_PREFIX}g++ \
		   ${libexecdir}/gcc/${TARGET_SYS}/${BINV}/cc1plus"
FILES_g++-symlinks = "${bindir}/c++ \
			    ${bindir}/g++"

PACKAGE_ARCH_libstdc++-dev = "${TARGET_ARCH}"
FILES_libstdc++-dev = "${includedir}/c++/${BINV} \
		       ${libdir}/libstdc++.so \
		       ${libdir}/libstdc++.la \
		       ${libdir}/libstdc++.a \
		       ${libdir}/libsupc++.la \
		       ${libdir}/libsupc++.a"

FILES_${PN}-doc = "${infodir} \
		   ${mandir} \
		   ${gcclibdir}/${TARGET_SYS}/${BINV}/include/README"


do_install () {
	autotools_do_install

	# Cleanup some of the ${libdir}{,exec}/gcc stuff ...
	rm -r ${D}${libdir}/gcc/${TARGET_SYS}/${BINV}/install-tools
	rm -r ${D}${libexecdir}/gcc/${TARGET_SYS}/${BINV}/install-tools

	# Hack around specs file assumptions
	sed -i -e '/^*cross_compile:$/ { n; s/1/0/; }' ${D}${libdir}/gcc/${TARGET_SYS}/${BINV}/specs

	# Move libgcc_s into /lib
	mkdir -p ${D}${base_libdir}
	mv ${D}${libdir}/libgcc_s.so.* ${D}${base_libdir}
	rm ${D}${libdir}/libgcc_s.so
	ln -sf `echo ${libdir}/gcc/${TARGET_SYS}/${BINV} \
		| tr -s / \
		| sed -e 's,^/,,' -e 's,[^/]*,..,g'`/lib/libgcc_s.so.1 \
		      ${D}${libdir}/gcc/${TARGET_SYS}/${BINV}/libgcc_s.so

	# We don't need libtool libraries
	rm ${D}${libdir}/libg2c.la

	# Cleanup manpages..
	rm -r ${D}${mandir}/man7

	# We use libiberty from binutils
	rm ${D}${libdir}/libiberty.a

	cd ${D}${bindir}

	# We care about g++ not c++
	rm *c++

	# We don't care about the gcc-<version> ones for this
	rm *gcc-?.?*

	# These sometimes show up, they are strange, we remove them
	rm -f ${TARGET_ARCH}-*${TARGET_ARCH}-*

	# Symlinks so we can use these trivially on the target
	ln -sf ${TARGET_SYS}-g77 g77
	ln -sf ${TARGET_SYS}-g++ g++
	ln -sf ${TARGET_SYS}-gcc gcc
	ln -sf g77 f77
	ln -sf g++ c++
	ln -sf gcc cc
	ln -sf ${bindir}/${TARGET_SYS}-cpp ${D}${base_libdir}/cpp
	ln -sf ${bindir}/${TARGET_SYS}-cpp ${D}${bindir}/cpp
}
