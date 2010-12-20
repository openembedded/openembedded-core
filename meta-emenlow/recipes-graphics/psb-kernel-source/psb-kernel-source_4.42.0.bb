DESCRIPTION = "Kernel module for the Poulsbo (psb) 2D X11 driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://GPLv2_License.txt;md5=f5ca3155cfeaa64a6ea228b11ad6916d"
PR = "r1"

inherit module

SRC_URI = "https://launchpad.net/~gma500/+archive/ppa/+files/psb-kernel-source_4.42.0-0ubuntu2~1004um2.tar.gz \
	file://build.patch;patch=1"

do_compile () {
	oe_runmake LINUXDIR=${STAGING_KERNEL_DIR} DRM_MODULES="psb"
}

do_install () {
        mkdir -p ${D}/lib/modules/${KERNEL_VERSION}/extra
        cp ${WORKDIR}/${PN}-${PV}/*.ko ${D}/lib/modules/${KERNEL_VERSION}/extra
}

FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/extra/*.ko"

DEPENDS += "virtual/kernel"

COMPATIBLE_MACHINE = "emenlow"
