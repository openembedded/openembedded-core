SUMMARY = "Kernel Module Utilities"
DESCRIPTION = "This package contains a set of programs for loading, inserting, and removing kernel modules for Linux (versions 2.5.48 and above). It serves the same function that the modutils package serves for Linux 2.4"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://lsmod.c;md5=743c873ec42632d2ce37d3c440f366dd"
SECTION = "base"

PACKAGES =+ "module-init-tools-insmod-static module-init-tools-depmod"
RDEPENDS_${PN} += "module-init-tools-depmod"

FILES_module-init-tools-depmod = "${sbindir}/depmod.26"
FILES_module-init-tools-insmod-static = "${sbindir}/insmod.static"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/kernel/module-init-tools/module-init-tools-${PV}.tar.bz2 \
           file://modutils_extension.patch \
           file://disable_man.patch \
           file://grab_module_memset.patch"

inherit autotools

# module-init-tools uses AX_ENABLE_BUILDDIR to move rest of configuration steps
# into a subdir. However this macro is not quite cross friendly. Instead of 
# mangling that macro, a easier way is to take the disable option
EXTRA_OECONF = "--disable-builddir"
