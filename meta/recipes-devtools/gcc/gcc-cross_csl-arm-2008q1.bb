require gcc-csl-arm-2008q1.inc
require gcc-cross4.inc
require gcc-configure-cross.inc
require gcc-package-cross.inc

SRC_URI_append = " file://zecke-no-host-includes.patch "

EXTRA_OECONF += "--disable-multilib --disable-libunwind-exceptions --with-mpfr=${STAGING_DIR_NATIVE}${prefix_native}"

#We don't want i686 linux ending up in the CFLAGS_FOR_TARGET like this: -isystem/OE/angstrom-tmp/staging/i686-linux/usr/include
CFLAGS = ""
CXXFLAGS = ""
LDFLAGS = ""

# staging-linkage and cross-linkage recipes don't work anymore, so do it by hand for this backwards CSL toolchain
do_compile_prepend() {
	ln -sf ${STAGING_DIR_TARGET}${target_libdir}/crt*.o ${STAGING_DIR_NATIVE}${prefix_native}/${TARGET_SYS}/lib/
	ln -sf ${STAGING_DIR_TARGET}${target_libdir}/ld-* ${STAGING_DIR_NATIVE}${prefix_native}/${TARGET_SYS}/lib/
	ln -sf ${STAGING_DIR_TARGET}/lib/libc* ${STAGING_DIR_NATIVE}${prefix_native}/${TARGET_SYS}/lib/ 
	sed -i -e 's:gcc_no_link=yes:gcc_no_link=no:' ${S}/libstdc++-v3/configure

}

ARCH_FLAGS_FOR_TARGET += " -L${STAGING_DIR_TARGET}${target_libdir} -isystem${STAGING_DIR_TARGET}${target_includedir}"
