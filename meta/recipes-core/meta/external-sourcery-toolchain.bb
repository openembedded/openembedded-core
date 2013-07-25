require recipes-core/eglibc/eglibc-package.inc

INHIBIT_DEFAULT_DEPS = "1"

# License applies to this recipe code, not the toolchain itself
SUMMARY = "External Sourcery G++ toolchain"
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
	virtual/${TARGET_PREFIX}libc-initial \
	virtual/${TARGET_PREFIX}compilerlibs \
	virtual/libc \
	virtual/libintl \
	virtual/libiconv \
	glibc-thread-db \
	libgcc \
	eglibc \
	virtual/linux-libc-headers \
"
PV = "${CSL_VER_MAIN}"
PR = "r7"

#SRC_URI = "http://www.codesourcery.com/public/gnu_toolchain/${CSL_TARGET_SYS}/arm-${PV}-${TARGET_PREFIX}i686-pc-linux-gnu.tar.bz2"

SRC_URI = "file://SUPPORTED"

do_install() {
	# Use optimized files if available
	sysroot="${EXTERNAL_TOOLCHAIN_SYSROOT}"

	cp -a $sysroot${base_libdir}/. ${D}${base_libdir}
	cp -a $sysroot/etc/. ${D}${sysconfdir}
	cp -a $sysroot/sbin/. ${D}${base_sbindir}

	install -d ${D}/usr
	for usr_element in bin libexec sbin share ${base_libdir}; do
		usr_path=$sysroot/usr/$usr_element
		cp -a $usr_path ${D}/usr/
	done
	for datadir_element in man info; do
		datadir_path=$sysroot/usr/$datadir_element
		if [ -e $datadir_path ]; then
			cp -a $datadir_path ${D}${datadir}/
		fi
	done

	# Some toolchains have headers under the core specific area
	if [ -e $sysroot/usr/include ]; then
		cp -a $sysroot/usr/include/. ${D}${includedir}
	else
		cp -a $sysroot/../usr/include/. ${D}${includedir}
	fi

	rm ${D}${sysconfdir}/rpc
	rm -r ${D}${datadir}/zoneinfo

	mv ${D}${libdir}/bin/* ${D}${bindir}/
	if [ -e ${D}${libdir}/bin/.debug ]; then
		mv ${D}${libdir}/bin/.debug/* ${D}${bindir}/.debug/
	fi
	ln -s ../../bin/gdbserver ${D}${libdir}/bin/sysroot-gdbserver

	sed -i -e 's/__packed/__attribute__ ((packed))/' ${D}${includedir}/mtd/ubi-user.h
        sed -i -e "s# ${base_libdir}# ../..${base_libdir}#g" -e "s# ${libdir}# .#g" ${D}${libdir}/libc.so
        sed -i -e "s# ${base_libdir}# ../..${base_libdir}#g" -e "s# ${libdir}# .#g" ${D}${libdir}/libpthread.so
}

SYSROOT_PREPROCESS_FUNCS += "external_toolchain_sysroot_adjust"
external_toolchain_sysroot_adjust() {
	dest_sysroot="$(${CC} -print-sysroot | sed -e's,^${STAGING_DIR_HOST},,; s,/$,,')"
	if [ -n "$dest_sysroot" ]; then
		rm -f ${SYSROOT_DESTDIR}/$dest_sysroot
		ln -s . ${SYSROOT_DESTDIR}/$dest_sysroot
	fi

	# If the usr/lib directory doesn't exist, the toolchain fails to even
	# try to find crti.o in a completely different directory (usr/lib64)
	install -d ${SYSROOT_DESTDIR}/usr/lib
}

PACKAGES =+ "libgcc libgcc-dev libstdc++ libstdc++-dev libstdc++-staticdev linux-libc-headers linux-libc-headers-dev gdbserver gdbserver-dbg"

# This test should be fixed to ignore .a files in .debug dirs
INSANE_SKIP_${PN}-dbg = "staticdev"

# We don't care about GNU_HASH in prebuilt binaries
INSANE_SKIP_${PN}-utils += "ldflags"
INSANE_SKIP_libstdc++ += "ldflags"
INSANE_SKIP_libgcc += "ldflags"
INSANE_SKIP_gdbserver += "ldflags"

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
PKGV_libstdc++-staticdev = "${CSL_VER_GCC}"
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
	${libdir}/libsupc++.la"
FILES_libstdc++-staticdev = "${libdir}/libstdc++.a ${libdir}/libsupc++.a"
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

