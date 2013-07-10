LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

require diffutils.inc

SRC_URI = "${GNU_MIRROR}/diffutils/diffutils-${PV}.tar.xz"

do_configure_prepend () {
	# Need to remove gettext macros with weird mix of versions
	for i in codeset.m4 gettext_gl.m4 intlmacosx.m4 inttypes-pri.m4 lib-ld_gl.m4 lib-prefix_gl.m4 po_gl.m4 ssize_t.m4 wchar_t.m4 wint_t.m4; do
		rm -f ${S}/m4/$i
	done
}

SRC_URI[md5sum] = "99180208ec2a82ce71f55b0d7389f1b3"
SRC_URI[sha256sum] = "a25e89a8ab65fded1731e4186be1bb25cda967834b6df973599cdcd5abdfc19c"
