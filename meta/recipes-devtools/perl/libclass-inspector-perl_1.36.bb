SUMMARY = "Get information about a class and its structure"
DESCRIPTION = "Class::Inspector allows you to get information \
               about a loaded class. Most or all of this \
               information can be found in other ways, but they \
               aren't always very friendly, and usually involve a \
               relatively high level of Perl wizardry, or strange \
               and unusual looking code."
HOMEPAGE = "https://metacpan.org/release/Class-Inspector"
BUGTRACKER = "https://github.com/plicease/Class-Inspector/issues"
SECTION = "libs"
LICENSE = "Artistic-1.0 OR GPL-1.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2a7c9eabf506fa8737d9971cc308b473"

SRC_URI = "${CPAN_MIRROR}/authors/id/P/PL/PLICEASE/Class-Inspector-${PV}.tar.gz"
SRC_URI[sha256sum] = "cc295d23a472687c24489d58226ead23b9fdc2588e522f0b5f0747741700694e"

S = "${UNPACKDIR}/Class-Inspector-${PV}"

inherit cpan ptest-perl

RDEPENDS:${PN} += "\
    perl-module-base \
    perl-module-exporter \
    perl-module-file-spec \
    perl-module-strict \
    perl-module-utf8 \
    perl-module-warnings \
"
RDEPENDS:${PN}-ptest += "\
    perl-module-config \
    perl-module-constant \
    perl-module-test-more \
    perl-module-vars \
"

BBCLASSEXTEND = "native nativesdk"
