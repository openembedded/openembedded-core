require apt-native.inc

SRC_URI += "file://noconfigure.patch \
            file://no-curl.patch"

SRC_URI[md5sum] = "e70c6d6227883cfc0dda6bc5db509bca"
SRC_URI[sha256sum] = "96bebcd7bfee0b2386741a8315182ba39487bdd743ecf5c1fc5b8b889cca2478"

LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=0636e73ff0215e8d672dc4c32c317bb3"
