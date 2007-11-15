DESCRIPTION = "Python Programming Language"
HOMEPAGE = "http://www.python.org"
LICENSE = "PSF"
SECTION = "devel/python"
PRIORITY = "optional"
DEPENDS = "python-native readline zlib gdbm openssl sqlite3"
DEPENDS_sharprom = "python-native readline zlib gdbm openssl"

# NOTE: Keep the digit in sync with BASEREV in contrib/generate-manifest-2.5.py
PR = "ml1"

PYTHON_MAJMIN = "2.5"

SRC_URI = "http://www.python.org/ftp/python/${PV}/Python-${PV}.tar.bz2 \
           file://bindir-libdir.patch;patch=1 \
           file://crosscompile.patch;patch=1 \
           file://fix-tkinter-detection.patch;patch=1 \
           file://autohell.patch;patch=1 \
           file://sitebranding.patch;patch=1 \
           file://default-is-optimized.patch;patch=1"
S = "${WORKDIR}/Python-${PV}"

inherit autotools

EXTRA_OECONF = "--with-threads --with-pymalloc --with-cyclic-gc \
                --without-cxx --with-signal-module --with-wctype-functions \
                --enable-shared"

#
# copy config.h and an appropriate Makefile for distutils.sysconfig
# which laters uses the information out of these to compile extensions
#
do_compile_prepend() {
	install -d ${STAGING_INCDIR}/python${PYTHON_MAJMIN}/
	install -d ${STAGING_LIBDIR}/python${PYTHON_MAJMIN}/config/
	install -m 0644 pyconfig.h ${STAGING_INCDIR}/python${PYTHON_MAJMIN}/
	install -m 0644 Makefile Makefile.orig
	install -m 0644 Makefile Makefile.backup
	sed -e 's,${includedir},${STAGING_INCDIR},' < Makefile.backup > Makefile
	install -m 0644 Makefile Makefile.backup
	sed -e 's,${libdir},${STAGING_LIBDIR},' < Makefile.backup > Makefile
	install -m 0644 Makefile ${STAGING_LIBDIR}/python${PYTHON_MAJMIN}/config/
}

do_compile() {
        oe_runmake HOSTPGEN=${STAGING_BINDIR_NATIVE}/pgen \
                   HOSTPYTHON=${STAGING_BINDIR_NATIVE}/python \
                   STAGING_LIBDIR=${STAGING_LIBDIR} \
                   STAGING_INCDIR=${STAGING_INCDIR} \
                   BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
                   OPT="${CFLAGS}" libpython2.5.so

	oe_libinstall -so libpython2.5 ${STAGING_LIBDIR}

	oe_runmake HOSTPGEN=${STAGING_BINDIR_NATIVE}/pgen \
		   HOSTPYTHON=${STAGING_BINDIR_NATIVE}/python \
		   STAGING_LIBDIR=${STAGING_LIBDIR} \
		   STAGING_INCDIR=${STAGING_INCDIR} \
		   BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
		   OPT="${CFLAGS}"
}

do_stage() {
	install -m 0644 Include/*.h ${STAGING_INCDIR}/python${PYTHON_MAJMIN}/
	oe_libinstall -a -so libpython${PYTHON_MAJMIN} ${STAGING_LIBDIR}
}

do_install() {
	install -m 0644 Makefile.orig Makefile
        oe_runmake HOSTPGEN=${STAGING_BINDIR_NATIVE}/pgen \
                   HOSTPYTHON=${STAGING_BINDIR_NATIVE}/python \
                   STAGING_LIBDIR=${STAGING_LIBDIR} \
                   STAGING_INCDIR=${STAGING_INCDIR} \
		   BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
		   DESTDIR=${D} LIBDIR=${libdir} install
}

require python-${PYTHON_MAJMIN}-manifest.inc

RPROVIDES_python-core = "python"
RPROVIDES_python-curses = "python"

PACKAGES =+ "libpython2"
FILES_libpython2 = "${libdir}/libpython*"

# catch debug extensions
FILES_python-dbg += "${libdir}/python${PYTHON_MAJMIN}/lib-dynload/.debug"

# catch all the rest (unsorted)
PACKAGES += "python-misc"
FILES_python-misc = "${libdir}/python${PYTHON_MAJMIN}"

# catch manpage
PACKAGES += "python-man"
FILES_python-man = "${datadir}/man"

