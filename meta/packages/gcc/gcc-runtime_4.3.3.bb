PR = "r10"

require gcc-${PV}.inc
require gcc-configure-runtime.inc
require gcc-package-runtime.inc

SRC_URI_append = "file://fortran-cross-compile-hack.patch;patch=1"

ARCH_FLAGS_FOR_TARGET += "-isystem${STAGING_INCDIR}"

RUNTIMETARGET = "libgcc libssp libstdc++-v3"
#  ?
# libiberty
# libmudflap
# libgfortran

do_configure () {
	cp ${CROSS_DIR}/include/gcc-build-internal/* ${S}/gcc
	for d in ${RUNTIMETARGET}; do
		echo "Configuring $d"
		mkdir -p ${B}/$d/
		cd ${B}/$d/
		chmod a+x ${S}/$d/configure
		${S}/$d/configure ${CONFIGUREOPTS}
	done
}

do_compile () {
	for d in ${RUNTIMETARGET}; do
		cd ${B}/$d/
		oe_runmake
	done
}

do_install () {
	for d in ${RUNTIMETARGET}; do
		cd ${B}/$d/
		oe_runmake 'DESTDIR=${D}' install
	done
}

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++"
PROVIDES = "virtual/${TARGET_PREFIX}compilerlibs"

BBCLASSEXTEND = "nativesdk"
