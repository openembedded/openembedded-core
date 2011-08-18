SUMMARY = "Traditional Unix macro processor"
DESCRIPTION = "GNU m4 is an implementation of the traditional Unix macro processor.  It is mostly SVR4 \
compatible although it has some extensions (for example, handling more than 9 positional parameters to macros). \
GNU M4 also has built-in functions for including files, running shell commands, doing arithmetic, etc."
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504\
	file://examples/COPYING;md5=fbc986d45b3dae6725c29870dd6b669d"

PR = "r0"
SRC_URI = "${GNU_MIRROR}/m4/m4-${PV}.tar.gz \
           file://ac_config_links.patch"

SRC_URI[md5sum] = "a5dfb4f2b7370e9d34293d23fd09b280"
SRC_URI[sha256sum] = "e9176a35bb13a1b08482359aa554ee8072794f58f00e4827bf0e06b570c827da"

inherit autotools

EXTRA_OEMAKE += "'infodir=${infodir}'"
