SUMMARY = "in-depth comparison of files, archives, and directories"
HOMEPAGE = "https://diffoscope.org/"
LICENSE = "GPL-3.0+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PYPI_PACKAGE = "diffoscope"

inherit pypi setuptools3

SRC_URI[sha256sum] = "d95cef5b3eef49fa1c811c1ac103f7f7cca4a0ebabc674e4283b51f28309d242"

RDEPENDS_${PN} += "binutils vim squashfs-tools python3-libarchive-c python3-magic python3-rpm"

# Dependencies don't build for musl
COMPATIBLE_HOST_libc-musl = 'null'

do_install_append_class-native() {
	create_wrapper ${D}${bindir}/diffoscope MAGIC=${STAGING_DIR_NATIVE}${datadir_native}/misc/magic.mgc
}

BBCLASSEXTEND = "native"
