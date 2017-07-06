DESCRIPTION = "Open Asset Import Library"
LICENSE = "BSD-3-Clause"
DEPENDS = "zlib"

LIC_FILES_CHKSUM = "file://LICENSE;md5=2119edef0916b0bd511cb3c731076271 \
                    file://code/Assimp.cpp;endline=41;md5=717f847b6e8f43c64cdbafcfea109923"

SRC_URI = "git://github.com/assimp/assimp.git"
SRCREV = "b38ba233f530fdb103d3ede3df5126121af78b10"
S = "${WORKDIR}/git"

inherit cmake
EXTRA_OECMAKE = "-DASSIMP_BUILD_ASSIMP_TOOLS=OFF -DASSIMP_BUILD_TESTS=OFF"
FILES_${PN}-dev += "${libdir}/cmake/assimp-3.3"
