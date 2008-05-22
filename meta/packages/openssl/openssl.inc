DESCRIPTION = "Secure Socket Layer (SSL) binary and related cryptographic tools."
HOMEPAGE = "http://www.openssl.org/"
LICENSE = "openssl"
SECTION = "libs/network"

SRC_URI = "http://www.openssl.org/source/openssl-${PV}.tar.gz"
S = "${WORKDIR}/openssl-${PV}"

AR_append = " r"
CFLAG = "${@base_conditional('SITEINFO_ENDIANESS', 'le', '-DL_ENDIAN', '-DB_ENDIAN', d)} \
	-DTERMIO ${FULL_OPTIMIZATION} -Wall"

# -02 does not work on mipsel: ssh hangs when it tries to read /dev/urandom
CFLAG_mtx-1 := "${@'${CFLAG}'.replace('-O2', '')}"
CFLAG_mtx-2 := "${@'${CFLAG}'.replace('-O2', '')}"

export DIRS = "crypto ssl apps"
export EX_LIBS = "-lgcc -ldl"
export AS = "${CC} -c"

PACKAGES =+ "libcrypto libssl"
FILES_libcrypto = "${libdir}/libcrypto.so.*"
FILES_libssl = "${libdir}/libssl.so.*"

do_configure () {
	cd util
	perl perlpath.pl ${bindir}
	cd ..
	ln -sf apps/openssl.pod crypto/crypto.pod ssl/ssl.pod doc/

	os=${HOST_OS}
	if [ "x$os" = "xlinux-uclibc" ]; then
		os=linux
	fi
	target="$os-${HOST_ARCH}"
	case $target in
	linux-arm)
		target=linux-elf-arm
		;;
	linux-armeb)
		target=linux-elf-armeb
		;;
	linux-sh3)
		target=debian-sh3
		;;
	linux-sh4)
		target=debian-sh4
		;;
	linux-i486)
		target=debian-i386-i486
		;;
	linux-i586)
		target=debian-i386-i586
		;;
	linux-i686)
		target=debian-i386-i686/cmov
		;;
	linux-mips)
		target=debian-mips
		;;
	linux-mipsel)
		target=debian-mipsel
		;;
	linux-powerpc)
		target=linux-ppc
		;;
	linux-supersparc)
		target=linux-sparcv8
		;;
	linux-sparc)
		target=linux-sparcv8
		;;
	esac
	# inject machine-specific flags
	sed -i -e "s|^\(\"$target\",\s*\"[^:]\+\):\([^:]\+\)|\1:${CFLAG}|g" Configure
	perl ./Configure shared --prefix=${prefix} --openssldir=${libdir}/ssl $target
}

do_compile () {
	oe_runmake
}

do_stage () {
	cp --dereference -R include/openssl ${STAGING_INCDIR}/
	oe_libinstall -a -so libcrypto ${STAGING_LIBDIR}
	oe_libinstall -a -so libssl ${STAGING_LIBDIR}
}

do_install () {
	install -m 0755 -d ${D}${libdir}/pkgconfig
	oe_runmake INSTALL_PREFIX="${D}" install
	chmod 644 ${D}${libdir}/pkgconfig/openssl.pc
	oe_libinstall -so libcrypto ${D}${libdir}
	oe_libinstall -so libssl ${D}${libdir}
}
