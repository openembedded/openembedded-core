SUMMARY = "Build Result Compare Script"
DESCRIPTION = "This package contains scripts to find out if the build result\
differs to a former build."
HOMEPAGE = "https://github.com/openSUSE/build-compare"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = "git://github.com/openSUSE/build-compare.git"
SRC_URI += "file://Rename-rpm-check.sh-to-pkg-diff.sh.patch;striplevel=1"
SRC_URI += "file://Ignore-DWARF-sections.patch;striplevel=1"
PATCHTOOL = "git"

SRCREV = "c5352c054c6ef15735da31b76d6d88620f4aff0a"

S = "${WORKDIR}/git"

BBCLASSEXTEND += "native nativesdk"

do_install() {
    install -d ${D}/${bindir}
    install -m 755 functions.sh ${D}/${bindir}
    install -m 755 pkg-diff.sh ${D}/${bindir}
    install -m 755 same-build-result.sh ${D}/${bindir}
    install -m 755 srpm-check.sh ${D}/${bindir}
}

RDEPENDS_${PN} += "bash"
