SUMMARY = "Generic library support script"
DESCRIPTION = "This is GNU libtool, a generic library support script. \
Libtool hides the complexity of generating special library types \
(such as shared libraries) behind a consistent interface."
HOMEPAGE = "http://www.gnu.org/software/libtool/libtool.html"
SECTION = "devel"
LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://libltdl/COPYING.LIB;md5=4fbd65380cdd255951079008b364516c "

SRC_URI = "${GNU_MIRROR}/libtool/libtool-${PV}.tar.gz \
           file://trailingslash.patch \
           file://rename-with-sysroot.patch \
           file://use-sysroot-in-libpath.patch \
           file://fix-final-rpath.patch \
           file://fix-rpath.patch \
           file://norm-rpath.patch \
           file://dont-depend-on-help2man.patch \
           file://fix-resolve-lt-sysroot.patch \
          "

SRC_URI[md5sum] = "353ed373fd3c6d7e47a1f4a8728d966b"
SRC_URI[sha256sum] = "159d4e20c201f929e3562536d3ae6b5e605403fa4bb4e72ef197a4e162c3fedf"

do_compile_prepend () {
	# Sometimes this file doesn't get rebuilt, force the issue
	rm -f ${S}/build-aux/ltmain.sh
	make build-aux/ltmain.sh
	./config.status
}

inherit autotools texinfo
EXTRA_AUTORECONF = "--exclude=libtoolize"

DEPENDS = "libtool-native"

PACKAGES =+ "libltdl libltdl-dev libltdl-dbg libltdl-staticdev"
FILES_${PN} += "${datadir}/aclocal"
FILES_${PN}-dev = "${includedir} ${FILES_SOLIBSDEV} ${libdir}/*.la \
                ${libdir}/*.o ${libdir}/pkgconfig ${datadir}/pkgconfig \
                ${base_libdir}/*.o ${libdir}/${BPN}/*.la ${base_libdir}/*.la"

#FILES_${PN}-dev := "${@oe_filter_out('${datadir}/aclocal', '${FILES_libtool-dev}', d)}"
FILES_libltdl = "${libdir}/libltdl${SOLIBS}"
FILES_libltdl-dev = "${libdir}/libltdl${SOLIBSDEV} ${includedir}"
FILES_libltdl-staticdev = "${libdir}/libltdl.a"
FILES_libltdl-dbg = "${libdir}/.debug/"

export CONFIG_SHELL="/bin/bash"
