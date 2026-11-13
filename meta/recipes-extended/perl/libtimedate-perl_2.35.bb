SUMMARY = "Perl modules useful for manipulating date and time information"
DESCRIPTION = "This is the perl5 TimeDate distribution. It \
               requires perl version 5.003 or later."
HOMEPAGE = "https://metacpan.org/release/TimeDate"
BUGTRACKER = "https://github.com/cpan-authors/TimeDate/issues"
SECTION = "libs"
# You can redistribute it and/or modify it under the same terms as Perl itself.
LICENSE = "Artistic-1.0 OR GPL-1.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=785dacdd4fe95b580e7b020d087ce54e"

SRC_URI = "${CPAN_MIRROR}/authors/id/A/AT/ATOOMIC/TimeDate-${PV}.tar.gz"
SRC_URI[sha256sum] = "baddd0306ae2e86e9ec28d3de5439e514643e80b3735e43bd0fbb426d73304de"

S = "${UNPACKDIR}/TimeDate-${PV}"

inherit cpan ptest-perl

RDEPENDS:${PN} += "\
    perl-module-carp \
    perl-module-exporter \
    perl-module-posix \
    perl-module-strict \
    perl-module-time-local \
"
RDEPENDS:${PN}-ptest += "\
    perl-module-extutils-makemaker \
    perl-module-extutils-mm-unix \
    perl-module-file-spec \
    perl-module-test-more \
    perl-module-utf8 \
    perl-module-warnings \
    tzdata-core \
"

BBCLASSEXTEND = "native"
