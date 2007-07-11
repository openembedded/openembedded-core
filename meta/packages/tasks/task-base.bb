DESCRIPTION = "Merge machine and distro options to create a basic machine task/package"
PR = "r41"

PROVIDES = "${PACKAGES}"
PACKAGES = ' \
            task-base task-base-dev task-base-dbg\
            task-base-extended task-base-extended-dev task-base-extended-dbg \
            task-distro-base task-distro-base-dev task-distro-base-dbg \
            task-machine-base task-machine-base-dev task-machine-base-dbg \
            \
            ${@base_contains("MACHINE_FEATURES", "acpi", "task-base-acpi task-base-acpi-dev task-base-acpi-dbg", "",d)} \
            ${@base_contains("MACHINE_FEATURES", "irda", "task-base-irda task-base-irda-dev task-base-irda-dbg", "",d)} \
            ${@base_contains("MACHINE_FEATURES", "pci", "task-base-pci task-base-pci-dev task-base-pci-dbg", "",d)} \
            ${@base_contains("MACHINE_FEATURES", "alsa", "task-base-alsa task-base-alsa-dev task-base-alsa-dbg", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "apm", "task-base-apm task-base-apm-dev task-base-apm-dbg", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "ext2", "task-base-ext2 task-base-ext2-dev task-base-ext2-dbg", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "keyboard", "task-base-keyboard task-base-keyboard-dev task-base-keyboard-dbg", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "pcmcia", "task-base-pcmcia task-base-pcmcia-dev task-base-pcmcia-dbg", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "phone", "task-base-phone task-base-phone-dev task-base-phone-dbg", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "screen", "task-base-screen task-base-screen-dev task-base-screen-dbg", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "serial", "task-base-serial task-base-serial-dev task-base-serial-dbg", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "touchscreen", "task-base-touchscreen task-base-touchscreen-dev task-base-touchscreen-dbg", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "usbgadget", "task-base-usbgadget task-base-usbgadget-dev task-base-usbgadget-dbg", "", d)} \
            ${@base_contains("MACHINE_FEATURES", "usbhost", "task-base-usbhost task-base-usbhost-dev task-base-usbhost-dbg", "", d)} \
            task-base-bluetooth task-base-bluetooth-dev task-base-bluetooth-dbg \
            task-base-wifi task-base-wifi-dev task-base-wifi-dbg \
            \
            ${@base_contains("DISTRO_FEATURES", "cramfs", "task-base-cramfs task-base-cramfs-dev task-base-cramfs-dbg", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "ipsec", "task-base-ipsec task-base-ipsec-dev task-base-ipsec-dbg", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "ipv6", "task-base-ipv6 task-base-ipv6-dev task-base-ipv6-dbg", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "nfs", "task-base-nfs task-base-nfs-dev task-base-nfs-dbg", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "ppp", "task-base-ppp task-base-ppp-dev task-base-ppp-dbg", "", d)} \
            ${@base_contains("DISTRO_FEATURES", "smbfs", "task-base-smbfs task-base-smbfs-dev task-base-smbfs-dbg", "", d)} \
	\
            task-base-kernel26 task-base-kernel26-dev task-base-kernel26-dbg \
            '

ALLOW_EMPTY = "1"

PACKAGE_ARCH = "${MACHINE_ARCH}"

#
# pcmciautils for >= 2.6.13-rc1, pcmcia-cs for others
#
PCMCIA_MANAGER ?= "${@base_contains('MACHINE_FEATURES', 'kernel26','pcmciautils','pcmcia-cs',d)} "

#
# task-base contain stuff needed for base system (machine related)
#
RDEPENDS_task-base = "\
    task-distro-base \
    task-machine-base \
    \
    task-base-kernel26 \
    ${@base_contains('MACHINE_FEATURES', 'apm', 'task-base-apm', '',d)} \
    ${@base_contains('MACHINE_FEATURES', 'acpi', 'task-base-acpi', '',d)} \
    ${@base_contains('MACHINE_FEATURES', 'keyboard', 'task-base-keyboard', '',d)} \
    \
    ${@base_contains('COMBINED_FEATURES', 'alsa', 'task-base-alsa', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'ext2', 'task-base-ext2', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'irda', 'task-base-irda', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'pci', 'task-base-pci', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'pcmcia', 'task-base-pcmcia', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'phone', 'task-base-phone', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'usbgadget', 'task-base-usbgadget', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'usbhost', 'task-base-usbhost', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'bluetooth', 'task-base-bluetooth', '',d)} \
    ${@base_contains('COMBINED_FEATURES', 'wifi', 'task-base-wifi', '',d)} \
    \
    ${@base_contains('DISTRO_FEATURES', 'nfs', 'task-base-nfs', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'ipv6', 'task-base-ipv6', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'ipsec', 'task-base-ipsec', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'ppp', 'task-base-ppp', '',d)} \
    "

RDEPENDS_task-base-extended = "\
    task-base \
    ${ADD_WIFI} \
    ${ADD_BT} \
    "

ADD_WIFI = ""
ADD_BT = ""

python __anonymous () {
    # If Distro want wifi and machine feature wifi/pci/pcmcia/usbhost (one of them)
    # then include task-base-wifi in task-base

    import bb

    if not hasattr(__builtins__, 'set'):
	from sets import Set as set

    distro_features = set(bb.data.getVar("DISTRO_FEATURES", d, 1).split())
    machine_features= set(bb.data.getVar("MACHINE_FEATURES", d, 1).split())

    if "bluetooth" in distro_features and not "bluetooth" in machine_features and ("pcmcia" in machine_features or "pci" in machine_features or "usbhost" in machine_features):
	bb.data.setVar("ADD_BT", "task-base-bluetooth", d)

    if "wifi" in distro_features and not "wifi" in machine_features and ("pcmcia" in machine_features or "pci" in machine_features or "usbhost" in machine_features):
	bb.data.setVar("ADD_WIFI", "task-base-wifi", d)
}

#
# packages added by distribution
#
RDEPENDS_task-distro-base = "${DISTRO_EXTRA_RDEPENDS}"
RRECOMMENDS_task-distro-base = "${DISTRO_EXTRA_RRECOMMENDS}"

#
# packages added by machine config
#
RDEPENDS_task-machine-base = "${MACHINE_EXTRA_RDEPENDS}"
RRECOMMENDS_task-machine-base = "${MACHINE_EXTRA_RRECOMMENDS}"

RDEPENDS_task-base-kernel26 = "\
    sysfsutils \
    module-init-tools"

RRECOMMENDS_task-base-kernel26 = "\
    kernel-module-input \
    kernel-module-uinput \
    kernel-module-rtc-dev \
    kernel-module-rtc-proc \
    kernel-module-rtc-sysfs \
    kernel-module-rtc-sa1100"

RDEPENDS_task-base-keyboard = "\
    keymaps"

RDEPENDS_task-base-pci = "\
    pciutils"

RDEPENDS_task-base-acpi = "\
    acpid"

RDEPENDS_task-base-apm = "\
    apm \
    apmd"

RDEPENDS_task-base-ext2 = "\
    hdparm \
    e2fsprogs \
    e2fsprogs-e2fsck \
    e2fsprogs-mke2fs"

RDEPENDS_task-base-alsa = "\
    alsa-utils-alsactl \
    alsa-utils-alsamixer"

#
# alsa-state is machine related so can be missing in feed, OSS support is optional
#
RRECOMMENDS_task-base-alsa = "\
    kernel-module-snd-mixer-oss \
    kernel-module-snd-pcm-oss"

RDEPENDS_task-base-pcmcia = "\
    ${PCMCIA_MANAGER} \
    ${@base_contains('DISTRO_FEATURES', 'wifi', 'prism-firmware', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'wifi', 'spectrum-fw', '',d)} \
    "

RRECOMMENDS_task-base-pcmcia = "\
    ${@base_contains('MACHINE_FEATURES', 'kernel26', '${task-base-pcmcia26}', '${task-base-pcmcia24}',d)} \
    kernel-module-pcmcia \
    kernel-module-airo-cs \
    kernel-module-pcnet-cs \
    kernel-module-serial-cs \
    kernel-module-ide-cs \
    kernel-module-ide-disk \
    "

task-base-pcmcia24 = "\
    ${@base_contains('DISTRO_FEATURES', 'wifi', 'hostap-modules-cs', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'wifi', 'orinoco-modules-cs', '',d)} \
    "

task-base-pcmcia26 = "\
    ${@base_contains('DISTRO_FEATURES', 'wifi', 'kernel-module-hostap-cs', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'wifi', 'kernel-module-orinoco-cs', '',d)} \
    ${@base_contains('DISTRO_FEATURES', 'wifi', 'kernel-module-spectrum-cs', '',d)}"

RDEPENDS_task-base-bluetooth = "\ 
    bluez-utils"

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
    ${@base_contains('COMBINED_FEATURES', 'pcmcia', 'kernel-module-dtl1-cs', '',d)}"

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
    kernel-module-michael-mic \
    kernel-module-aes"

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

RDEPENDS_task-base-screen = "\
	"

#
# GPE/OPIE/OpenMoko provide own touchscreen calibration utils
#
RDEPENDS_task-base-touchscreen = "\
    tslib-tests \
    tslib-calibrate "

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
