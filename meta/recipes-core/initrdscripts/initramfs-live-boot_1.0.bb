SUMMARY = "Live image init script"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
DEPENDS = "virtual/kernel"
RDEPENDS_${PN} = "udev udev-extraconf"
SRC_URI = "file://init-live.sh"

PR = "r12"

do_install() {
        install -m 0755 ${WORKDIR}/init-live.sh ${D}/init
}

FILES_${PN} += " /init "

# Due to kernel dependency
PACKAGE_ARCH = "${MACHINE_ARCH}"
