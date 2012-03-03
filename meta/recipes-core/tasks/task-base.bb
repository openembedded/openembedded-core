DESCRIPTION = "Merge machine and distro options to create a basic machine task/package"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r73"

inherit task

PROVIDES = "${PACKAGES}"
PACKAGES = ' \
            task-base \
            task-base-extended \
            task-distro-base \
            task-machine-base \
            \
            ${@base_contains("MACHINE_FEATURES", "acpi", "task-base-acpi", "",d)} \
            ${@base_contains("MACHINE_FEATURES", "alsa", "task-base-alsa", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "apm", "task-base-apm", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "ext2", "task-base-ext2", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "vfat", "task-base-vfat", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "irda", "task-base-irda", "",d)} \
            ${@base_contains("MACHINE_FEATURES", "keyboard", "task-base-keyboard", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "pci", "task-base-pci", "",d)} \
            ${@base_contains("MACHINE_FEATURES", "pcmcia", "task-base-pcmcia", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "phone", "task-base-phone", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "screen", "task-base-screen", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "serial", "task-base-serial", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "touchscreen", "task-base-touchscreen", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "usbgadget", "task-base-usbgadget", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "usbhost", "task-base-usbhost", "", d)} \
            \
            ${@base_contains("MACHINE_FEATURES", "uboot", "task-base-uboot", "",d)} \
            ${@base_contains("MACHINE_FEATURES", "redboot", "task-base-redboot", "",d)} \
            ${@base_contains("MACHINE_FEATURES", "apex", "task-base-apex", "",d)} \
	    \
            ${@base_contains("DISTRO_FEATURES", "bluetooth", "task-base-bluetooth", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "wifi", "task-base-wifi", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "3g", "task-base-3g", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "cramfs", "task-base-cramfs", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "ipsec", "task-base-ipsec", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "ipv6", "task-base-ipv6", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "nfs", "task-base-nfs", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "ppp", "task-base-ppp", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "smbfs", "task-base-smbfs", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "raid", "task-base-raid", "",d)} \
            ${@base_contains("DISTRO_FEATURES", "zeroconf", "task-base-zeroconf", "", d)} \
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
# task-base contain stuff needed for base system (machine related)
#
RDEPENDS_task-base = "\
    task-distro-base \
    task-machine-base \
    \
    sysfsutils \
    module-init-tools \
    ${@base_contains('MACHINE_FEATURES', 'apm', 'task-base-apm', '',d)} \
    ${@base_contains('MACHINE_FEATURES', 'acpi', 'task-base-acpi', '',d)} \
    ${@base_contains('MACHINE_FEATURES', 'keyboard', 'task-base-keyboard', '',d)} \
    ${@base_contains('MACHINE_FEATURES', 'phone', 'task-base-phone', '',d)} \
    \
    ${@base_contains('COMBINED_FEATURES', 'alsa', 'task-base-alsa', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'ext2', 'task-base-ext2', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'vfat', 'task-base-vfat', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'irda', 'task-base-irda', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'pci', 'task-base-pci', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'pcmcia', 'task-base-pcmcia', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'usbgadget', 'task-base-usbgadget', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'usbhost', 'task-base-usbhost', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'bluetooth', 'task-base-bluetooth', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'wifi', 'task-base-wifi', '',d)} \
    ${@base_contains('COMBINED_FEATURES', '3g', 'task-base-3g', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'uboot', 'task-base-uboot', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'redboot', 'task-base-redboot', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'apex', 'task-base-apex', '',d)} \
    \
    ${@base_contains('DISTRO_FEATURES', 'nfs', 'task-base-nfs', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'cramfs', 'task-base-cramfs', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'smbfs', 'task-base-smbfs', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'ipv6', 'task-base-ipv6', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'ipsec', 'task-base-ipsec', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'ppp', 'task-base-ppp', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'raid', 'task-base-raid', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'zeroconf', 'task-base-zeroconf', '',d)} \
    "


RRECOMMENDS_task-base = "\
    kernel-module-nls-utf8 \
    kernel-module-input \
    kernel-module-uinput \
    kernel-module-rtc-dev \
    kernel-module-rtc-proc \
    kernel-module-rtc-sysfs \
    kernel-module-unix"

RDEPENDS_task-base-extended = "\
    task-base \
    ${ADD_WIFI} \
    ${ADD_BT} \
    ${ADD_3G} \
    "

ADD_WIFI = ""
ADD_BT = ""
ADD_3G = ""

python __anonymous () {
    # If Distro want wifi and machine feature wifi/pci/pcmcia/usbhost (one of them)
    # then include task-base-wifi in task-base

    import bb

    distro_features = set(d.getVar("DISTRO_FEATURES", True).split())
    machine_features= set(d.getVar("MACHINE_FEATURES", True).split())

    if "bluetooth" in distro_features and not "bluetooth" in machine_features and ("pcmcia" in machine_features or "pci" in machine_features or "usbhost" in machine_features):
	d.setVar("ADD_BT", "task-base-bluetooth")

    if "wifi" in distro_features and not "wifi" in machine_features and ("pcmcia" in machine_features or "pci" in machine_features or "usbhost" in machine_features):
	d.setVar("ADD_WIFI", "task-base-wifi")

    if "3g" in distro_features and not "3g" in machine_features and ("pcmcia" in machine_features or "pci" in machine_features or "usbhost" in machine_features):
	d.setVar("ADD_3G", "task-base-3g")
}

#
# packages added by distribution
#
DEPENDS_task-distro-base = "${DISTRO_EXTRA_DEPENDS}"
RDEPENDS_task-distro-base = "${DISTRO_EXTRA_RDEPENDS}"
RRECOMMENDS_task-distro-base = "${DISTRO_EXTRA_RRECOMMENDS}"

#
# packages added by machine config
#
RDEPENDS_task-machine-base = "${MACHINE_EXTRA_RDEPENDS}"
RRECOMMENDS_task-machine-base = "${MACHINE_EXTRA_RRECOMMENDS}"

RDEPENDS_task-base-keyboard = "\
    keymaps"

RDEPENDS_task-base-pci = "\
    pciutils"

RDEPENDS_task-base-acpi = "\
    acpid \
    libacpi "

RDEPENDS_task-base-apm = "\
    ${VIRTUAL-RUNTIME_apm} \
    apmd"

RDEPENDS_task-base-ext2 = "\
    hdparm \
    e2fsprogs \
    e2fsprogs-e2fsck \
    e2fsprogs-mke2fs"

RRECOMMENDS_task-base-vfat = "\
    kernel-module-msdos \
    kernel-module-vfat \
    kernel-module-nls-iso8859-1 \
    kernel-module-nls-cp437"

RDEPENDS_task-base-alsa = "\
    alsa-utils-alsactl \
    alsa-utils-alsamixer \
    ${VIRTUAL-RUNTIME_alsa-state}"

RRECOMMENDS_task-base-alsa = "\
    kernel-module-snd-mixer-oss \
    kernel-module-snd-pcm-oss"

RDEPENDS_task-base-pcmcia = "\
    pcmciautils \
    "
#${@base_contains('DISTRO_FEATURES', 'wifi', 'prism-firmware', '',d)}
#${@base_contains('DISTRO_FEATURES', 'wifi', 'spectrum-fw', '',d)}


RRECOMMENDS_task-base-pcmcia = "\
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

RDEPENDS_task-base-bluetooth = "\ 
    bluez4 \
    "

RRECOMMENDS_task-base-bluetooth = "\
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

RDEPENDS_task-base-irda = "\
    irda-utils"

RRECOMMENDS_task-base-irda = "\
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

RRECOMMENDS_task-base-usbgadget = "\
    kernel-module-pxa27x_udc \
    kernel-module-gadgetfs \
    kernel-module-g-file-storage \
    kernel-module-g-serial \
    kernel-module-g-ether"

RDEPENDS_task-base-usbhost = "\
    usbutils "

RRECOMMENDS_task-base-usbhost = "\
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

RDEPENDS_task-base-ppp = "\
    ppp \
    ppp-dialin"

RRECOMMENDS_task-base-ppp = "\
    kernel-module-ppp-async \
    kernel-module-ppp-deflate \
    kernel-module-ppp-generic \
    kernel-module-ppp-mppe \
    kernel-module-slhc"

RDEPENDS_task-base-ipsec = "\
    openswan"

RRECOMMENDS_task-base-ipsec = "\
    kernel-module-ipsec"

#
# task-base-wifi contain everything needed to get WiFi working
# WEP/WPA connection needs to be supported out-of-box
#
RDEPENDS_task-base-wifi = "\
    wireless-tools \
    ${@base_contains('COMBINED_FEATURES', 'pcmcia', 'hostap-utils', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'pci', 'hostap-utils', '',d)} \
    wpa-supplicant"

RRECOMMENDS_task-base-wifi = "\
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

RDEPENDS_task-base-3g = "\
    ofono"

RRECOMMENDS_task-base-3g = "\
    kernel-module-cdc-acm \
    kernel-module-cdc-wdm"

RRECOMMENDS_task-base-smbfs = "\
    kernel-module-cifs \
    kernel-module-smbfs"

RRECOMMENDS_task-base-cramfs = "\
    kernel-module-cramfs"

#
# task-base-nfs provides ONLY client support - server is in nfs-utils package
#
RDEPENDS_task-base-nfs = "\
    portmap"

RRECOMMENDS_task-base-nfs = "\
    kernel-module-nfs "

RDEPENDS_task-base-zeroconf = "\
    avahi-daemon"

RDEPENDS_task-base-raid = "\
	"

RDEPENDS_task-base-screen = "\
	"

#
# GPE/OPIE/OpenMoko provide own touchscreen calibration utils
#
RDEPENDS_task-base-touchscreen = "\
    "

RDEPENDS_task-base-ipv6 = "\
    "

RRECOMMENDS_task-base-ipv6 = "\
    kernel-module-ipv6 "

RDEPENDS_task-base-serial = "\
    setserial \
    lrzsz "

RDEPENDS_task-base-phone = "\
    gsmd \
    libgsmd-tools"
