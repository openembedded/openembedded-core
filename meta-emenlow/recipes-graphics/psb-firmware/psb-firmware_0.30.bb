DESCRIPTION = "Binary firmware for the Poulsbo (psb) 3D X11 driver"
LICENSE = "Intel-binary-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=02c597a2f082b4581596065bb5a521a8"
PR = "r0"

SRC_URI = "https://launchpad.net/~gma500/+archive/ppa/+files/psb-firmware_0.30-0ubuntu1netbook1ubuntu1.tar.gz"

do_install() {
        install -d ${D}${base_libdir}/firmware/
	install -m 0644 ${WORKDIR}/psb-firmware-0.30/msvdx_fw.bin ${D}${base_libdir}/firmware/
}

FILES_${PN} += "${base_libdir}/firmware/msvdx_fw.bin"

COMPATIBLE_MACHINE = "emenlow"
