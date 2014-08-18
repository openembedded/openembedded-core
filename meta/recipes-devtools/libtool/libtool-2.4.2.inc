SUMMARY = "Generic library support script"
DESCRIPTION = "This is GNU libtool, a generic library support script. \
Libtool hides the complexity of generating special library types \
(such as shared libraries) behind a consistent interface."
HOMEPAGE = "http://www.gnu.org/software/libtool/libtool.html"
SECTION = "devel"
LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
    file://libltdl/COPYING.LIB;md5=e3eda01d9815f8d24aae2dbd89b68b06"

INC_PR = "r6"

SRC_URI = "${GNU_MIRROR}/libtool/libtool-${PV}.tar.gz \
           file://trailingslash.patch \
           file://rename-with-sysroot.patch \
           file://use-sysroot-in-libpath.patch \
           file://fix-final-rpath.patch \
           file://avoid_absolute_paths_for_general_utils.patch \
           file://fix-rpath.patch \
	   file://respect-fstack-protector.patch \
           file://norm-rpath.patch \
           file://dont-depend-on-help2man.patch \
           file://fix-resolve-lt-sysroot.patch \
          "

SRC_URI[md5sum] = "d2f3b7d4627e69e13514a40e72a24d50"
SRC_URI[sha256sum] = "b38de44862a987293cd3d8dfae1c409d514b6c4e794ebc93648febf9afc38918"

do_compile_prepend () {
	# Sometimes this file doesn't get rebuilt, force the issue
	rm -f ${S}/libltdl/config/ltmain.sh
	make libltdl/config/ltmain.sh
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
