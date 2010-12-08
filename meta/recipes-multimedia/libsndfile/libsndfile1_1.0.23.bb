DESCRIPTION = "An audio format Conversion library"
HOMEPAGE = "http://www.mega-nerd.com/libsndfile"
AUTHOR = "Erik de Castro Lopo"
DEPENDS = "sqlite3"
SECTION = "libs/multimedia"
LICENSE = "LGPLv2.1"
PR = "r0"

SRC_URI = "http://www.mega-nerd.com/libsndfile/files/libsndfile-${PV}.tar.gz"

SRC_URI[md5sum] = "d0e22b5ff2ef945615db33960376d733"
SRC_URI[sha256sum] = "54c9c375598538263395a691f9b30987a2faa3d8a166d27e6a09ba4700223d4d"

LIC_FILES_CHKSUM = "file://COPYING;md5=e77fe93202736b47c07035910f47974a"

S = "${WORKDIR}/libsndfile-${PV}"

inherit autotools lib_package pkgconfig

do_configure_prepend_arm() {
	export ac_cv_sys_largefile_source=1
	export ac_cv_sys_file_offset_bits=64
	ac_cv_sizeof_off_t=8
}

