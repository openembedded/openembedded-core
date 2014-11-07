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

SRC_URI[md5sum] = "36869bf10378ea08c155cd5c3580a1dd"
SRC_URI[sha256sum] = "9ad49de376310effab7fdad69581054b0fa9023d062032d06e73058146c8f76e"

BINCONFIG = "${bindir}/npth-config"

inherit autotools binconfig-disabled

FILES_${PN} = "${libdir}/libnpth.so.*"
FILES_${PN}-dev += "${bindir}/npth-config"
