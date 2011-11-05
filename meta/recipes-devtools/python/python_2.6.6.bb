require python.inc
DEPENDS = "python-native db gdbm openssl readline sqlite3 zlib"
DEPENDS_sharprom = "python-native db readline zlib gdbm openssl"
PR = "${INC_PR}.11"
LIC_FILES_CHKSUM = "file://LICENSE;md5=38fdd546420fab09ac6bd3d8a1c83eb6"

DISTRO_SRC_URI ?= "file://sitecustomize.py"
DISTRO_SRC_URI_linuxstdbase = ""
SRC_URI = "\
  http://www.python.org/ftp/python/${PV}/Python-${PV}.tar.bz2 \
  file://01-use-proper-tools-for-cross-build.patch \
  file://02-remove-test-for-cross.patch \
  file://03-fix-tkinter-detection.patch \
  file://04-default-is-optimized.patch \
  file://05-enable-ctypes-cross-build.patch \
  file://06-ctypes-libffi-fix-configure.patch \
  file://06-avoid_usr_lib_termcap_path_in_linking.patch \
  file://07-linux3-regen-fix.patch \
  file://99-ignore-optimization-flag.patch \
  ${DISTRO_SRC_URI} \
  file://multilib.patch \
  file://security_issue_2254_fix.patch \
  file://cgi_py.patch \
  file://remove_sqlite_rpath.patch \
  file://setup_py_skip_cross_import_check.patch \
"

SRC_URI[md5sum] = "cf4e6881bb84a7ce6089e4a307f71f14"
SRC_URI[sha256sum] = "134c5e0736bae2e5570d0b915693374f11108ded63c35a23a35d282737d2ce83"
S = "${WORKDIR}/Python-${PV}"

inherit autotools

# The 3 lines below are copied from the libffi recipe, ctypes ships its own copy of the libffi sources
#Somehow gcc doesn't set __SOFTFP__ when passing -mfloatabi=softp :(
TARGET_CC_ARCH_append_armv6 = " -D__SOFTFP__"
TARGET_CC_ARCH_append_armv7a = " -D__SOFTFP__"

do_configure_prepend() {
	autoreconf -Wcross --verbose --install --force --exclude=autopoint Modules/_ctypes/libffi || bbnote "_ctypes failed to autoreconf"
}

do_compile() {
	#
	# Copy config.h and an appropriate Makefile for distutils.sysconfig,
	# which laters uses the information out of these to compile extensions
	#
	# The following part (until python compilation) should probably moved to an
	# -initial recipe to handle staging better
	#
	install -d ${STAGING_INCDIR}/python${PYTHON_MAJMIN}/
	install -d ${STAGING_LIBDIR}/python${PYTHON_MAJMIN}/config/
	install -m 0644 pyconfig.h ${STAGING_INCDIR}/python${PYTHON_MAJMIN}/

	# remove hardcoded ccache, see http://bugs.openembedded.net/show_bug.cgi?id=4144
	sed -i -e s,ccache,'$(CCACHE)', Makefile

	install -m 0644 Makefile Makefile.orig
	sed -i -e 's,${includedir},${STAGING_INCDIR},' Makefile
	sed -i -e 's,${libdir},${STAGING_LIBDIR},' Makefile
	install -m 0644 Makefile ${STAGING_LIBDIR}/python${PYTHON_MAJMIN}/config/
	# save copy of it now, because if we do it in do_install and 
	# then call do_install twice we get Makefile.orig == Makefile.sysroot
	install -m 0644 Makefile Makefile.sysroot

	export CROSS_COMPILE="${TARGET_PREFIX}"

	oe_runmake HOSTPGEN=${STAGING_BINDIR_NATIVE}/pgen \
		HOSTPYTHON=${STAGING_BINDIR_NATIVE}/python \
		STAGING_LIBDIR=${STAGING_LIBDIR} \
		STAGING_INCDIR=${STAGING_INCDIR} \
		BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
		OPT="${CFLAGS}" libpython${PYTHON_MAJMIN}.so

	oe_libinstall -so libpython${PYTHON_MAJMIN} ${STAGING_LIBDIR}

	oe_runmake HOSTPGEN=${STAGING_BINDIR_NATIVE}/pgen \
		HOSTPYTHON=${STAGING_BINDIR_NATIVE}/python \
		STAGING_LIBDIR=${STAGING_LIBDIR} \
		STAGING_INCDIR=${STAGING_INCDIR} \
		BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
		OPT="${CFLAGS}"
}

do_install() {
	# make install needs the original Makefile, or otherwise the inclues would
	# go to ${D}${STAGING...}/...
	install -m 0644 Makefile.orig Makefile

	export CROSS_COMPILE="${TARGET_PREFIX}"
	
	oe_runmake HOSTPGEN=${STAGING_BINDIR_NATIVE}/pgen \
		HOSTPYTHON=${STAGING_BINDIR_NATIVE}/python \
		STAGING_LIBDIR=${STAGING_LIBDIR} \
		STAGING_INCDIR=${STAGING_INCDIR} \
		BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
		DESTDIR=${D} LIBDIR=${libdir} install

	install -m 0644 Makefile.sysroot ${D}/${libdir}/python${PYTHON_MAJMIN}/config/Makefile

	if [ -e ${WORKDIR}/sitecustomize.py ]; then
		install -m 0644 ${WORKDIR}/sitecustomize.py ${D}/${libdir}/python${PYTHON_MAJMIN}
	fi
}

PACKAGE_PREPROCESS_FUNCS += "py_package_preprocess"

py_package_preprocess () {
	# copy back the old Makefile to fix target package
	install -m 0644 Makefile.orig ${PKGD}/${libdir}/python${PYTHON_MAJMIN}/config/Makefile
}

require python-${PYTHON_MAJMIN}-manifest.inc

# manual dependency additions
RPROVIDES_${PN}-core = "${PN}"
RRECOMMENDS_${PN}-core = "${PN}-readline"
RRECOMMENDS_${PN}-crypt = "openssl"

# add sitecustomize
FILES_${PN}-core += "${libdir}/python${PYTHON_MAJMIN}/sitecustomize.py"
# ship 2to3
FILES_${PN}-core += "${bindir}/2to3"

# package libpython2
PACKAGES =+ "lib${BPN}2"
FILES_lib${BPN}2 = "${libdir}/libpython*.so.*"

# additional stuff -dev

FILES_${PN}-dev = "\
  ${includedir} \
  ${libdir}/lib*${SOLIBSDEV} \
  ${libdir}/*.la \
  ${libdir}/*.a \
  ${libdir}/*.o \
  ${libdir}/pkgconfig \
  ${base_libdir}/*.a \
  ${base_libdir}/*.o \
  ${datadir}/aclocal \
  ${datadir}/pkgconfig \
"

# catch debug extensions (isn't that already in python-core-dbg?)
FILES_${PN}-dbg += "${libdir}/python${PYTHON_MAJMIN}/lib-dynload/.debug"

# catch all the rest (unsorted)
PACKAGES += "${PN}-misc"
FILES_${PN}-misc = "${libdir}/python${PYTHON_MAJMIN}"

# catch manpage
PACKAGES += "${PN}-man"
FILES_${PN}-man = "${datadir}/man"

BBCLASSEXTEND = "nativesdk"
