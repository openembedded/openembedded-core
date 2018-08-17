SUMMARY = "Secure Socket Layer"
DESCRIPTION = "Secure Socket Layer (SSL) binary and related cryptographic tools."
HOMEPAGE = "http://www.openssl.org/"
BUGTRACKER = "http://www.openssl.org/news/vulnerabilities.html"
SECTION = "libs/network"

# "openssl | SSLeay" dual license
LICENSE = "openssl"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d57d511030c9d66ef5f5966bee5a7eff"

DEPENDS = "hostperl-runtime-native"

SRC_URI = "http://www.openssl.org/source/openssl-${PV}.tar.gz \
           file://run-ptest \
           file://openssl-c_rehash.sh \
           file://0001-Take-linking-flags-from-LDFLAGS-env-var.patch \
           "

SRC_URI_append_class-nativesdk = " \
           file://environment.d-openssl.sh \
           "

SRC_URI[md5sum] = "9495126aafd2659d357ea66a969c3fe1"
SRC_URI[sha256sum] = "ebbfc844a8c8cc0ea5dc10b86c9ce97f401837f3fa08c17b2cdadc118253cf99"

inherit lib_package multilib_header ptest relative_symlinks

#| engines/afalg/e_afalg.c: In function 'eventfd':
#| engines/afalg/e_afalg.c:110:20: error: '__NR_eventfd' undeclared (first use in this function)
#|      return syscall(__NR_eventfd, n);
#|                     ^~~~~~~~~~~~
EXTRA_OECONF_append_aarch64 = " no-afalgeng"

#| ./libcrypto.so: undefined reference to `getcontext'
#| ./libcrypto.so: undefined reference to `setcontext'
#| ./libcrypto.so: undefined reference to `makecontext'
EXTRA_OECONF_append_libc-musl = " -DOPENSSL_NO_ASYNC"

do_configure () {
	os=${HOST_OS}
	case $os in
	linux-gnueabi |\
	linux-gnuspe |\
	linux-musleabi |\
	linux-muslspe |\
	linux-musl )
		os=linux
		;;
	*)
		;;
	esac
	target="$os-${HOST_ARCH}"
	case $target in
	linux-arm*)
		target=linux-armv4
		;;
	linux-aarch64*)
		target=linux-aarch64
		;;
	linux-i?86 | linux-viac3)
		target=linux-x86
		;;
	linux-gnux32-x86_64 | linux-muslx32-x86_64 )
		target=linux-x32
		;;
	linux-gnu64-x86_64)
		target=linux-x86_64
		;;
	linux-mips | linux-mipsel)
		# specifying TARGET_CC_ARCH prevents openssl from (incorrectly) adding target architecture flags
		target="linux-mips32 ${TARGET_CC_ARCH}"
		;;
	linux-gnun32-mips*)
		target=linux-mips64
		;;
	linux-*-mips64 | linux-mips64 | linux-*-mips64el | linux-mips64el)
		target=linux64-mips64
		;;
	linux-microblaze* | linux-nios2* | linux-sh3 | linux-sh4)
		target=linux-generic32
		;;
	linux-powerpc)
		target=linux-ppc
		;;
	linux-powerpc64)
		target=linux-ppc64
		;;
	linux-riscv32)
		target=linux-generic32
		;;
	linux-riscv64)
		target=linux-generic64
		;;
	linux-sparc | linux-supersparc)
		target=linux-sparcv9
		;;
	esac

	useprefix=${prefix}
	if [ "x$useprefix" = "x" ]; then
		useprefix=/
	fi
	libdirleaf="$(echo ${libdir} | sed s:$useprefix::)"
	perl ./Configure ${EXTRA_OECONF} ${PACKAGECONFIG_CONFARGS} --prefix=$useprefix --openssldir=${libdir}/ssl-1.1 --libdir=$libdirleaf $target
}

do_install () {
	oe_runmake DESTDIR="${D}" MANDIR="${mandir}" MANSUFFIX=ssl install
	oe_multilib_header openssl/opensslconf.h

	# Create SSL structure for PATH hard-coded packages like ca-certificates
	# Debian is also using this technique
	install -d ${D}${sysconfdir}/ssl/
	mv ${D}${libdir}/ssl-1.1/openssl.cnf \
		${D}${libdir}/ssl-1.1/certs \
		${D}${libdir}/ssl-1.1/private \
		\
		${D}${sysconfdir}/ssl/
	ln -sf ${sysconfdir}/ssl/certs ${D}${libdir}/ssl-1.1/certs
	ln -sf ${sysconfdir}/ssl/private ${D}${libdir}/ssl-1.1/private
	ln -sf ${sysconfdir}/ssl/openssl.cnf ${D}${libdir}/ssl-1.1/openssl.cnf
}

do_install_append_class-native () {
	# Install a custom version of c_rehash that can handle sysroots properly.
	# This version is used for example when installing ca-certificates during
	# image creation.
	install -Dm 0755 ${WORKDIR}/openssl-c_rehash.sh ${D}${bindir}/c_rehash
	sed -i -e 's,/etc/openssl,${sysconfdir}/ssl,g' ${D}${bindir}/c_rehash
}

do_install_append_class-nativesdk () {
	mkdir -p ${D}${SDKPATHNATIVE}/environment-setup.d
	install -m 644 ${WORKDIR}/environment.d-openssl.sh ${D}${SDKPATHNATIVE}/environment-setup.d/openssl.sh
}

do_install_ptest() {
	cp -r * ${D}${PTEST_PATH}

	# Putting .so files in ptest package will mess up the dependencies of the main openssl package
	# so we rename them to .so.ptest and patch the test accordingly
	mv ${D}${PTEST_PATH}/libcrypto.so ${D}${PTEST_PATH}/libcrypto.so.ptest
	mv ${D}${PTEST_PATH}/libssl.so ${D}${PTEST_PATH}/libssl.so.ptest
	sed -i 's/$target{shared_extension_simple}/".so.ptest"/' ${D}${PTEST_PATH}/test/recipes/90-test_shlibload.t
}

PACKAGES =+ "libcrypto libssl ${PN}-misc ${PN}-engines openssl-conf"

FILES_libcrypto = "${libdir}/libcrypto${SOLIBS}"
FILES_libssl = "${libdir}/libssl${SOLIBS}"
FILES_${PN} =+ "${libdir}/ssl-1.1/*"
FILES_${PN}_append_class-nativesdk = " ${SDKPATHNATIVE}/environment-setup.d/openssl.sh"
FILES_${PN}-engines = "${libdir}/engines-1.1"

FILES_${PN}-misc = "${libdir}/ssl-1.1/misc"
RDEPENDS_${PN}-misc = "${@bb.utils.filter('PACKAGECONFIG', 'perl', d)}"

FILES_openssl-conf = "${sysconfdir}/ssl/openssl.cnf ${libdir}/ssl-1.1/openssl.cnf"
CONFFILES_openssl-conf = "${sysconfdir}/ssl/openssl.cnf"
RRECOMMENDS_libcrypto += "openssl-conf"

RDEPENDS_${PN}-bin = "perl"
RDEPENDS_${PN}-ptest += "perl-module-file-spec-functions bash python"

BBCLASSEXTEND = "native nativesdk"
