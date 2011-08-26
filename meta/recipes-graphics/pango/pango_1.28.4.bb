require pango.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

PR = "r1"

SRC_URI += "file://no-tests.patch"

SRC_URI[archive.md5sum] = "3f3989700f04e9117d30544a9078b3a0"
SRC_URI[archive.sha256sum] = "7eb035bcc10dd01569a214d5e2bc3437de95d9ac1cfa9f50035a687c45f05a9f"

#PARALLEL_MAKE = ""
