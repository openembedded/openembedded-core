SECTION = "devel"
DESCRIPTION = "The Linux trace toolkit is a suite of tools designed to \
extract program execution details from the Linux operating system and  \
interpret them."
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=id32239bcb673463ab874e80d47fae504"
PR = "r0"

LTTDATE = "09062010"

SRC_URI = "http://ltt.polymtl.ca/files/lttng/ltt-control-${PV}-${LTTDATE}.tar.gz"

S = "${WORKDIR}/ltt-control-${PV}-${LTTDATE}"

inherit autotools

export KERNELDIR="${STAGING_KERNEL_DIR}"

FILES_${PN} += "${datadir}/ltt-control/facilities/*"	    
