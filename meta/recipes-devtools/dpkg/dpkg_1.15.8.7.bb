require dpkg.inc
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI += "file://noman.patch \
            file://check_snprintf.patch \
            file://check_version.patch"

SRC_URI[md5sum] = "d1731d4147c1ea3b537a4d094519a6dc"
SRC_URI[sha256sum] = "1ec1376471b04717a4497e5d7a27cd545248c92116898ce0c53ced8ea94267b5"

PR = "${INC_PR}.0"

EXTRA_OECONF = "--without-static-progs \
		--without-dselect \
		--with-start-stop-daemon \
		--with-zlib \
		--with-bz2lib \
		--without-selinux \
		--without-sgml-doc"

BBCLASSEXTEND = "native"

do_install_append_virtclass-native () {
	rm ${D}${bindir}/update-alternatives
}
