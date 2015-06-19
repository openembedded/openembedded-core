SUMMARY = "Firmware files for use with Linux kernel"
SECTION = "kernel"
# Notes:
# This is kind of a mess. Each bit of firmware has their own license. Some free
# some not. Leaving this as Proprietary for now, but this recipe should be probably
# be rethought out a bit more around how it deals with licenses.

LICENSE = "Proprietary"

LIC_FILES_CHKSUM = "file://LICENSE.radeon;md5=07b0c31777bd686d8e1609c6940b5e74\
                    file://LICENSE.dib0700;md5=f7411825c8a555a1a3e5eab9ca773431 \
                    file://LICENCE.xc5000;md5=1e170c13175323c32c7f4d0998d53f66 \
                    file://LICENCE.ralink-firmware.txt;md5=ab2c269277c45476fb449673911a2dfd \
                    file://LICENCE.qla2xxx;md5=f5ce8529ec5c17cb7f911d2721d90e91 \
                    file://LICENCE.iwlwifi_firmware;md5=5106226b2863d00d8ed553221ddf8cd2 \
                    file://LICENCE.i2400m;md5=14b901969e23c41881327c0d9e4b7d36 \
                    file://LICENCE.atheros_firmware;md5=30a14c7823beedac9fa39c64fdd01a13 \
                    file://LICENCE.agere;md5=af0133de6b4a9b2522defd5f188afd31 \
                    file://LICENCE.rtlwifi_firmware.txt;md5=00d06cfd3eddd5a2698948ead2ad54a5 \
                    file://LICENCE.broadcom_bcm43xx;md5=3160c14df7228891b868060e1951dfbc \
                    file://LICENCE.ti-connectivity;md5=186e7a43cf6c274283ad81272ca218ea \
                    file://LICENCE.atheros_firmware;md5=30a14c7823beedac9fa39c64fdd01a13 \
                    file://LICENCE.via_vt6656;md5=e4159694cba42d4377a912e78a6e850f \
                    file://LICENCE.Marvell;md5=9ddea1734a4baf3c78d845151f42a37a \
                   "

SRCREV = "ec89525b2ab65f1d5ae4f67e27f0d525ddedd2ef"
PE = "1"
PV = "0.0+git${SRCPV}"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git"

S = "${WORKDIR}/git"

inherit allarch update-alternatives

do_compile() {
	:
}

do_install() {
	install -d  ${D}/lib/firmware/
	cp -r * ${D}/lib/firmware/

	# Avoid Makefile to be deplyed
	rm ${D}/lib/firmware/Makefile

	# Remove unbuild firmware which needs cmake and bash
	rm ${D}/lib/firmware/carl9170fw -rf

	# Remove pointless bash script
	rm ${D}/lib/firmware/configure

	# Libertas sd8686
	ln -sf libertas/sd8686_v9.bin ${D}/lib/firmware/sd8686.bin
	ln -sf libertas/sd8686_v9_helper.bin ${D}/lib/firmware/sd8686_helper.bin

	# fixup wl12xx location, after 2.6.37 the kernel searches a different location for it
	( cd ${D}/lib/firmware ; ln -sf ti-connectivity/* . )
}


PACKAGES =+ "${PN}-ralink \
             ${PN}-radeon \
             ${PN}-marvell-license ${PN}-sd8686 ${PN}-sd8787 ${PN}-sd8797 ${PN}-pcie8897 ${PN}-usb8897 \
             ${PN}-wl12xx ${PN}-wl18xx ${PN}-vt6656 \
             ${PN}-rtl-license ${PN}-rtl8192cu ${PN}-rtl8192ce ${PN}-rtl8192su \
             ${PN}-broadcom-license ${PN}-bcm4329 ${PN}-bcm4330 ${PN}-bcm4334 \
             ${PN}-atheros-license ${PN}-ar9170 ${PN}-ar3k ${PN}-ath6k ${PN}-ath9k \
             ${PN}-iwlwifi-license ${PN}-iwlwifi-6000g2a-5 ${PN}-iwlwifi-6000g2b-6 ${PN}-iwlwifi-7260-7 \
             ${PN}-iwlwifi-6000g2a-6 ${PN}-iwlwifi-135-6"


FILES_${PN}-atheros-license = "/lib/firmware/LICENCE.atheros_firmware"

LICENSE_${PN}-9170 = "Firmware-atheros_firmware"
FILES_${PN}-ar9170 = " \
  /lib/firmware/ar9170*.fw \
"
RDEPENDS_${PN}-ar9170 += "${PN}-atheros-license"

LICENSE_${PN}-ar3k = "Firmware-atheros_firmware"
FILES_${PN}-ar3k = " \
  /lib/firmware/ar3k \
"
RDEPENDS_${PN}-ath6k += "${PN}-atheros-license"

LICENSE_${PN}-ath6k = "Firmware-atheros_firmware"
FILES_${PN}-ath6k = " \
  /lib/firmware/ath6k \
"
RDEPENDS_${PN}-ath6k += "${PN}-atheros-license"

LICENSE_${PN}-ath9k = "Firmware-atheros_firmware"
FILES_${PN}-ath9k = " \
  /lib/firmware/ar9271.fw \
  /lib/firmware/ar7010*.fw \
  /lib/firmware/htc_9271.fw \
  /lib/firmware/htc_7010.fw \
"
RDEPENDS_${PN}-ath9k += "${PN}-atheros-license"

LICENSE_${PN}-ralink = "Firmware-ralink"
FILES_${PN}-ralink = " \
  /lib/firmware/rt*.bin \
  /lib/firmware/LICENCE.ralink-firmware.txt \
"

LICENSE_${PN}-radeon = "Firmware-radeon"
FILES_${PN}-radeon = " \
  /lib/firmware/radeon \
  /lib/firmware/LICENCE.radeon \
"

FILES_${PN}-marvell-license = "/lib/firmware/LICENCE.Marvell"

LICENSE_${PN}-sd8686 = "Firmware-Marvell"
FILES_${PN}-sd8686 = " \
  /lib/firmware/libertas/sd8686_v9* \
  /lib/firmware/sd8686* \
"
RDEPENDS_${PN}-sd8686 += "${PN}-marvell-license"

LICENSE_${PN}-sd8787 = "Firmware-Marvell"
FILES_${PN}-sd8787 = " \
  /lib/firmware/mrvl/sd8787_uapsta.bin \
"
RDEPENDS_${PN}-sd8787 += "${PN}-marvell-license"

LICENSE_${PN}-sd8797 = "Firmware-Marvell"
FILES_${PN}-sd8797 = " \
  /lib/firmware/mrvl/sd8797_uapsta.bin \
"
RDEPENDS_${PN}-sd8797 += "${PN}-marvell-license"

LICENSE_${PN}-pcie8897 = "Firmware-Marvell"
FILES_${PN}-pcie8897 = " \
  /lib/firmware/mrvl/pcie8897_uapsta.bin \
"
RDEPENDS_${PN}-pcie8897 += "${PN}-marvell-license"

LICENSE_${PN}-usb8897 = "Firmware-Marvell"
FILES_${PN}-usb8897 = " \
  /lib/firmware/mrvl/usb8897_uapsta.bin \
"
RDEPENDS_${PN}-usb8897 += "${PN}-marvell-license"


FILES_${PN}-rtl-license = " \
  /lib/firmware/LICENCE.rtlwifi_firmware.txt \
"

LICENSE_${PN}-rtl8192cu = "Firmware-rtlwifi"
FILES_${PN}-rtl8192cu = " \
  /lib/firmware/rtlwifi/rtl8192cufw.bin \
"
RDEPENDS_${PN}-rtl8192cu += "${PN}-rtl-license"

LICENSE_${PN}-rtl8192ce = "Firmware-rtlwifi"
FILES_${PN}-rtl8192ce = " \
  /lib/firmware/rtlwifi/rtl8192cfw.bin \
"
RDEPENDS_${PN}-rtl8192ce += "${PN}-rtl-license"


LICENSE_${PN}-rtl8192su = "Firmware-rtlwifi"
FILES_${PN}-rtl8192su = " \
  /lib/firmware/rtlwifi/rtl8712u.bin \
"

LICENSE_${PN}-wl12xx = "Firmware-ti-connectivity"
FILES_${PN}-wl12xx = " \
  /lib/firmware/wl12* \
  /lib/firmware/TI* \
  /lib/firmware/ti-connectivity \
"

LICENSE_${PN}-wl18xx = "Firmware-ti-connectivity"
FILES_${PN}-wl18xx = " \
  /lib/firmware/wl18* \
  /lib/firmware/TI* \
  /lib/firmware/ti-connectivity \
"

LICENSE_${PN}-vt6656 = "Firmware-via_vt6656"
FILES_${PN}-vt6656 = " \
  /lib/firmware/vntwusb.fw \
"

# WARNING: The ALTERNATIVE_* variables are not using ${PN} because of
# a bug in bitbake; when this is fixed and bitbake learns how to proper
# pass variable flags with expansion we can rework this patch.

ALTERNATIVE_LINK_NAME[brcmfmac-sdio.bin] = "/lib/firmware/brcm/brcmfmac-sdio.bin"

FILES_${PN}-broadcom-license = " \
  /lib/firmware/LICENCE.broadcom_bcm43xx \
"

LICENSE_${PN}-bcm4329 = "Firmware-bcm4329"
FILES_${PN}-bcm4329 = " \
  /lib/firmware/brcm/brcmfmac4329-sdio.bin \
"
RDEPENDS_${PN}-bcm4329 += "${PN}-broadcom-license"
ALTERNATIVE_linux-firmware-bcm4329 = "brcmfmac-sdio.bin"
ALTERNATIVE_TARGET_linux-firmware-bcm4329[brcmfmac-sdio.bin] = "/lib/firmware/brcm/brcmfmac4329-sdio.bin"

LICENSE_${PN}-bcm4330 = "Firmware-bcm4330"
FILES_${PN}-bcm4330 = " \
  /lib/firmware/brcm/brcmfmac4330-sdio.bin \
"
RDEPENDS_${PN}-bcm4330 += "${PN}-broadcom-license"
ALTERNATIVE_linux-firmware-bcm4330 = "brcmfmac-sdio.bin"
ALTERNATIVE_TARGET_linux-firmware-bcm4330[brcmfmac-sdio.bin] = "/lib/firmware/brcm/brcmfmac4330-sdio.bin"

LICENSE_${PN}-bcm4334 = "Firmware-bcm4334"
FILES_${PN}-bcm4334 = " \
  /lib/firmware/brcm/brcmfmac4334-sdio.bin \
"
RDEPENDS_${PN}-bcm4334 += "${PN}-broadcom-license"
ALTERNATIVE_linux-firmware-bcm4334 = "brcmfmac-sdio.bin"
ALTERNATIVE_TARGET_linux-firmware-bcm4334[brcmfmac-sdio.bin] = "/lib/firmware/brcm/brcmfmac4334-sdio.bin"

RDEPENDS_${PN}-iwlwifi-6000g2a-5 = "${PN}-iwlwifi-license"
RDEPENDS_${PN}-iwlwifi-6000g2a-6 = "${PN}-iwlwifi-license"
RDEPENDS_${PN}-iwlwifi-6000g2b-6 = "${PN}-iwlwifi-license"
RDEPENDS_${PN}-iwlwifi-135-6 = "${PN}-iwlwifi-license"
RDEPENDS_${PN}-iwlwifi-7260-7 = "${PN}-iwlwifi-license"

FILES_${PN}-iwlwifi-license =   "/lib/firmware/LICENCE.iwlwifi_firmware"
FILES_${PN}-iwlwifi-6000g2a-5 = "/lib/firmware/iwlwifi-6000g2a-5.ucode"
FILES_${PN}-iwlwifi-6000g2a-6 = "/lib/firmware/iwlwifi-6000g2a-6.ucode"
FILES_${PN}-iwlwifi-6000g2b-6 = "/lib/firmware/iwlwifi-6000g2b-6.ucode"
FILES_${PN}-iwlwifi-135-6 =     "/lib/firmware/iwlwifi-135-6.ucode"
FILES_${PN}-iwlwifi-7260-7 = "/lib/firmware/iwlwifi-7260-7.ucode"

FILES_${PN} += "/lib/firmware/*"

# Make linux-firmware depend on all of the split-out packages.
python populate_packages_prepend () {
    firmware_pkgs = oe.utils.packages_filter_out_system(d)
    d.appendVar('RDEPENDS_linux-firmware', ' ' + ' '.join(firmware_pkgs))
}
