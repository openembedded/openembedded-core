SUMMARY = "Drop-in replacement for openssl 1.0.x, maintained by OpenBSD"
DESCRIPTION = "LibreSSL is a version of the TLS/crypto stack forked from \
               OpenSSL in 2014, with goals of modernizing the codebase, \
               improving security, and applying best practice development processes. "
HOMEPAGE = "http://www.libressl.org/"

LICENSE = "openssl"
LIC_FILES_CHKSUM = "file://COPYING;md5=01f9bb4d275f5eeea905377bef3de622"

SRC_URI = "https://ftp.openbsd.org/pub/OpenBSD/LibreSSL/libressl-${PV}.tar.gz \
           file://0001-Link-dynamic-libraries-with-their-library-dependenci.patch \
           "
SRC_URI[md5sum] = "d922be6690e7de8949948aaec42a4563"
SRC_URI[sha256sum] = "af2bba965b06063518eec6f192d411631dfe1d07713760c67c3c29d348789dc3"

inherit cmake

EXTRA_OECMAKE = "-DOPENSSLDIR=${sysconfdir}/libressl -DBUILD_SHARED_LIBS=ON"

PACKAGE_PREPROCESS_FUNCS += "libressl_package_preprocess"

# libressl development files and executable binaries clash with openssl 1.1
# files when installed into target rootfs. So we don't put them into
# packages, but they continue to be provided via target sysroot for
# cross-compilation on the host, if some software needs specifically libressl.
libressl_package_preprocess () {
        for file in `find ${PKGD} -name *.h -o -name *.pc -o -name *.so`; do
                rm $file
        done
}

# {standard input}: Assembler messages:
# {standard input}:303: Error: selected processor does not support `rev r0,r0' in ARM mode
# {standard input}:303: Error: selected processor does not support `rev ip,ip' in ARM mode
OECMAKE_C_FLAGS_append_arm = " -D__STRICT_ALIGNMENT"
