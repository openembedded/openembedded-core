SUMMARY = "OpenEmbedded Image Creator (wic) standalone CLI"
HOMEPAGE = "https://git.yoctoproject.org/wic"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4ee23c52855c222cba72583d301d2338"

SRC_URI = "git://git.yoctoproject.org/wic.git;branch=master;protocol=https"
SRCREV = "79e2b06a1f9c97decdaf44f0806dbf3cc1e86bf9"

inherit python_hatchling

RDEPENDS:${PN} += " \
    python3-core \
    python3-json \
    python3-logging \
    python3-misc \
    "

BBCLASSEXTEND = "native nativesdk"
