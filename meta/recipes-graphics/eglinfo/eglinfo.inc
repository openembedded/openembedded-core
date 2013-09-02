SUMMARY = "Utility for printing information EGL and its client APIs (OpenGL, OpenGL ES, OpenVG)"
SECTION = "graphics"
LICENSE = "Zlib"
HOMEPAGE = "https://github.com/dv1/eglinfo"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8d4f33bc3add976f7dfae85dab66f03c"

DEPENDS = "virtual/egl"

SRC_URI = "git://github.com/dv1/eglinfo.git;branch=master"
SRCREV = "4a49ff59545d9b6a8222badab64115f89d3a6774"

S = "${WORKDIR}/git"

inherit waf

# BSP layers should add .bbappend files for the -x11 and -fb .bb recipes,
# which append the respective EGL/OpenGLES/OpenVG libraries to DEPENDS
# not doing that here, since some platforms might not have OpenGL ES or OpenVG support
# (in that case, the eglinfo build scripts automatically disable the relevant code paths)

EGLINFO_DEVICE ?= "generic"
EGLINFO_DEVICE_raspberrypi ?= "raspberrypi"
EGLINFO_DEVICE_beagleboard ?= "beagleboard"
EGLINFO_DEVICE_mx6 ?= "imx6"

EXTRA_OECONF = "--platform=${EGLINFO_PLATFORM} \
                --device=${EGLINFO_DEVICE} \
                --sysroot ${STAGING_DIR_TARGET}"

do_install_append() {
	mv ${D}/${bindir}/eglinfo ${D}/${bindir}/${EGLINFO_BINARY_NAME}
}
