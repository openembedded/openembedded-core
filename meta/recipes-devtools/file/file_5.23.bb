SUMMARY = "File classification tool"
DESCRIPTION = "File attempts to classify files depending \
on their contents and prints a description if a match is found."
HOMEPAGE = "http://www.darwinsys.com/file/"
SECTION = "console/utils"

# two clause BSD
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;beginline=2;md5=6a7382872edb68d33e1a9398b6e03188"

DEPENDS = "zlib file-native"
DEPENDS_class-native = "zlib-native"

SRC_URI = "ftp://ftp.astron.com/pub/file/${BP}.tar.gz \
	   file://debian-742262.patch \
	   file://0001-Add-P-prompt-into-Usage-info.patch \
	   file://0001-Fix-bug-with-long-options-and-explicitly-number-them.patch \
	   file://0002-fix-bug-with-5.23-long-options.patch \
          "

SRC_URI[md5sum] = "61db35209ce71a6d576392ce6e1d2f80"
SRC_URI[sha256sum] = "2c8ab3ff143e2cdfb5ecee381752f80a79e0b4cfe9ca4cc6e1c3e5ec15e6157c"

inherit autotools

FILES_${PN} += "${datadir}/misc/*.mgc"

do_install_append_class-native() {
	create_cmdline_wrapper ${D}/${bindir}/file \
		--magic-file ${datadir}/misc/magic.mgc
}

do_install_append_class-nativesdk() {
	create_cmdline_wrapper ${D}/${bindir}/file \
		--magic-file ${datadir}/misc/magic.mgc
}

BBCLASSEXTEND = "native nativesdk"
