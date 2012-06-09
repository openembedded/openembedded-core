DESCRIPTION = "New GNU Portable Threads"
HOMEPAGE = "http://www.gnupg.org/software/pth/"
SECTION = "libs"
LICENSE = "LGPLv3+ & GPLv2+"
LIC_FILES_CHKSUM = "\
    file://COPYING;md5=751419260aa954499f7abaabaa882bbe\
    file://COPYING.LESSER;md5=6a6a8e020838b23406c81b19c1d46df6\
    "
SRC_URI = "ftp://ftp.gnupg.org/gcrypt/npth/npth-${PV}.tar.bz2 \
          "
SRC_URI[md5sum] = "701665669b371385a92817ff1f9215ca"
SRC_URI[sha256sum] = "e24e99c9e90e7c2c2272fed3a939647fdcd3a7ee0c9efb2983eed553ab2b4568"

inherit autotools binconfig

FILES_${PN} = "${libdir}/libnpth.so.*"
FILES_${PN}-dev += "${bindir}/npth-config"
