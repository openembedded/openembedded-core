SECTION = "devel"
DESCRIPTION = "The Linux trace toolkit is a suite of tools designed to \
extract program execution details from the Linux operating system and  \
interpret them."
LICENSE = "GPL"

SRC_URI = "http://ltt.polymtl.ca/lttng/ltt-control-${PV}-16072007.tar.gz \
           file://dynticks.patch;patch=1"

S = "${WORKDIR}/ltt-control-${PV}-16072007"

inherit autotools

export KERNELDIR="${STAGING_KERNEL_DIR}"

FILES_${PN} += "${datadir}/ltt-control/facilities/*"	    