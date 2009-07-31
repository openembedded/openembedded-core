PR = "r0"

SRC_URI_append += "file://sh4-asmfix.patch;patch=1 \
                   file://use-includedir.patch;patch=1 \
		  "
require gmp.inc
LICENSE = "GPLv3 LGPLv3"
