SUMMARY = "OpenGL / OpenGL ES Reference Compiler"
DESCRIPTION = "Glslang is the official reference compiler front end for the \
OpenGL ES and OpenGL shading languages. It implements a strict interpretation \
of the specifications for these languages. It is open and free for anyone to use, \
either from a command line or programmatically."
SECTION = "graphics"
HOMEPAGE = "https://www.khronos.org/opengles/sdk/tools/Reference-Compiler"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=918e668376010a04448a312fb37ae69b"

SRCREV = "bcf6a2430e99e8fc24f9f266e99316905e6d5134"
SRC_URI = "git://github.com/KhronosGroup/glslang.git;protocol=https"
UPSTREAM_CHECK_GITTAGREGEX = "^(?P<pver>\d+(\.\d+)+)$"
S = "${WORKDIR}/git"

inherit cmake python3native

BBCLASSEXTEND = "native nativesdk"
