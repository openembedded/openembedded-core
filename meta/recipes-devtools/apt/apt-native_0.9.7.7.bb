require apt-native.inc

PR = "r0"

SRC_URI += "file://noconfigure.patch \
	    file://no-curl.patch"

SRC_URI[md5sum] = "3f86f4eff53c94b7f285b3c59eb89754"
SRC_URI[sha256sum] = "71fc7ff15fda50f16c9d73f701adf3bd67c9803a2304cd3d82cb490d76d3c3b3"

LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=0636e73ff0215e8d672dc4c32c317bb3"
