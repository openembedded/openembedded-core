SECTION = "devel"
DESCRIPTION = "The Linux trace toolkit is a suite of tools designed to \
extract program execution details from the Linux operating system and  \
interpret them."
LICENSE = "GPL"
MAINTAINER = "Richard Purdie <rpurdie@rpsys.net>"

SRC_URI = "http://ltt.polymtl.ca/lttng/${PN}-${PV}.tar.gz \
           file://ltt-modules-fixes.patch;patch=1"

S = "${WORKDIR}/ltt-modules-${PV}"

inherit module

export KERNELDIR="${STAGING_KERNEL_DIR}"

do_install() {   
        install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/net/
        install -m 0644 *${KERNEL_OBJECT_SUFFIX} ${D}${base_libdir}/modules/${KERNEL_VERSION}/net/
}

FILES = "/lib/modules/${KERNEL_VERSION}/net/*_cs${KERNEL_OBJECT_SUFFIX}"        
