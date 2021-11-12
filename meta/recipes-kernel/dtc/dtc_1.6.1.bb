SUMMARY = "Device Tree Compiler"
HOMEPAGE = "https://devicetree.org/"
DESCRIPTION = "The Device Tree Compiler is a tool used to manipulate the Open-Firmware-like device tree used by PowerPC kernels."
SECTION = "bootloader"
LICENSE = "GPLv2 | BSD-2-Clause"
DEPENDS = "flex-native bison-native"

LIC_FILES_CHKSUM = "file://GPL;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://libfdt/libfdt.h;beginline=4;endline=7;md5=05bb357cfb75cae7d2b01d2ee8d76407"

SRC_URI = "git://git.kernel.org/pub/scm/utils/dtc/dtc.git;branch=master"
SRCREV = "b6910bec11614980a21e46fbccc35934b671bd81"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\d+(\.\d+)+)"

S = "${WORKDIR}/git"

inherit meson pkgconfig

EXTRA_OEMESON = "-Dpython=disabled -Dvalgrind=disabled"

PACKAGES =+ "${PN}-misc"
FILES:${PN}-misc = "${bindir}/convert-dtsv0 ${bindir}/ftdump ${bindir}/dtdiff"

RDEPENDS:${PN}-misc += "bash diffutils"

BBCLASSEXTEND = "native nativesdk"
