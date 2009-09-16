DESCRIPTION = "This package contains a set of programs for loading, inserting, and \
removing kernel modules for Linux (versions 2.5.48 and above). It serves \
the same function that the modutils package serves for Linux 2.4."
LICENSE = "GPL"
SECTION = "base"

PACKAGES =+ "module-init-tools-insmod-static module-init-tools-depmod"
RDEPENDS_${PN} += "module-init-tools-depmod"

FILES_module-init-tools-depmod = "${sbindir}/depmod.26"
FILES_module-init-tools-insmod-static = "${sbindir}/insmod.static"

SRC_URI = "${KERNELORG_MIRROR}/pub/linux/utils/kernel/module-init-tools/module-init-tools-${PV}.tar.bz2 \
	   file://ignore_arch_directory;patch=1 \
	   file://modutils_extension;patch=1 \
	   file://no_man_rebuild;patch=1 \
	   file://manpagesopt;patch=1 \
           file://module-init-tools-remove-index.patch;patch=1 "
S = "${WORKDIR}/module-init-tools-${PV}"

EXTRA_OECONF = "--disable-manpages"

inherit autotools
