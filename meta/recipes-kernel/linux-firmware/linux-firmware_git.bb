DESCRIPTION = "Firmware files for use with Linux kernel"
SECTION = "kernel"
LICENSE = "Proprietary"
LICENSE_${PN}-sd8686 = "Firmware:LICENSE.libertas"

LIC_FILES_CHKSUM = "file://LICENSE.radeon_rlc;md5=4c243f7854d2884b483abda991adef43 \
                    file://LICENSE.dib0700;md5=f7411825c8a555a1a3e5eab9ca773431 \
                    file://LICENCE.xc5000;md5=1e170c13175323c32c7f4d0998d53f66 \
                    file://LICENCE.ralink-firmware.txt;md5=ab2c269277c45476fb449673911a2dfd \
                    file://LICENCE.qla2xxx;md5=4005328a134054f0fa077bdc37aa64f2 \
                    file://LICENCE.mwl8k;md5=9ddea1734a4baf3c78d845151f42a37a \
                    file://LICENCE.libertas;md5=2d2127d203ac000f1afabfce593659ce \
                    file://LICENCE.iwlwifi_firmware;md5=311cc823df5b1be4f00fbf0f17d96a6b \
                    file://LICENCE.i2400m;md5=14b901969e23c41881327c0d9e4b7d36 \
                    file://LICENCE.atheros_firmware;md5=62748c8fecfa12010fd76409db4b5459 \
                    file://LICENCE.agere;md5=af0133de6b4a9b2522defd5f188afd31 \
                   "

PROVIDES += "linux-firmware-sd8686"

SRCREV = "40c0f950be7040614dc45df54e25e54d00e3b73b"
PV = "0.0+git${SRCPV}"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/dwmw2/linux-firmware.git;protocol=git"

S = "${WORKDIR}/git"

do_compile() {
	:
}

do_install() {
	install -d  ${D}/lib/firmware/
	# Libertas sd8686
	install -m 0644 libertas/sd8686_v9.bin ${D}/lib/firmware/sd8686.bin
	install -m 0644 libertas/sd8686_v9_helper.bin ${D}/lib/firmware/sd8686_helper.bin
	install -m 0644 LICENCE.libertas ${D}/lib/firmware/
}

PACKAGES = "${PN}-sd8686"

FILES_${PN}-sd8686 = "/lib/firmware/sd8686* /lib/firmware/LICENCE.libertas"

RPROVIDES_${PN}-sd8686 = "${PN}-sd8686"

PACKAGE_ARCH = "all"
