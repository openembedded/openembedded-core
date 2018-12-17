SUMMARY = "Extended cryptographic library (from glibc)"
DESCRIPTION = "Forked code from glibc libary to extract only crypto part."
HOMEPAGE = "https://github.com/besser82/libxcrypt"
SECTION = "libs"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM ?= "file://LICENSING;md5=e28ba6195a4e39904919b78a92bcf27e \
      file://COPYING.LIB;md5=4fbd65380cdd255951079008b364516c \
"

inherit autotools pkgconfig

# v4.4.1
SRCREV ?= "b8714d4e9e37cf0d511917bd5eea0e51e4a397d5"
SRCBRANCH ?= "develop"

SRC_URI = "git://github.com/besser82/libxcrypt.git;branch=${SRCBRANCH} \
           file://0001-Add-x32-specific-inline-asm.patch \
           "

PROVIDES = "virtual/crypt"

FILES_${PN} = "${libdir}/libcrypt*.so.* ${libdir}/libcrypt-*.so ${libdir}/libowcrypt*.so.* ${libdir}/libowcrypt-*.so"

S = "${WORKDIR}/git"

BUILD_CPPFLAGS = "-I${STAGING_INCDIR_NATIVE} -std=gnu99"
TARGET_CPPFLAGS = "-I${STAGING_DIR_TARGET}${includedir} -Wno-error=missing-attributes"

BBCLASSEXTEND = "nativesdk"
