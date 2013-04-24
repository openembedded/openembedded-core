require apt-native.inc

SRC_URI += "file://noconfigure.patch \
	    file://no-curl.patch"

SRC_URI[md5sum] = "d44f459d59d8fa7fc5f455f1f982f08c"
SRC_URI[sha256sum] = "9570905992f4a83b0c182f11f9e0a8c20a1209a52996d1a01ddbfa359ae2c819"

LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=0636e73ff0215e8d672dc4c32c317bb3"
