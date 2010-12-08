require dpkg.inc
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI += "file://noman.patch;patch=1 \
            file://check_snprintf.patch \
            file://check_version.patch"

SRC_URI[md5sum] = "b9b817389e655ec2c12465de5c619011"
SRC_URI[sha256sum] = "2ef55e8eb6c1e8c3dfb54c8ccc9a883fec7540b705c5179ca7a198bebe2f18bc"

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
