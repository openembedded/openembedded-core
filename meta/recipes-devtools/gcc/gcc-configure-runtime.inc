require gcc-configure-common.inc

CXXFLAGS := "${@oe_filter_out('-fvisibility-inlines-hidden', '${CXXFLAGS}', d)}"

EXTRA_OECONF_PATHS = " \
    --with-local-prefix=${STAGING_DIR_TARGET}${prefix} \
    --with-gxx-include-dir=${includedir}/c++/ \
    --with-sysroot=${STAGING_DIR_TARGET} \
    --with-build-sysroot=${STAGING_DIR_TARGET}"

RUNTIMETARGET = "libssp libstdc++-v3"
RUNTIMETARGET_append_powerpc = " libgomp"
RUNTIMETARGET_append_powerpc64 = " libgomp"
#  ?
# libiberty
# libmudflap
# libgfortran

do_configure () {
	export CXX="${CXX} -nostdinc++ -nostdlib++"
	for d in ${RUNTIMETARGET}; do
		echo "Configuring $d"
		mkdir -p ${B}/$d/
		cd ${B}/$d/
		chmod a+x ${S}/$d/configure
		${S}/$d/configure ${CONFIGUREOPTS} ${EXTRA_OECONF}
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
		if [ "$d" = "libgomp" ]; then
			rm -rf ${D}${datadir}/info/libgomp.info ${D}${datadir}/info/dir
			rmdir --ignore-fail-on-non-empty -p ${D}${libdir}/gcc/${TARGET_SYS}/${BINV}/finclude
			rmdir --ignore-fail-on-non-empty -p ${D}${datadir}/info
		fi
	done
	chown -R root:root ${D}
}

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++ libgcc"
PROVIDES = "virtual/${TARGET_PREFIX}compilerlibs"

BBCLASSEXTEND = "nativesdk"

