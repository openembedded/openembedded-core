SUMMARY = "New GNU Portable Threads library"
HOMEPAGE = "http://www.gnupg.org/software/pth/"
SECTION = "libs"
LICENSE = "LGPLv3+ & GPLv2+"
LIC_FILES_CHKSUM = "\
    file://COPYING;md5=751419260aa954499f7abaabaa882bbe\
    file://COPYING.LESSER;md5=6a6a8e020838b23406c81b19c1d46df6\
    "
SRC_URI = "ftp://ftp.gnupg.org/gcrypt/npth/npth-${PV}.tar.bz2 \
           file://pkgconfig.patch \
          "

SRC_URI[md5sum] = "aaffc8ef3e955ab50a1905809f268a23"
SRC_URI[sha256sum] = "896c561eb2ec8da35f11828fb04a3fbff12d41ff657c799056d7dc4a66e5df7f"

BINCONFIG = "${bindir}/npth-config"

inherit autotools binconfig-disabled

FILES_${PN} = "${libdir}/libnpth.so.*"
FILES_${PN}-dev += "${bindir}/npth-config"
