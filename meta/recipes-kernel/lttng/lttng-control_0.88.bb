SECTION = "devel"
SUMMARY = "Linux Trace Toolkit Control"
DESCRIPTION = "The Linux trace toolkit is a suite of tools designed to \
extract program execution details from the Linux operating system and  \
interpret them."
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
PR = "r0"

LTTDATE = "09242010"

SRC_URI = "http://ltt.polymtl.ca/files/lttng/ltt-control-${PV}-${LTTDATE}.tar.gz"

SRC_URI[md5sum] = "ca81d874352756837518d74c9d1091a7"
SRC_URI[sha256sum] = "89c625a4d1b0823954217df232a31f3cae31de7b6655c0d885a61bece0dfcd88"

S = "${WORKDIR}/ltt-control-${PV}-${LTTDATE}"

inherit autotools

export KERNELDIR="${STAGING_KERNEL_DIR}"

FILES_${PN} += "${datadir}/ltt-control/facilities/*"	    
