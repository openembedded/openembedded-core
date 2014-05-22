SUMMARY = "New GNU Portable Threads library"
HOMEPAGE = "http://www.gnupg.org/software/pth/"
SECTION = "libs"
LICENSE = "LGPLv3+ & GPLv2+"
LIC_FILES_CHKSUM = "\
    file://COPYING;md5=751419260aa954499f7abaabaa882bbe\
    file://COPYING.LESSER;md5=6a6a8e020838b23406c81b19c1d46df6\
    "
SRC_URI = "ftp://ftp.gnupg.org/gcrypt/npth/npth-${PV}.tar.bz2 \
          "
SRC_URI[md5sum] = "87712f0cee656c390b49773923e26e7f"
SRC_URI[sha256sum] = "caef86ced4a331e162897818a5b924860c8d6003e52da5bdf76da00e8e0dfae1"

BINCONFIG = "${bindir}/npth-config"

inherit autotools binconfig-disabled

FILES_${PN} = "${libdir}/libnpth.so.*"
FILES_${PN}-dev += "${bindir}/npth-config"
