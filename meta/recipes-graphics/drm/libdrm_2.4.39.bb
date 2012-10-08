require libdrm.inc

PR = "${INC_PR}.0"

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
            file://disable-cairo.patch \
           "

SRC_URI[md5sum] = "9a299e021d81bab6c82307582c78319d"
SRC_URI[sha256sum] = "386b17388980504bca16ede81ceed4c77b12c3488f46ecb7f4d48e48512a733d"
