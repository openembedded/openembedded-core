SUMMARY = "Display or change ethernet card settings"
DESCRIPTION = "A small utility for examining and tuning the settings of your ethernet-based network interfaces."
HOMEPAGE = "http://www.kernel.org/pub/software/network/ethtool/"
SECTION = "console/network"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://ethtool.c;beginline=4;endline=17;md5=c19b30548c582577fc6b443626fc1216"

SRC_URI = "${KERNELORG_MIRROR}/software/network/ethtool/ethtool-${PV}.tar.gz \
           file://run-ptest"

SRC_URI[md5sum] = "0c0c0558a403b478cd3f65dadc00470d"
SRC_URI[sha256sum] = "68f6b6c9417e8ff3d3b5a8bc22fc4fe8846176ee7d7636e365ce6a2e7b1e44e9"

inherit autotools ptest
RDEPENDS_${PN}-ptest += "make"

do_compile_ptest() {
   oe_runmake test-cmdline test-features
}

do_install_ptest () {
   cp ${B}/Makefile                 ${D}${PTEST_PATH}
   install ${B}/test-cmdline        ${D}${PTEST_PATH}
   install ${B}/test-features       ${D}${PTEST_PATH}
   install ${B}/ethtool             ${D}${PTEST_PATH}/ethtool
   sed -i 's/^Makefile/_Makefile/'  ${D}${PTEST_PATH}/Makefile
}
