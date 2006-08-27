DESCRIPTION = "Merge machine and distro options to create a basic machine task/package"
MAINTAINER = "Richard Purdie <richard@openedhand.com>"
PR = "r0"

PACKAGES = "task-base \
            task-base-oh-minimal"

ALLOW_EMPTY = "1"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Valid only in MACHINE_FEATURES:
#
# kernel24
# kernel26
# apm

# Valid only in DISTO_FEATURES:
#
# nfs
# smbfs
# ipsec
# wifi
# ppp

# Valid COMBINED_FEATURES:
# (These features need to be supported by both the machine and the distro)
#
# alsa
# bluetooth
# ext2
# irda
# pcmcia
# usbgadget
# usbhost


MACHINE_FEATURES ?= "kernel26"
DISTRO_FEATURES ?= ""

DISTRO_EXTRA_RDEPENDS ?= ""
DISTRO_EXTRA_RRECOMMENDS ?= ""
MACHINE_EXTRA_RDEPENDS ?= ""
MACHINE_EXTRA_RRECOMMENDS ?= ""
MACHINE_ESSENTIAL_EXTRA_RDEPENDS ?= ""
MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS ?= ""


COMBINED_FEATURES = "\
    ${@base_both_contain("DISTRO_FEATURES", "MACHINE_FEATURES", "alsa", d)} \
    ${@base_both_contain("DISTRO_FEATURES", "MACHINE_FEATURES", "bluetooth", d)} \
    ${@base_both_contain("DISTRO_FEATURES", "MACHINE_FEATURES", "ext2", d)} \
    ${@base_both_contain("DISTRO_FEATURES", "MACHINE_FEATURES", "irda", d)} \
    ${@base_both_contain("DISTRO_FEATURES", "MACHINE_FEATURES", "pcmcia", d)} \
    ${@base_both_contain("DISTRO_FEATURES", "MACHINE_FEATURES", "usbgadget", d)} \
    ${@base_both_contain("DISTRO_FEATURES", "MACHINE_FEATURES", "usbhost", d)}"


#
# task-base 
#
RDEPENDS_task-base = "\
    kernel \
    ${@base_contains("MACHINE_FEATURES", "kernel26", "${task-base-kernel26-rdepends}", "",d)} \
    ${@base_contains("MACHINE_FEATURES", "apm", "${task-base-apm-rdepends}", "",d)} \
    ${@base_contains("COMBINED_FEATURES", "alsa", "${task-base-alsa-rdepends}", "",d)} \
    ${@base_contains("COMBINED_FEATURES", "ext2", "${task-base-ext2-rdepends}", "",d)} \
    ${@base_contains("COMBINED_FEATURES", "irda", "${task-base-irda-rdepends}", "",d)} \
    ${@base_contains("COMBINED_FEATURES", "pcmcia", "${task-base-pcmcia-rdepends}", "",d)} \
    ${@base_contains("DISTRO_FEATURES", "ipsec", "${task-distro-ipsec-rdepends}", "",d)} \
    ${@base_contains("DISTRO_FEATURES", "ppp", "${task-distro-ppp-rdepends}", "",d)} \
    ${@base_contains("DISTRO_FEATURES", "wifi", "${task-distro-wifi-rdepends}", "",d)} \
    ${MACHINE_ESSENTIAL_EXTRA_RDEPENDS} \
    ${MACHINE_EXTRA_RDEPENDS} \
    ${DISTRO_EXTRA_RDEPENDS}"

RRECOMMENDS_task-base = "\
    ${@base_contains("MACHINE_FEATURES", "kernel26", "${task-base-kernel26-extras-rrecommends}", "",d)} \
    ${@base_contains("COMBINED_FEATURES", "alsa", "${task-base-alsa-rrecommends}", "",d)} \
    ${@base_contains("COMBINED_FEATURES", "pcmcia", "${task-base-pcmcia-rrecommends}", "",d)} \
    ${@base_contains("COMBINED_FEATURES", "bluetooth", "${task-base-bluetooth-rrecommends}", "",d)} \
    ${@base_contains("COMBINED_FEATURES", "irda", "${task-base-irda-rrecommends}", "",d)} \
    ${@base_contains("COMBINED_FEATURES", "usbgadget", "${task-base-usbgadget-rrecommends}", "",d)} \
    ${@base_contains("COMBINED_FEATURES", "usbhost", "${task-base-usbhost-rrecommends}", "",d)} \
    ${@base_contains("DISTRO_FEATURES", "ppp", "${task-distro-ppp-rrecommends}", "",d)} \
    ${@base_contains("DISTRO_FEATURES", "nfs", "${task-distro-nfs-rrecommends}", "",d)} \
    ${@base_contains("DISTRO_FEATURES", "ipsec", "${task-distro-ipsec-rrecommends}", "",d)} \
    ${@base_contains("DISTRO_FEATURES", "cramfs", "${task-distro-cramfs-rrecommends}", "",d)} \
    ${MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS} \
    ${MACHINE_EXTRA_RRECOMMENDS} \
    ${DISTRO_EXTRA_RRECOMMENDS}"


#
# task-base-oh-minimal
# An example of a small cut down machine configuration
#
RDEPENDS_task-base-oh-minimal = "\
    kernel \
    ${@base_contains("MACHINE_FEATURES", "kernel26", "${task-base-kernel26-rdepends}", "",d)} \
    ${@base_contains("MACHINE_FEATURES", "apm", "${task-base-apm-rdepends}", "",d)} \
    ${@base_contains("COMBINED_FEATURES", "pcmcia", "${PCMCIA_MANAGER}", "",d)} \
    ${MACHINE_ESSENTIAL_EXTRA_RDEPENDS}"

RRECOMMENDS_task-base-minimal = "\
    ${MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS}"

task-base-kernel26-rdepends = "\
    udev \
    keymaps \
    sysfsutils \
    module-init-tools"

task-base-kernel26-extras-rrecommends = "\
    kernel-module-input \
    kernel-module-uinput"

task-base-apm-rdepends = "\
    apm \
    apmd \
    ${@base_contains("MACHINE_FEATURES", "kernel24", "network-suspend-scripts", "",d)}"

task-base-ext2-rdepends = "\
    hdparm \
    e2fsprogs \
    e2fsprogs-e2fsck \
    e2fsprogs-mke2fs"

task-base-alsa-rdepends = "\
    alsa-utils-alsactl \
    alsa-utils-alsamixer \
    alsa-conf"

task-base-alsa-rrecommends = "\
    kernel-module-snd-mixer-oss \
    kernel-module-snd-pcm-oss"

task-base-pcmcia-rdepends = "\
    ${PCMCIA_MANAGER} \
    ${@base_contains("DISTRO_FEATURES", "wifi", "prism3-firmware", "",d)} \
    ${@base_contains("DISTRO_FEATURES", "wifi", "prism3-support", "",d)} \
    ${@base_contains("DISTRO_FEATURES", "wifi", "spectrum-fw", "",d)} \
    ${@base_contains("DISTRO_FEATURES", "wifi", "hostap-conf", "",d)} \
    ${@base_contains("DISTRO_FEATURES", "wifi", "orinoco-conf", "",d)}"

task-base-pcmcia-rrecommends = "\
    kernel-module-airo-cs \
    kernel-module-pcnet-cs \
    kernel-module-serial-cs \
    kernel-module-hostap-cs \
    kernel-module-ide-cs \
    ${@base_contains("DISTRO_FEATURES", "wifi", "kernel-module-orinoco-cs", "",d)} \
    ${@base_contains("DISTRO_FEATURES", "wifi", "kernel-module-spectrum-cs", "",d)}"

task-base-bluetooth-rrecommends = "\
    kernel-module-bluetooth \
    kernel-module-l2cap \
    kernel-module-rfcomm \
    kernel-module-hci-vhci \
    kernel-module-bnep \
    kernel-module-hidp \
    kernel-module-hci-uart \
    kernel-module-sco \
    ${@base_contains("COMBINED_FEATURES", "pcmcia", "kernel-module-bluetooth3c-cs", "",d)} \
    ${@base_contains("COMBINED_FEATURES", "pcmcia", "kernel-module-bluecard-cs", "",d)} \
    ${@base_contains("COMBINED_FEATURES", "pcmcia", "kernel-module-bluetoothuart-cs", "",d)} \
    ${@base_contains("COMBINED_FEATURES", "pcmcia", "kernel-module-dtl1-cs", "",d)}"

task-base-irda-rdepends = "\
    irda-utils"

task-base-irda-rrecommends = "\
    kernel-module-pxaficp-ir \
    kernel-module-irda \
    kernel-module-ircomm \
    kernel-module-ircomm-tty \
    kernel-module-irlan \
    kernel-module-irnet \
    kernel-module-irport \
    kernel-module-irtty \
    kernel-module-ir-usb"

task-base-usbgadget-rrecommends = "\
    kernel-module-gadgetfs \
    kernel-module-g-file-storage \
    kernel-module-g-serial \
    kernel-module-g-ether"

task-base-usbhost-rrecommends = "\
    kernel-module-ohci-hcd \
    kernel-module-usbcore \
    kernel-module-usbhid \
    kernel-module-usbnet \
    kernel-module-sd-mod \
    kernel-module-scsi-mod \
    kernel-module-usbmouse \
    kernel-module-mousedev \
    kernel-module-hci-usb \
    kernel-module-usbserial \
    kernel-module-usb-storage \
    kernel-module-ir-usb"

task-distro-ppp-rdepends = "\
    ppp \
    ppp-dialin"

task-distro-ppp-rrecommends = "\
    kernel-module-ppp-async \
    kernel-module-ppp-deflate \
    kernel-module-ppp-mppe"

task-distro-ipsec-rdepends = "\
    openswan"

task-distro-ipsec-rrecommends = "\
    kernel-module-ipsec"

task-distro-wifi-rdepends = "\
    wireless-tools \
    hostap-utils \
    wpa-supplicant-nossl"

task-distro-smbfs-rrecommends = "\
    kernel-module-smbfs"

task-distro-cramfs-rrecommends = "\
    kernel-module-cramfs"

task-distro-nfs-rrecommends = "\
    kernel-module-nfs \
    kernel-module-lockd \
    kernel-module-sunrpc"


# Tosort
# kernel-module-ipv6 
# kernel-module-ipsec
# kernel-module-nvrd
# kernel-module-mip6-mn
# kernel-module-tun
# kernel-module-ide-disk 
# kernel-module-ide-probe-mo
# kernel-module-loop
# kernel-module-vfat 
# kernel-module-ext2
# kernel-module-nfs
# kernel-module-sco 
# kernel-module-af_packet
# kernel-module-ip-gre 
# kernel-module-ip-tables 
# kernel-module-ipip
# kernel-module-des 
# kernel-module-md5
# kernel-module-8250
# Should be DISTRO_EXTRA_RRECOMMENDS: lrzsz 
