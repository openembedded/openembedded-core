DESCRIPTION = "Firmware files for use with Linux kernel"
SECTION = "kernel"
LICENSE = "Proprietary"
LICENSE_${PN}-sd8686 = "Firmware:LICENSE.libertas"
LICENSE_${PN}-rtl8192cu = "Firmware:LICENCE.rtlwifi_firmware"
LICENSE_${PN}-rtl8192ce = "Firmware:LICENCE.rtlwifi_firmware"
LICENSE_${PN}-rtl8192su = "Firmware:LICENCE.rtlwifi_firmware"

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
                    file://LICENCE.rtlwifi_firmware.txt;md5=00d06cfd3eddd5a2698948ead2ad54a5 \
                   "

PROVIDES += "linux-firmware-sd8686 linux-firmware-rtl8192cu linux-firmware-rtl8192ce linux-firmware-rtl8192su"

SRCREV = "40c0f950be7040614dc45df54e25e54d00e3b73b"
PV = "0.0+git${SRCPV}"
PR = "r1"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/dwmw2/linux-firmware.git;protocol=git"

S = "${WORKDIR}/git"

inherit allarch

do_compile() {
	:
}

do_install() {
	install -d  ${D}/lib/firmware/
	# Libertas sd8686
	install -m 0644 libertas/sd8686_v9.bin ${D}/lib/firmware/sd8686.bin
	install -m 0644 libertas/sd8686_v9_helper.bin ${D}/lib/firmware/sd8686_helper.bin
	install -m 0644 LICENCE.libertas ${D}/lib/firmware/
	# Realtek rtl8192* 
	install -d ${D}/lib/firmware/rtlwifi/
	install -m 0644 rtlwifi/rtl8192cufw.bin ${D}/lib/firmware/rtlwifi/rtl8192cufw.bin
	install -m 0644 rtlwifi/rtl8192cfw.bin ${D}/lib/firmware/rtlwifi/rtl8192cfw.bin
	install -m 0644 rtlwifi/rtl8712u.bin ${D}/lib/firmware/rtlwifi/rtl8712u.bin	
	install -m 0644 LICENCE.rtlwifi_firmware.txt ${D}/lib/firmware/rtlwifi/LICENCE.rtlwifi_firmware.txt
}

PACKAGES = "${PN}-sd8686 ${PN}-rtl8192cu linux-firmware-rtl8192ce linux-firmware-rtl8192su"

FILES_${PN}-sd8686 = "/lib/firmware/sd8686* /lib/firmware/LICENCE.libertas"

RPROVIDES_${PN}-sd8686 = "${PN}-sd8686"

FILES_${PN}-rtl8192cu = " \
  /lib/firmware/rtlwifi/rtl8192cufw.bin \
  /lib/firmware/rtlwifi/LICENCE.rtlwifi_firmware.txt \
"
RPROVIDES_${PN}-rtl8192cu = "${PN}-rtl8192cu"

FILES_${PN}-rtl8192cu = " \
  /lib/firmware/rtlwifi/rtl8192cufw.bin \
  /lib/firmware/rtlwifi/LICENCE.rtlwifi_firmware.txt \
"
RPROVIDES_${PN}-rtl8192cu = "${PN}-rtl8192cu"

FILES_${PN}-rtl8192ce = " \
  /lib/firmware/rtlwifi/rtl8192cfw.bin \
  /lib/firmware/rtlwifi/LICENCE.rtlwifi_firmware.txt \
"
RPROVIDES_${PN}-rtl8192ce = "${PN}-rtl8192ce"

FILES_${PN}-rtl8192su = " \
  /lib/firmware/rtlwifi/rtl8712u.bin \
  /lib/firmware/rtlwifi/LICENCE.rtlwifi_firmware.txt \
"
RPROVIDES_${PN}-rtl8192su = "${PN}-rtl8192su"

