require dpkg.inc
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI += "file://noman.patch;patch=1 \
            file://check_snprintf.patch \
            file://check_version.patch"

PR = "r3"

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
