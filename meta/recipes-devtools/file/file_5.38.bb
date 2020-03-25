SUMMARY = "File classification tool"
DESCRIPTION = "File attempts to classify files depending \
on their contents and prints a description if a match is found."
HOMEPAGE = "http://www.darwinsys.com/file/"
SECTION = "console/utils"

# two clause BSD
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYING;beginline=2;md5=0251eaec1188b20d9a72c502ecfdda1b"

DEPENDS = "zlib file-replacement-native"
DEPENDS_class-native = "zlib-native"

SRC_URI = "git://github.com/file/file.git"

SRCREV = "ec41083645689a787cdd00cb3b5bf578aa79e46c"
S = "${WORKDIR}/git"

inherit autotools update-alternatives

EXTRA_OECONF += "--disable-libseccomp"

ALTERNATIVE_${PN} = "file"
ALTERNATIVE_LINK_NAME[file] = "${bindir}/file"

EXTRA_OEMAKE_append_class-target = "-e FILE_COMPILE=${STAGING_BINDIR_NATIVE}/file-native/file"
EXTRA_OEMAKE_append_class-nativesdk = "-e FILE_COMPILE=${STAGING_BINDIR_NATIVE}/file-native/file"

FILES_${PN} += "${datadir}/misc/*.mgc"

do_compile_append_class-native() {
	oe_runmake check
}

do_install_append_class-native() {
	create_cmdline_wrapper ${D}/${bindir}/file \
		--magic-file ${datadir}/misc/magic.mgc
}

do_install_append_class-nativesdk() {
	create_cmdline_wrapper ${D}/${bindir}/file \
		--magic-file ${datadir}/misc/magic.mgc
}

BBCLASSEXTEND = "native nativesdk"
PROVIDES_append_class-native = " file-replacement-native"
# Don't use NATIVE_PACKAGE_PATH_SUFFIX as that hides libmagic from anyone who
# depends on file-replacement-native.
bindir_append_class-native = "/file-native"
