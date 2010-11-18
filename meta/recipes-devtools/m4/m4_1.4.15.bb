SUMMARY = "Traditional Unix macro processor"
DESCRIPTION = "GNU m4 is an implementation of the traditional Unix macro processor.  It is mostly SVR4 \
compatible although it has some extensions (for example, handling more than 9 positional parameters to macros). \
GNU M4 also has built-in functions for including files, running shell commands, doing arithmetic, etc."
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504\
	file://examples/COPYING;md5=380fd7d57d3fd009a5716e074a845d6d"

PR = "r0"
SRC_URI = "${GNU_MIRROR}/m4/m4-${PV}.tar.gz \
           file://ac_config_links.patch;patch=1"

SRC_URI[md5sum] = "5649a2e593b6c639deae9e72ede777dd"
SRC_URI[sha256sum] = "3d66dfeb609007062265a67b9a2c08d3686a74068830bacae60a6d58413c9367"

inherit autotools

EXTRA_OEMAKE += "'infodir=${infodir}'"
