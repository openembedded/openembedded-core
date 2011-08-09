DESCRIPTION = "An Embeddable SQL Database Engine"
HOMEPAGE = "http://www.sqlite.org"
SECTION = "libs"
DEPENDS = "readline ncurses"
DEPENDS_virtclass-native = ""
LICENSE = "PD"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-tcl --enable-shared --enable-threadsafe"
EXTRA_OECONF_virtclass-native = "--disable-tcl --enable-shared --enable-threadsafe --disable-readline --enable-tempstore"
export config_BUILD_CC = "${BUILD_CC}"
export config_BUILD_CFLAGS = "${BUILD_CFLAGS}"
export config_BUILD_LIBS = "${BUILD_LDFLAGS}"
export config_TARGET_CC = "${CC}"
export config_TARGET_LINK = "${CCLD}"
export config_TARGET_CFLAGS = "${CFLAGS}"
export config_TARGET_LFLAGS = "${LDFLAGS}"

PKGSUFFIX = ""
PKGSUFFIX_virtclass-nativesdk = "-nativesdk"

PACKAGES = "lib${BPN}${PKGSUFFIX} lib${BPN}${PKGSUFFIX}-dev lib${BPN}${PKGSUFFIX}-doc ${PN} ${PN}-dbg"
FILES_${PN} = "${bindir}/*"
FILES_lib${BPN}${PKGSUFFIX} = "${libdir}/*.so.*"
FILES_lib${BPN}${PKGSUFFIX}-dev = "${libdir}/*.a ${libdir}/*.la ${libdir}/*.so \
	                	   ${libdir}/pkgconfig ${includedir}"
FILES_lib${BPN}${PKGSUFFIX}-doc = "${docdir} ${mandir} ${infodir}"
AUTO_LIBNAME_PKGS = "lib${BPN}${PKGSUFFIX}"

BBCLASSEXTEND = "native nativesdk"
