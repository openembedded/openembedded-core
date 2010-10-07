DESCRIPTION = "2.6 Linux Development Kernel for Atom based platforms"
SECTION = "kernel"
LICENSE = "GPL"

inherit kernel

do_configure() {

	rm -f ${S}/.config || true

   	cp ${WORKDIR}/defconfig-${MACHINE} ${S}/.config

        yes '' | oe_runmake oldconfig

}

COMPATIBLE_MACHINE = "(atom-pc|menlow)"
