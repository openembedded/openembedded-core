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

PACKAGES = "lib${BPN} lib${BPN}-dev lib${BPN}-doc ${PN} ${PN}-dbg"
FILES_${PN} = "${bindir}/*"
FILES_lib${PN} = "${libdir}/*.so.*"
FILES_lib${PN}-dev = "${libdir}/*.a ${libdir}/*.la ${libdir}/*.so \
		       ${libdir}/pkgconfig ${includedir}"
FILES_lib${BPN}-doc = "${docdir} ${mandir} ${infodir}"
AUTO_LIBNAME_PKGS = "lib${BPN}"

BBCLASSEXTEND = "native nativesdk"
