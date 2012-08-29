DESCRIPTION = "Merge machine and distro options to create a basic machine task/package"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r75"

inherit packagegroup

PROVIDES = "${PACKAGES}"
PACKAGES = ' \
            packagegroup-base \
            packagegroup-base-extended \
            packagegroup-distro-base \
            packagegroup-machine-base \
            \
            ${@base_contains("MACHINE_FEATURES", "acpi", "packagegroup-base-acpi", "",d)} \
            ${@base_contains("MACHINE_FEATURES", "alsa", "packagegroup-base-alsa", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "apm", "packagegroup-base-apm", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "ext2", "packagegroup-base-ext2", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "vfat", "packagegroup-base-vfat", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "irda", "packagegroup-base-irda", "",d)} \
            ${@base_contains("MACHINE_FEATURES", "keyboard", "packagegroup-base-keyboard", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "pci", "packagegroup-base-pci", "",d)} \
            ${@base_contains("MACHINE_FEATURES", "pcmcia", "packagegroup-base-pcmcia", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "phone", "packagegroup-base-phone", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "screen", "packagegroup-base-screen", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "serial", "packagegroup-base-serial", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "touchscreen", "packagegroup-base-touchscreen", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "usbgadget", "packagegroup-base-usbgadget", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "usbhost", "packagegroup-base-usbhost", "", d)} \
            \
            ${@base_contains("MACHINE_FEATURES", "uboot", "packagegroup-base-uboot", "",d)} \
            ${@base_contains("MACHINE_FEATURES", "redboot", "packagegroup-base-redboot", "",d)} \
            ${@base_contains("MACHINE_FEATURES", "apex", "packagegroup-base-apex", "",d)} \
	    \
            ${@base_contains("DISTRO_FEATURES", "bluetooth", "packagegroup-base-bluetooth", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "wifi", "packagegroup-base-wifi", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "3g", "packagegroup-base-3g", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "cramfs", "packagegroup-base-cramfs", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "ipsec", "packagegroup-base-ipsec", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "ipv6", "packagegroup-base-ipv6", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "nfs", "packagegroup-base-nfs", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "ppp", "packagegroup-base-ppp", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "smbfs", "packagegroup-base-smbfs", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "raid", "packagegroup-base-raid", "",d)} \
            ${@base_contains("DISTRO_FEATURES", "zeroconf", "packagegroup-base-zeroconf", "", d)} \
            \
            '

ALLOW_EMPTY = "1"

#
# packages which content depend on MACHINE_FEATURES need to be MACHINE_ARCH
#
PACKAGE_ARCH = "${MACHINE_ARCH}"

#
# those ones can be set in machine config to supply packages needed to get machine booting
#
MACHINE_ESSENTIAL_EXTRA_RDEPENDS ?= ""
MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS ?= ""

#
# packagegroup-base contain stuff needed for base system (machine related)
#
RDEPENDS_packagegroup-base = "\
    packagegroup-distro-base \
    packagegroup-machine-base \
    \
    sysfsutils \
    module-init-tools \
    ${@base_contains('MACHINE_FEATURES', 'apm', 'packagegroup-base-apm', '',d)} \
    ${@base_contains('MACHINE_FEATURES', 'acpi', 'packagegroup-base-acpi', '',d)} \
    ${@base_contains('MACHINE_FEATURES', 'keyboard', 'packagegroup-base-keyboard', '',d)} \
    ${@base_contains('MACHINE_FEATURES', 'phone', 'packagegroup-base-phone', '',d)} \
    \
    ${@base_contains('COMBINED_FEATURES', 'alsa', 'packagegroup-base-alsa', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'ext2', 'packagegroup-base-ext2', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'vfat', 'packagegroup-base-vfat', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'irda', 'packagegroup-base-irda', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'pci', 'packagegroup-base-pci', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'pcmcia', 'packagegroup-base-pcmcia', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'usbgadget', 'packagegroup-base-usbgadget', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'usbhost', 'packagegroup-base-usbhost', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'bluetooth', 'packagegroup-base-bluetooth', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'wifi', 'packagegroup-base-wifi', '',d)} \
    ${@base_contains('COMBINED_FEATURES', '3g', 'packagegroup-base-3g', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'uboot', 'packagegroup-base-uboot', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'redboot', 'packagegroup-base-redboot', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'apex', 'packagegroup-base-apex', '',d)} \
    \
    ${@base_contains('DISTRO_FEATURES', 'nfs', 'packagegroup-base-nfs', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'cramfs', 'packagegroup-base-cramfs', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'smbfs', 'packagegroup-base-smbfs', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'ipv6', 'packagegroup-base-ipv6', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'ipsec', 'packagegroup-base-ipsec', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'ppp', 'packagegroup-base-ppp', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'raid', 'packagegroup-base-raid', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'zeroconf', 'packagegroup-base-zeroconf', '',d)} \
    "


RRECOMMENDS_packagegroup-base = "\
    kernel-module-nls-utf8 \
    kernel-module-input \
    kernel-module-uinput \
    kernel-module-rtc-dev \
    kernel-module-rtc-proc \
    kernel-module-rtc-sysfs \
    kernel-module-unix"

RDEPENDS_packagegroup-base-extended = "\
    packagegroup-base \
    ${ADD_WIFI} \
    ${ADD_BT} \
    ${ADD_3G} \
    "

ADD_WIFI = ""
ADD_BT = ""
ADD_3G = ""

python __anonymous () {
    # If Distro want wifi and machine feature wifi/pci/pcmcia/usbhost (one of them)
    # then include packagegroup-base-wifi in packagegroup-base

    distro_features = set(d.getVar("DISTRO_FEATURES", True).split())
    machine_features= set(d.getVar("MACHINE_FEATURES", True).split())

    if "bluetooth" in distro_features and not "bluetooth" in machine_features and ("pcmcia" in machine_features or "pci" in machine_features or "usbhost" in machine_features):
	d.setVar("ADD_BT", "packagegroup-base-bluetooth")

    if "wifi" in distro_features and not "wifi" in machine_features and ("pcmcia" in machine_features or "pci" in machine_features or "usbhost" in machine_features):
	d.setVar("ADD_WIFI", "packagegroup-base-wifi")

    if "3g" in distro_features and not "3g" in machine_features and ("pcmcia" in machine_features or "pci" in machine_features or "usbhost" in machine_features):
	d.setVar("ADD_3G", "packagegroup-base-3g")
}

#
# packages added by distribution
#
DEPENDS_packagegroup-distro-base = "${DISTRO_EXTRA_DEPENDS}"
RDEPENDS_packagegroup-distro-base = "${DISTRO_EXTRA_RDEPENDS}"
RRECOMMENDS_packagegroup-distro-base = "${DISTRO_EXTRA_RRECOMMENDS}"

#
# packages added by machine config
#
RDEPENDS_packagegroup-machine-base = "${MACHINE_EXTRA_RDEPENDS}"
RRECOMMENDS_packagegroup-machine-base = "${MACHINE_EXTRA_RRECOMMENDS}"

RDEPENDS_packagegroup-base-keyboard = "\
    keymaps"

RDEPENDS_packagegroup-base-pci = "\
    pciutils"

RDEPENDS_packagegroup-base-acpi = "\
    acpid \
    libacpi "

RDEPENDS_packagegroup-base-apm = "\
    ${VIRTUAL-RUNTIME_apm} \
    apmd"

RDEPENDS_packagegroup-base-ext2 = "\
    hdparm \
    e2fsprogs \
    e2fsprogs-e2fsck \
    e2fsprogs-mke2fs"

RRECOMMENDS_packagegroup-base-vfat = "\
    kernel-module-msdos \
    kernel-module-vfat \
    kernel-module-nls-iso8859-1 \
    kernel-module-nls-cp437"

RDEPENDS_packagegroup-base-alsa = "\
    alsa-utils-alsactl \
    alsa-utils-alsamixer \
    ${VIRTUAL-RUNTIME_alsa-state}"

RRECOMMENDS_packagegroup-base-alsa = "\
    kernel-module-snd-mixer-oss \
    kernel-module-snd-pcm-oss"

RDEPENDS_packagegroup-base-pcmcia = "\
    pcmciautils \
    "
#${@base_contains('DISTRO_FEATURES', 'wifi', 'prism-firmware', '',d)}
#${@base_contains('DISTRO_FEATURES', 'wifi', 'spectrum-fw', '',d)}


RRECOMMENDS_packagegroup-base-pcmcia = "\
    kernel-module-pcmcia \
    kernel-module-airo-cs \
    kernel-module-pcnet-cs \
    kernel-module-serial-cs \
    kernel-module-ide-cs \
    kernel-module-ide-disk \
    ${@base_contains('DISTRO_FEATURES', 'wifi', 'kernel-module-hostap-cs', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'wifi', 'kernel-module-orinoco-cs', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'wifi', 'kernel-module-spectrum-cs', '',d)}"

# Provide bluez-utils-compat utils for the time being, the binaries in that package will vanish soon from upstream releases, so beware! 

RDEPENDS_packagegroup-base-bluetooth = "\
    bluez4 \
    ${@base_contains('COMBINED_FEATURES', 'alsa', 'libasound-module-bluez', '',d)} \
    "

RRECOMMENDS_packagegroup-base-bluetooth = "\
    kernel-module-bluetooth \
    kernel-module-l2cap \
    kernel-module-rfcomm \
    kernel-module-hci-vhci \
    kernel-module-bnep \
    kernel-module-hidp \
    kernel-module-hci-uart \
    kernel-module-sco \
    ${@base_contains('COMBINED_FEATURES', 'usbhost', 'kernel-module-hci-usb', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'pcmcia', 'kernel-module-bluetooth3c-cs', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'pcmcia', 'kernel-module-bluecard-cs', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'pcmcia', 'kernel-module-bluetoothuart-cs', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'pcmcia', 'kernel-module-dtl1-cs', '',d)} \
    "

RDEPENDS_packagegroup-base-irda = "\
    irda-utils"

RRECOMMENDS_packagegroup-base-irda = "\
    kernel-module-pxaficp-ir \
    kernel-module-irda \
    kernel-module-ircomm \
    kernel-module-ircomm-tty \
    kernel-module-irlan \
    ${@base_contains('DISTRO_FEATURES', 'ppp', 'kernel-module-irnet', '',d)} \
    kernel-module-irport \
    kernel-module-irtty \
    kernel-module-irtty-sir \
    kernel-module-sir-dev \
    ${@base_contains('COMBINED_FEATURES', 'usbhost', 'kernel-module-ir-usb', '',d)} "

RRECOMMENDS_packagegroup-base-usbgadget = "\
    kernel-module-pxa27x_udc \
    kernel-module-gadgetfs \
    kernel-module-g-file-storage \
    kernel-module-g-serial \
    kernel-module-g-ether"

RDEPENDS_packagegroup-base-usbhost = "\
    usbutils "

RRECOMMENDS_packagegroup-base-usbhost = "\
    kernel-module-uhci-hcd \
    kernel-module-ohci-hcd \
    kernel-module-ehci-hcd \
    kernel-module-usbcore \
    kernel-module-usbhid \
    kernel-module-usbnet \
    kernel-module-sd-mod \
    kernel-module-scsi-mod \
    kernel-module-usbmouse \
    kernel-module-mousedev \
    kernel-module-usbserial \
    kernel-module-usb-storage "

RDEPENDS_packagegroup-base-ppp = "\
    ppp \
    ppp-dialin"

RRECOMMENDS_packagegroup-base-ppp = "\
    kernel-module-ppp-async \
    kernel-module-ppp-deflate \
    kernel-module-ppp-generic \
    kernel-module-ppp-mppe \
    kernel-module-slhc"

RDEPENDS_packagegroup-base-ipsec = "\
    openswan"

RRECOMMENDS_packagegroup-base-ipsec = "\
    kernel-module-ipsec"

#
# packagegroup-base-wifi contain everything needed to get WiFi working
# WEP/WPA connection needs to be supported out-of-box
#
RDEPENDS_packagegroup-base-wifi = "\
    wireless-tools \
    ${@base_contains('COMBINED_FEATURES', 'pcmcia', 'hostap-utils', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'pci', 'hostap-utils', '',d)} \
    wpa-supplicant"

RRECOMMENDS_packagegroup-base-wifi = "\
    ${@base_contains('COMBINED_FEATURES', 'usbhost', 'kernel-module-zd1211rw', '',d)} \
    kernel-module-ieee80211-crypt \
    kernel-module-ieee80211-crypt-ccmp \
    kernel-module-ieee80211-crypt-tkip \
    kernel-module-ieee80211-crypt-wep \
    kernel-module-ecb \
    kernel-module-arc4 \
    kernel-module-crypto_algapi \
    kernel-module-cryptomgr \
    kernel-module-michael-mic \
    kernel-module-aes-generic \
    kernel-module-aes"

RDEPENDS_packagegroup-base-3g = "\
    ofono"

RRECOMMENDS_packagegroup-base-3g = "\
    kernel-module-cdc-acm \
    kernel-module-cdc-wdm"

RRECOMMENDS_packagegroup-base-smbfs = "\
    kernel-module-cifs \
    kernel-module-smbfs"

RRECOMMENDS_packagegroup-base-cramfs = "\
    kernel-module-cramfs"

#
# packagegroup-base-nfs provides ONLY client support - server is in nfs-utils package
#
RDEPENDS_packagegroup-base-nfs = "\
    portmap"

RRECOMMENDS_packagegroup-base-nfs = "\
    kernel-module-nfs "

RDEPENDS_packagegroup-base-zeroconf = "\
    avahi-daemon"

RDEPENDS_packagegroup-base-raid = "\
	"

RDEPENDS_packagegroup-base-screen = "\
	"

#
# GPE/OPIE/OpenMoko provide own touchscreen calibration utils
#
RDEPENDS_packagegroup-base-touchscreen = "\
    "

RDEPENDS_packagegroup-base-ipv6 = "\
    "

RRECOMMENDS_packagegroup-base-ipv6 = "\
    kernel-module-ipv6 "

RDEPENDS_packagegroup-base-serial = "\
    setserial \
    lrzsz "

RDEPENDS_packagegroup-base-phone = "\
    ofono"
