DESCRIPTION = "cx3110x wifi support as found in the Nokia 770/N800/N810"
SECTION = "kernel/modules"
LICENSE = "GPL"
PACKAGES = "${PN}"

COMPATIBLE_MACHINE = "(nokia800)"

export KERNEL_SRC_DIR = ${STAGING_KERNEL_DIR}
export LDFLAGS = ""

DEFAULT_PREFERENCE_nokia770 = "-1"

SRC_URI = "http://repository.maemo.org/pool/os2008/free/source/c/cx3110x-module-src/cx3110x-module-src_2.0.15-1.tar.gz"

S = "${WORKDIR}/cx3110x-module-src-${PV}"

inherit module

do_compile() {
	oe_runmake modules 
}
