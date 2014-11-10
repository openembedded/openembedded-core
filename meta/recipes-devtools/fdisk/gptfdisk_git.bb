DESCRIPTION = "GPT fdisk is a disk partitioning tool loosely modeled on Linux fdisk, but used for modifying GUID Partition Table (GPT) disks. The related FixParts utility fixes some common problems on Master Boot Record (MBR) disks."

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

DEPENDS = "util-linux popt ncurses"

PV = "0.8.10+git${SRCPV}"
SRCREV = "a920398fa393f9d6301b32b191bc01e086ab8bc8"
SRC_URI = "git://git.code.sf.net/p/gptfdisk/code"

S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 cgdisk ${D}${sbindir}
    install -m 0755 gdisk ${D}${sbindir}
    install -m 0755 sgdisk ${D}${sbindir}
    install -m 0755 fixparts ${D}${sbindir}
}

BBCLASSEXTEND = "native nativesdk"
