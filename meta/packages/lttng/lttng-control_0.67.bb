SECTION = "devel"
DESCRIPTION = "The Linux trace toolkit is a suite of tools designed to \
extract program execution details from the Linux operating system and  \
interpret them."
LICENSE = "GPL"

LTTDATE = "05032009"

SRC_URI = "http://ltt.polymtl.ca/files/lttng/ltt-control-${PV}-${LTTDATE}.tar.gz"
#           file://build_fix.patch;patch=1 \
#           file://dynticks.patch;patch=1

S = "${WORKDIR}/ltt-control-${PV}-${LTTDATE}"

inherit autotools

export KERNELDIR="${STAGING_KERNEL_DIR}"

FILES_${PN} += "${datadir}/ltt-control/facilities/*"	    