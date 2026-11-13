SUMMARY = "Install shared files"
DESCRIPTION = "File::ShareDir::Install allows you to install \
               read-only data files from a distribution. It is a \
               companion module to File::ShareDir, which allows \
               you to locate these files after installation."
HOMEPAGE = "https://metacpan.org/release/File-ShareDir-Install"
BUGTRACKER = "https://rt.cpan.org/Public/Dist/Display.html?Name=File-ShareDir-Install"
SECTION = "libs"
LICENSE = "Artistic-1.0 OR GPL-1.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7a8070807ef71bcb5c0523e6dfa84727"

SRC_URI = "${CPAN_MIRROR}/authors/id/E/ET/ETHER/File-ShareDir-Install-${PV}.tar.gz"
SRC_URI[sha256sum] = "8f9533b198f2d4a9a5288cbc7d224f7679ad05a7a8573745599789428bc5aea0"

S = "${UNPACKDIR}/File-ShareDir-Install-${PV}"

inherit cpan ptest-perl

RDEPENDS:${PN} += "\
    perl-module-carp \
    perl-module-exporter \
    perl-module-extutils-makemaker \
    perl-module-extutils-mm-unix \
    perl-module-file-spec \
    perl-module-io-dir \
    perl-module-strict \
    perl-module-warnings \
"
RDEPENDS:${PN}-ptest += "\
    perl-module-config \
    perl-module-file-path \
    perl-module-test-more \
"

BBCLASSEXTEND = "native nativesdk"
