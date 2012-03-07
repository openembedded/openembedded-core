require recipes-core/eglibc/eglibc-package.inc

INHIBIT_DEFAULT_DEPS = "1"

# License applies to this recipe code, not the toolchain itself
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PROVIDES += "\
	linux-libc-headers \
	virtual/${TARGET_PREFIX}gcc \
	virtual/${TARGET_PREFIX}g++ \
	virtual/${TARGET_PREFIX}gcc-initial \
	virtual/${TARGET_PREFIX}gcc-intermediate \
	virtual/${TARGET_PREFIX}binutils \
	virtual/${TARGET_PREFIX}libc-for-gcc \
	virtual/${TARGET_PREFIX}compilerlibs \
	virtual/libc \
	virtual/libintl \
	virtual/libiconv \
	glibc-thread-db \
	libgcc \
	virtual/linux-libc-headers \
"
PV = "${CSL_VER_MAIN}"
PR = "r3"

#SRC_URI = "http://www.codesourcery.com/public/gnu_toolchain/${CSL_TARGET_SYS}/arm-${PV}-${TARGET_PREFIX}i686-pc-linux-gnu.tar.bz2"

SRC_URI = "file://SUPPORTED"

do_install() {
	install -d ${D}${sysconfdir} ${D}${bindir} ${D}${sbindir} ${D}${base_bindir} ${D}${libdir}
	install -d ${D}${base_libdir} ${D}${base_sbindir} ${D}${datadir} ${D}/usr

	if [ -d ${EXTERNAL_TOOLCHAIN}/${CSL_TARGET_SYS}/libc/${CSL_TARGET_CORE} ]; then
		cp -a ${EXTERNAL_TOOLCHAIN}/${CSL_TARGET_SYS}/libc/${CSL_TARGET_CORE}/lib/.  ${D}${base_libdir}
		cp -a ${EXTERNAL_TOOLCHAIN}/${CSL_TARGET_SYS}/libc/${CSL_TARGET_CORE}/etc/.  ${D}${sysconfdir}
		cp -a ${EXTERNAL_TOOLCHAIN}/${CSL_TARGET_SYS}/libc/${CSL_TARGET_CORE}/sbin/. ${D}${base_sbindir}
		if [ ! -e ${EXTERNAL_TOOLCHAIN}/${CSL_TARGET_SYS}/libc/${CSL_TARGET_CORE}/usr/include ]; then
			cp -a ${EXTERNAL_TOOLCHAIN}/${CSL_TARGET_SYS}/libc/usr/include  ${D}/usr/
		fi
		cp -a ${EXTERNAL_TOOLCHAIN}/${CSL_TARGET_SYS}/libc/${CSL_TARGET_CORE}/usr/.  ${D}/usr/
	else
		cp -a ${EXTERNAL_TOOLCHAIN}/${CSL_TARGET_SYS}/libc/lib/.  ${D}${base_libdir}
		cp -a ${EXTERNAL_TOOLCHAIN}/${CSL_TARGET_SYS}/libc/etc/.  ${D}${sysconfdir}
		cp -a ${EXTERNAL_TOOLCHAIN}/${CSL_TARGET_SYS}/libc/sbin/. ${D}${base_sbindir}
		cp -a ${EXTERNAL_TOOLCHAIN}/${CSL_TARGET_SYS}/libc/usr/.  ${D}/usr/
	fi

	if [ -e ${D}${prefix}/info ]; then
		mv ${D}${prefix}/info ${D}${infodir}
	fi
	if [ -e ${D}${prefix}/man ]; then
		mv ${D}${prefix}/man ${D}${mandir}
	fi

	rm ${D}${sysconfdir}/rpc
	rm -r ${D}${datadir}/zoneinfo

	mv ${D}${libdir}/bin/* ${D}${bindir}/
	if [ -e ${D}${libdir}/bin/.debug ]; then
		install -d ${D}${bindir}/.debug
		mv ${D}${libdir}/bin/.debug/* ${D}${bindir}/.debug/
	fi
	ln -s ../../bin/gdbserver ${D}${libdir}/bin/sysroot-gdbserver

	sed -i -e 's/__packed/__attribute__ ((packed))/' ${D}${includedir}/mtd/ubi-user.h
	sed -i -e "s# /lib# ../../lib#g" -e "s# /usr/lib# .#g" ${D}${libdir}/libc.so
	sed -i -e "s# /lib# ../../lib#g" -e "s# /usr/lib# .#g" ${D}${libdir}/libpthread.so
}

do_install_locale_append () {
	rm -r ${D}${datadir}/locale ${D}${libdir}/locale
}

SYSROOT_PREPROCESS_FUNCS += "external_toolchain_sysroot_adjust"
external_toolchain_sysroot_adjust() {
       if [ -n "${CSL_TARGET_CORE}" ]; then
               rm -f ${SYSROOT_DESTDIR}/${CSL_TARGET_CORE}
               ln -s . ${SYSROOT_DESTDIR}/${CSL_TARGET_CORE}
       fi

       if [ "${TUNE_PKGARCH}" = "i586" ]; then
               rm -f ${SYSROOT_DESTDIR}/system32
               ln -s . ${SYSROOT_DESTDIR}/system32
       fi
}

PACKAGES =+ "libgcc libgcc-dev libstdc++ libstdc++-dev linux-libc-headers linux-libc-headers-dev gdbserver gdbserver-dbg"

INSANE_SKIP_libgcc = "1"
INSANE_SKIP_libstdc++ = "1"
INSANE_SKIP_gdbserver = "1"

PKG_${PN} = "eglibc"
PKG_${PN}-dev = "eglibc-dev"
PKG_${PN}-staticdev = "eglibc-staticdev"
PKG_${PN}-doc = "eglibc-doc"
PKG_${PN}-dbg = "eglibc-dbg"
PKG_${PN}-pic = "eglibc-pic"
PKG_${PN}-utils = "eglibc-utils"
PKG_${PN}-gconv = "eglibc-gconv"
PKG_${PN}-extra-nss = "eglibc-extra-nss"
PKG_${PN}-thread-db = "eglibc-thread-db"
PKG_${PN}-pcprofile = "eglibc-pcprofile"

PKGV = "${CSL_VER_LIBC}"
PKGV_libgcc = "${CSL_VER_GCC}"
PKGV_libgcc-dev = "${CSL_VER_GCC}"
PKGV_libstdc++ = "${CSL_VER_GCC}"
PKGV_libstdc++-dev = "${CSL_VER_GCC}"
PKGV_linux-libc-headers = "${CSL_VER_KERNEL}"
PKGV_linux-libc-headers-dev = "${CSL_VER_KERNEL}"
PKGV_gdbserver = "${CSL_VER_GDB}"
PKGV_gdbserver-dbg = "${CSL_VER_GDB}"

FILES_libgcc = "${base_libdir}/libgcc_s.so.1"
FILES_libgcc-dev = "${base_libdir}/libgcc_s.so"
FILES_libstdc++ = "${libdir}/libstdc++.so.*"
FILES_libstdc++-dev = "${includedir}/c++/${PV} \
	${libdir}/libstdc++.so \
	${libdir}/libstdc++.la \
	${libdir}/libstdc++.a \
	${libdir}/libsupc++.la \
	${libdir}/libsupc++.a"
FILES_linux-libc-headers = "${includedir}/asm* \
	${includedir}/linux \
	${includedir}/mtd \
	${includedir}/rdma \
	${includedir}/scsi \
	${includedir}/sound \
	${includedir}/video \
"
FILES_gdbserver = "${bindir}/gdbserver ${libdir}/bin/sysroot-gdbserver"
FILES_gdbserver-dbg = "${bindir}/.debug/gdbserver"

CSL_VER_MAIN ??= ""

python () {
    if not d.getVar("CSL_VER_MAIN"):
	raise bb.parse.SkipPackage("External CSL toolchain not configured (CSL_VER_MAIN not set).")
}

