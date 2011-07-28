DESCRIPTION = "Kernel drivers for the PowerVR SGX chipset found in the omap3 SoCs"
LICENSE = "GPLv2"

inherit module

SRC_URI = "http://dominion.thruhere.net/koen/OE/omap3-sgx-modules-1.3.13.1397.tar.bz2"

S = ${WORKDIR}/${BPN}-${PV}/eurasiacon/build/linux/omap3430_linux/kbuild/

MAKE_TARGETS = "BUILD=debug"

COMPATIBLE_MACHINE = "(omap-3430ldp|omap-3430sdp|beagleboard|overo)"

do_install() {
	mkdir -p ${D}/lib/modules/${KERNEL_VERSION}/drivers/gpu
	cp ${WORKDIR}/${BPN}-${PV}/eurasiacon/binary_omap3430_linux_debug/*.ko ${D}/lib/modules/${KERNEL_VERSION}/drivers/gpu	
}
