SUMMARY = "Incredibly simple helpers for testing code with exceptions"
DESCRIPTION = "Test::Fatal is an alternative to the popular Test::Exception.\
It does much less, but should allow greater flexibility in testing \
exception-throwing code with about the same amount of typing."
HOMEPAGE = "https://github.com/rjbs/Test-Fatal"
BUGTRACKER = "https://github.com/rjbs/Test-Fatal/issues"
SECTION = "libs"
LICENSE = "Artistic-1.0 | GPL-1.0-or-later"

LIC_FILES_CHKSUM = "file://LICENSE;md5=f7c73bf24be1bc7759bde9c15000db6c"

SRC_URI = "${CPAN_MIRROR}/authors/id/R/RJ/RJBS/Test-Fatal-${PV}.tar.gz"

SRC_URI[sha256sum] = "7283d430f2ba2030b8cd979ae3039d3f1b2ec3dde1a11ca6ae09f992a66f788f"

S = "${WORKDIR}/Test-Fatal-${PV}"

inherit cpan ptest-perl

RDEPENDS:${PN} += "\
    libtry-tiny-perl \
    perl-module-carp \
    perl-module-exporter \
    perl-module-test-builder \
"

RDEPENDS:${PN}-ptest += "\
    perl-module-extutils-makemaker \
    perl-module-extutils-mm-unix \
    perl-module-file-spec \
    perl-module-overload \
    perl-module-test-builder-tester \
    perl-module-test-more \
"

BBCLASSEXTEND = "native nativesdk"
