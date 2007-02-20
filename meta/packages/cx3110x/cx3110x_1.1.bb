DESCRIPTION = "cx3110x wifi support as found in the Nokia 770/800"
SECTION = "kernel/modules"
LICENSE = "GPL"
PR = "r0"

export KERNEL_SRC_DIR = ${STAGING_KERNEL_DIR}
export LDFLAGS = ""

SRC_URI = "https://garage.maemo.org/frs/download.php/939/cx3110x-1.1.tar.gz"

S = "${WORKDIR}/cx3110x-${PV}"

inherit module

do_compile() {
	oe_runmake modules 
}

