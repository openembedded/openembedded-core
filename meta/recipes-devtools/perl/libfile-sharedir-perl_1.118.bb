SUMMARY = "Locate per-dist and per-module shared files"
DESCRIPTION = "File::ShareDir provides a way to access read-only \
               data files installed alongside a Perl distribution \
               or module, as installed by \
               File::ShareDir::Install."
HOMEPAGE = "https://metacpan.org/release/File-ShareDir"
BUGTRACKER = "https://rt.cpan.org/Public/Dist/Display.html?Name=File-ShareDir"
SECTION = "libs"
LICENSE = "Artistic-1.0 OR GPL-1.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2a22a4899749b8c0c56bc4de923f05e9"

DEPENDS += "libfile-sharedir-install-perl-native"

SRC_URI = "${CPAN_MIRROR}/authors/id/R/RE/REHSACK/File-ShareDir-${PV}.tar.gz"
SRC_URI[sha256sum] = "3bb2a20ba35df958dc0a4f2306fc05d903d8b8c4de3c8beefce17739d281c958"

S = "${UNPACKDIR}/File-ShareDir-${PV}"

inherit cpan ptest-perl

RDEPENDS:${PN} += "\
    libclass-inspector-perl \
    perl-module-base \
    perl-module-carp \
    perl-module-constant \
    perl-module-cwd \
    perl-module-exporter \
    perl-module-file-spec \
    perl-module-file-spec-functions \
    perl-module-strict \
    perl-module-warnings \
"
RDEPENDS:${PN}-ptest += "\
    perl-module-file-basename \
    perl-module-file-path \
    perl-module-parent \
    perl-module-posix \
    perl-module-test-more \
"

BBCLASSEXTEND = "native nativesdk"
