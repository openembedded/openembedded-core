SECTION = "devel"
SUMMARY = "Linux Trace Toolkit Control"
DESCRIPTION = "The Linux trace toolkit is a suite of tools designed to \
extract program execution details from the Linux operating system and  \
interpret them."
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
PR = "r0"

LTTDATE = "05122011"

SRC_URI = "http://lttng.org/files/lttng/ltt-control-${PV}-${LTTDATE}.tar.gz"

SRC_URI[md5sum] = "2e5a531bf5cab88eef5282b45271789f"
SRC_URI[sha256sum] = "feb120e0bedd9ef8917dd14ee96cc94941f517826a4c2035d1c4aa33e4e808ab"

S = "${WORKDIR}/ltt-control-${PV}-${LTTDATE}"

inherit autotools

export KERNELDIR="${STAGING_KERNEL_DIR}"

FILES_${PN} += "${datadir}/ltt-control/facilities/*"	    
