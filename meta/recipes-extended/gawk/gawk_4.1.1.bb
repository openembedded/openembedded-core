SUMMARY = "GNU awk text processing utility"
DESCRIPTION = "The GNU version of awk, a text processing utility. \
Awk interprets a special-purpose programming language to do \
quick and easy text pattern matching and reformatting jobs."
HOMEPAGE = "www.gnu.org/software/gawk"
BUGTRACKER  = "bug-gawk@gnu.org"
SECTION = "console/utils"

# gawk <= 3.1.5: GPLv2
# gawk >= 3.1.6: GPLv3
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS += "readline"

PACKAGECONFIG[mpfr] = "--with-mpfr,--without-mpfr, mpfr"

SRC_URI = "${GNU_MIRROR}/gawk/gawk-${PV}.tar.gz \
           file://run-ptest \
           file://Use-DESTDIR-in-extension-Makefile.am-when-removing-..patch \
           file://extension-Add-DESTDIR-prefix-to-remaining-pkgextensi.patch \
"

SRC_URI[md5sum] = "45f5b09aa87b4744c4c53bf274e96ed0"
SRC_URI[sha256sum] = "556464bd2e4bc5a0fad4526b59623e4be40b4c2f4c663dfaaf246af6e2ec1d62"

inherit autotools gettext texinfo update-alternatives

FILES_${PN} += "${datadir}/awk"
FILES_${PN}-dev += "${libdir}/${BPN}/*.la"
FILES_${PN}-dbg += "${libexecdir}/awk/.debug"

ALTERNATIVE_${PN} = "awk"
ALTERNATIVE_TARGET[awk] = "${bindir}/gawk"
ALTERNATIVE_PRIORITY = "100"

do_install_append() {
	# remove the link since we don't package it
	rm ${D}${bindir}/awk
}

inherit ptest

do_install_ptest() {
	mkdir ${D}${PTEST_PATH}/test
	for i in `grep -vE "@|^$|#|Gt-dummy" ${S}/test/Maketests |awk -F: '{print $1}'` Maketests; \
	  do cp ${S}/test/$i* ${D}${PTEST_PATH}/test; \
	done
}
