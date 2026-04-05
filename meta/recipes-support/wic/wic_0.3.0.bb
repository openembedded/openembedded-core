SUMMARY = "OpenEmbedded Image Creator (wic) standalone CLI"
HOMEPAGE = "https://git.yoctoproject.org/wic"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4ee23c52855c222cba72583d301d2338"

SRC_URI = "git://git.yoctoproject.org/wic.git;branch=contrib/rpurdie;protocol=https"
SRCREV = "cb9c009c9f062e50ba688e34b3b724cbac171ed9"

inherit python_hatchling

RDEPENDS:${PN} += " \
    python3-core \
    python3-json \
    python3-logging \
    python3-misc \
    "

BBCLASSEXTEND = "native nativesdk"
