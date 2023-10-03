SUMMARY = "GNU awk text processing utility"
DESCRIPTION = "The GNU version of awk, a text processing utility. \
Awk interprets a special-purpose programming language to do \
quick and easy text pattern matching and reformatting jobs."
HOMEPAGE = "https://www.gnu.org/software/gawk/"
BUGTRACKER  = "bug-gawk@gnu.org"
SECTION = "console/utils"

# gawk <= 3.1.5: GPLv2
# gawk >= 3.1.6: GPLv3
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PACKAGECONFIG ??= "readline"
PACKAGECONFIG[readline] = "--with-readline,--without-readline,readline"
PACKAGECONFIG[mpfr] = "--with-mpfr,--without-mpfr, mpfr"

SRC_URI = "${GNU_MIRROR}/gawk/gawk-${PV}.tar.gz \
           file://remove-sensitive-tests.patch \
           file://run-ptest \
           file://CVE-2023-4156.patch \
"

SRC_URI[md5sum] = "c5441c73cc451764055ee65e9a4292bb"
SRC_URI[sha256sum] = "625bf3718e25a84dc4486135d5cb5388174682362c70107fd13f21572f5603bb"

inherit autotools gettext texinfo update-alternatives

FILES_${PN} += "${datadir}/awk"
FILES_${PN}-dev += "${libdir}/${BPN}/*.la"

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
	ln -s ${bindir}/gawk ${D}${PTEST_PATH}/gawk
	# The list of tests is all targets in Maketests, apart from the dummy Gt-dummy
	TESTS=$(awk -F: '$1 == "Gt-dummy" { next } /[[:alnum:]]+:$/ { print $1 }' ${S}/test/Maketests)
	for i in $TESTS Maketests inclib.awk; do
		cp ${S}/test/$i* ${D}${PTEST_PATH}/test
	done
	sed -i -e 's|/usr/local/bin|${bindir}|g' \
	    -e 's|#!${base_bindir}/awk|#!${bindir}/awk|g' ${D}${PTEST_PATH}/test/*.awk

	sed -i -e "s|GAWKLOCALE|LANG|g" ${D}${PTEST_PATH}/test/Maketests

	# These tests require an unloaded host as otherwise timing sensitive tests can fail
	# https://bugzilla.yoctoproject.org/show_bug.cgi?id=14371
	rm -f ${D}${PTEST_PATH}/test/time.*
	rm -f ${D}${PTEST_PATH}/test/timeout.*
}

RDEPENDS_${PN}-ptest += "make"

BBCLASSEXTEND = "native nativesdk"
