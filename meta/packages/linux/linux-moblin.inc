DESCRIPTION = "2.6 Linux Development Kernel for moblin2 platforms"
SECTION = "kernel"
LICENSE = "GPL"

inherit kernel

do_configure() {

	rm -f ${S}/.config || true

   	cp ${WORKDIR}/defconfig-${MACHINE} ${S}/.config

        yes '' | oe_runmake oldconfig

}


COMPATIBLE_MACHINE = "eee901"