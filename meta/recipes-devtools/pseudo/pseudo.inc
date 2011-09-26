# Note: Due to the bitbake wrapper script, making changes to pseudo can be
# difficult.  To work around the current version of the wrapper use:
# BBFETCH2=True PSEUDO_BUILD=1 ../bitbake/bin/bitbake pseudo-native [-c CMD]

DESCRIPTION = "Pseudo gives fake root capabilities to a normal user"
HOMEPAGE = "http://wiki.github.com/wrpseudo/pseudo/"
LIC_FILES_CHKSUM = "file://COPYING;md5=243b725d71bb5df4a1e5920b344b86ad"
SECTION = "base"
LICENSE = "LGPL2.1"
DEPENDS = "sqlite3"

FILES_${PN} = "${libdir}/libpseudo.so ${bindir}/* ${localstatedir}/pseudo"
FILES_${PN}-dbg += "${libdir}/pseudo/lib*/.debug"
PROVIDES += "virtual/fakeroot"

# In the nativesdk case, we'll already search the searchpaths
# pseudo tries to build in so override RPATH
MAKEOPTS = ""
MAKEOPTS_virtclass-nativesdk = "'RPATH='"

inherit siteinfo

do_configure () {
	:
}

NO32LIBS ??= "0"

# Compile for the local machine arch...
do_compile () {
	if [ "${SITEINFO_BITS}" == "64" ]; then
	  ${S}/configure --prefix=${prefix} --libdir=${prefix}/lib/pseudo/lib${SITEINFO_BITS} --with-sqlite=${STAGING_DIR_TARGET}${exec_prefix} --bits=${SITEINFO_BITS}
	else
	  ${S}/configure --prefix=${prefix} --libdir=${prefix}/lib/pseudo/lib --with-sqlite=${STAGING_DIR_TARGET}${exec_prefix} --bits=${SITEINFO_BITS}
	fi
	oe_runmake ${MAKEOPTS}
}

# Two below are the same
# If necessary compile for the alternative machine arch.  This is only 
# necessary in a native build.
do_compile_prepend_virtclass-native () {
	if [ "${SITEINFO_BITS}" == "64" -a -e "/usr/include/gnu/stubs-32.h" -a "${PN}" == "pseudo-native" -a "${NO32LIBS}" != "1" ]; then
		# We need the 32-bit libpseudo on a 64-bit machine...
		./configure --prefix=${prefix} --libdir=${prefix}/lib/pseudo/lib --with-sqlite=${STAGING_DIR_TARGET}${exec_prefix} --bits=32
		oe_runmake ${MAKEOPTS} libpseudo
		# prevent it from removing the lib, but remove everything else
		make 'LIB=foo' ${MAKEOPTS} distclean 
	fi
}

do_compile_prepend_virtclass-nativesdk () {
	if [ "${SITEINFO_BITS}" == "64" -a -e "/usr/include/gnu/stubs-32.h" -a "${PN}" == "pseudo-native" -a "${NO32LIBS}" != "1" ]; then
		# We need the 32-bit libpseudo on a 64-bit machine...
		./configure --prefix=${prefix} --libdir=${prefix}/lib/pseudo/lib --with-sqlite=${STAGING_DIR_TARGET}${exec_prefix} --bits=32
		oe_runmake ${MAKEOPTS} libpseudo
		# prevent it from removing the lib, but remove everything else
		make 'LIB=foo' ${MAKEOPTS} distclean 
	fi
}

do_install () {
	oe_runmake 'DESTDIR=${D}' ${MAKEOPTS} 'LIB=lib/pseudo/lib$(MARK64)' install
}

# Two below are the same
# If necessary install for the alternative machine arch.  This is only 
# necessary in a native build.
do_install_append_virtclass-native () {
	if [ "${SITEINFO_BITS}" == "64" -a -e "/usr/include/gnu/stubs-32.h" -a "${PN}" == "pseudo-native" -a "${NO32LIBS}" != "1" ]; then
		mkdir -p ${D}${prefix}/lib/pseudo/lib
		cp lib/pseudo/lib/libpseudo.so ${D}${prefix}/lib/pseudo/lib/.
	fi
}

do_install_append_virtclass-nativesdk () {
	if [ "${SITEINFO_BITS}" == "64" -a -e "/usr/include/gnu/stubs-32.h" -a "${PN}" == "pseudo-native" -a "${NO32LIBS}" != "1" ]; then
		mkdir -p ${D}${prefix}/lib/pseudo/lib
		cp lib/pseudo/lib/libpseudo.so ${D}${prefix}/lib/pseudo/lib/.
	fi
}

BBCLASSEXTEND = "native nativesdk"
