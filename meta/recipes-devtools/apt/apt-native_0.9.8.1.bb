require apt-native.inc

SRC_URI += "file://noconfigure.patch \
            file://no-curl.patch"

SRC_URI[md5sum] = "85781bb39901d6fb79c37ca307929594"
SRC_URI[sha256sum] = "dcef6fc33948d5e430d337ad6326bf7ac3d06b14d99ede176809461ac12b4c6f"

LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=0636e73ff0215e8d672dc4c32c317bb3"
