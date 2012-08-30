DESCRIPTION = "Tasks for core X11 applications"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r0"

inherit packagegroup

RDEPENDS_${PN} = "\
    packagegroup-core-x11-xserver \
    packagegroup-core-x11-utils \
    dbus \
    pointercal \
    matchbox-terminal \
    matchbox-wm \
    mini-x-session \
    liberation-fonts \
    "
