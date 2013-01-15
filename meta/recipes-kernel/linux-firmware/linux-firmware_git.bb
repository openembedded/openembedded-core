DESCRIPTION = "Firmware files for use with Linux kernel"
SECTION = "kernel"
# Notes:
# This is kind of a mess. Each bit of firmware has their own license. Some free
# some not. Leaving this as Proprietary for now, but this recipe should be probably
# be rethought out a bit more around how it deals with licenses.

LICENSE = "Proprietary"

LIC_FILES_CHKSUM = "file://LICENSE.radeon;md5=e56b405656593a0c97e478513051ea0e \
                    file://LICENSE.dib0700;md5=f7411825c8a555a1a3e5eab9ca773431 \
                    file://LICENCE.xc5000;md5=1e170c13175323c32c7f4d0998d53f66 \
                    file://LICENCE.ralink-firmware.txt;md5=ab2c269277c45476fb449673911a2dfd \
                    file://LICENCE.qla2xxx;md5=4005328a134054f0fa077bdc37aa64f2 \
                    file://LICENCE.iwlwifi_firmware;md5=11545778abf78c43d7644d4f171ea1c7 \
                    file://LICENCE.i2400m;md5=14b901969e23c41881327c0d9e4b7d36 \
                    file://LICENCE.atheros_firmware;md5=30a14c7823beedac9fa39c64fdd01a13 \
                    file://LICENCE.agere;md5=af0133de6b4a9b2522defd5f188afd31 \
                    file://LICENCE.rtlwifi_firmware.txt;md5=00d06cfd3eddd5a2698948ead2ad54a5 \
                   "

SRCREV = "0d6729f2b0bc6254d3628eb831bc80a36b5a17db"
PE = "1"
PV = "0.0+git${SRCPV}"
PR = "r7"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git;protocol=git"

S = "${WORKDIR}/git"

inherit allarch

do_compile() {
	:
}

do_install() {
	install -d  ${D}/lib/firmware/
	cp -r * ${D}/lib/firmware/

	# Libertas sd8686
	ln -sf libertas/sd8686_v9.bin ${D}/lib/firmware/sd8686.bin
	ln -sf libertas/sd8686_v9_helper.bin ${D}/lib/firmware/sd8686_helper.bin

	# Realtek rtl8192* 
	install -m 0644 LICENCE.rtlwifi_firmware.txt ${D}/lib/firmware/rtlwifi/LICENCE.rtlwifi_firmware.txt

	# fixup wl12xx location, after 2.6.37 the kernel searches a different location for it
	( cd ${D}/lib/firmware ; ln -sf ti-connectivity/* . )
}

PACKAGES =+ "${PN}-ralink ${PN}-sd8686 ${PN}-rtl8192cu linux-firmware-rtl8192ce linux-firmware-rtl8192su ${PN}-wl12xx"

LICENSE_${PN}-ralink = "Firmware-ralink"
FILES_${PN}-ralink = " \
  /lib/firmware/rt*.bin \
  /lib/firmware/LICENSE.ralink_firmware.txt \
"

LICENSE_${PN}-sd8686 = "Firmware-libertas"
FILES_${PN}-sd8686 = " \
  /lib/firmware/libertas/sd8686_v9* \
  /lib/firmware/sd8686* \
  /lib/firmware/LICENCE.libertas \
"

LICENSE_${PN}-rtl8192cu = "Firmware-rtlwifi"
FILES_${PN}-rtl8192cu = " \
  /lib/firmware/rtlwifi/rtl8192cufw.bin \
  /lib/firmware/rtlwifi/LICENCE.rtlwifi_firmware.txt \
"

LICENSE_${PN}-rtl8192ce = "Firmware-rtlwifi"
FILES_${PN}-rtl8192ce = " \
  /lib/firmware/rtlwifi/rtl8192cfw.bin \
"

LICENSE_${PN}-rtl8192su = "Firmware-rtlwifi"
FILES_${PN}-rtl8192su = " \
  /lib/firmware/rtlwifi/rtl8712u.bin \
"

FILES_${PN}-wl12xx = " \
  /lib/firmware/wl12* \
  /lib/firmware/TI* \
  /lib/firmware/ti-connectivity \
"

FILES_${PN} += "/lib/firmware/*"
