DESCRIPTION = "An audio format Conversion library"
HOMEPAGE = "http://www.mega-nerd.com/libsndfile"
AUTHOR = "Erik de Castro Lopo"
DEPENDS = "sqlite3"
SECTION = "libs/multimedia"
LICENSE = "LGPLv2.1"
PR = "r0"

SRC_URI = "http://www.mega-nerd.com/libsndfile/files/libsndfile-${PV}.tar.gz"

SRC_URI[md5sum] = "8f823c30c1d8d44830db6ab845d6679e"
SRC_URI[sha256sum] = "b6050e6fbfbb72c8bfbc895104697a4af1d49077a64e4846e0be7af87c9e56a4"

LIC_FILES_CHKSUM = "file://COPYING;md5=e77fe93202736b47c07035910f47974a"

S = "${WORKDIR}/libsndfile-${PV}"

inherit autotools lib_package pkgconfig

do_configure_prepend_arm() {
	export ac_cv_sys_largefile_source=1
	export ac_cv_sys_file_offset_bits=64
	ac_cv_sizeof_off_t=8
}

