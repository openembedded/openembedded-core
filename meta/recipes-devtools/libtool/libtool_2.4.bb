require libtool.inc
DEPENDS = "libtool-native"

PR = "r1"

PACKAGES =+ "libltdl libltdl-dev libltdl-dbg"
FILES_${PN} += "${datadir}/aclocal*"
FILES_libltdl = "${libdir}/libltdl.so.*"
FILES_libltdl-dev = "${libdir}/libltdl.* ${includedir}/ltdl.h ${includedir}/libltdl/* ${datadir}/aclocal/ltdl.m4 ${datadir}/libtool/libltdl/*"
FILES_libltdl-dbg = "${libdir}/.debug/"

#
# We want the results of libtool-cross preserved - don't stage anything ourselves.
#
SYSROOT_PREPROCESS_FUNCS += "libtool_sysroot_preprocess"

libtool_sysroot_preprocess () {
	if [ "${PN}" == "libtool" ]; then
		rm -rf ${SYSROOT_DESTDIR}${STAGING_DIR_TARGET}${bindir}/*
		rm -rf ${SYSROOT_DESTDIR}${STAGING_DIR_TARGET}${datadir}/aclocal/*
		rm -rf ${SYSROOT_DESTDIR}${STAGING_DIR_TARGET}${datadir}/libtool/config/*
	fi
}

SRC_URI[md5sum] = "b32b04148ecdd7344abc6fe8bd1bb021"
SRC_URI[sha256sum] = "13df57ab63a94e196c5d6e95d64e53262834fe780d5e82c28f177f9f71ddf62e"
