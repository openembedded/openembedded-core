DESCRIPTION = "Cross locale generation tool for eglibc"
HOMEPAGE = "http://www.eglibc.org/home"
SECTION = "libs"
LICENSE = "LGPL-2.1"

LIC_DIR = "${WORKDIR}/eglibc-${PV}/libc"
LIC_FILES_CHKSUM = "file://${LIC_DIR}/LICENSES;md5=98a1128c4b58120182cbea3b1752d8b9\
      file://${LIC_DIR}/COPYING;md5=393a5ca445f6965873eca0259a17f833 \
      file://${LIC_DIR}/posix/rxspencer/COPYRIGHT;md5=dc5485bb394a13b2332ec1c785f5d83a \
      file://${LIC_DIR}/COPYING.LIB;md5=bbb461211a33b134d42ed5ee802b37ff "


inherit native
inherit autotools

# pick up an eglibc-2.16 patch
FILESPATH = "${FILE_DIRNAME}/eglibc-${PV}"

PR = "r2"
SRC_URI = "http://downloads.yoctoproject.org/releases/eglibc/eglibc-${PV}-svnr21224.tar.bz2 \
	   file://fix_for_centos_5.8.patch;patchdir=.. \
	  "

SRC_URI[md5sum] = "88894fa6e10e58e85fbd8134b8e486a8"
SRC_URI[sha256sum] = "460a45f422da6eb1fd909baab6a64b5ae4c8ba18ea05a1491ed1024c8b98eeaa"

S = "${WORKDIR}/eglibc-${PV}/localedef"

do_unpack_append() {
    bb.build.exec_func('do_move_ports', d)
}

do_move_ports() {
        if test -d ${WORKDIR}/eglibc-${PV}/ports ; then
	    rm -rf ${WORKDIR}/libc/ports
	    mv ${WORKDIR}/eglibc-${PV}/ports ${WORKDIR}/libc/
	fi
}

EXTRA_OECONF = "--with-glibc=${WORKDIR}/eglibc-${PV}/libc"
CFLAGS += "-DNOT_IN_libc=1"

do_configure () {
	./configure ${EXTRA_OECONF}
}


do_install() {
	install -d ${D}${bindir} 
	install -m 0755 ${S}/localedef ${D}${bindir}/cross-localedef
}
