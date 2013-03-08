require apt-native.inc

PR = "r1"

SRC_URI += "file://noconfigure.patch \
	    file://no-curl.patch"

SRC_URI[md5sum] = "039fc57668d1a0c2f62dc22e71900a16"
SRC_URI[sha256sum] = "6eca4285f1ac2e8cb837b9546553b0546881ed15853aa5bbeb860bab6bfa1700"

LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=0636e73ff0215e8d672dc4c32c317bb3"
