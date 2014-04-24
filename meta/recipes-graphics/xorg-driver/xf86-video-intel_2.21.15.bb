require xorg-driver-video.inc

SUMMARY = "X.Org X server -- Intel integrated graphics chipsets driver"

DESCRIPTION = "intel is an Xorg driver for Intel integrated graphics \
chipsets. The driver supports depths 8, 15, 16 and 24. On some chipsets, \
the driver supports hardware accelerated 3D via the Direct Rendering \
Infrastructure (DRI)."

LIC_FILES_CHKSUM = "file://COPYING;md5=8730ad58d11c7bbad9a7066d69f7808e"

DEPENDS += "virtual/libx11 drm libpciaccess pixman"

SRC_URI += "file://disable-dri2-tests.patch \
            file://compat-api-Map-changes-of-DamageUnregister-API-in-1..patch \
           "

PACKAGECONFIG ??= "sna udev ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'dri', '', d)}"

PACKAGECONFIG[dri] = "--enable-dri,--disable-dri,xf86driproto dri2proto"
PACKAGECONFIG[sna] = "--enable-sna,--disable-sna"
PACKAGECONFIG[uxa] = "--enable-uxa,--disable-uxa"
PACKAGECONFIG[udev] = "--enable-udev,--disable-udev,udev"
PACKAGECONFIG[xvmc] = "--enable-xvmc,--disable-xvmc,libxvmc"

# --enable-kms-only option is required by ROOTLESS_X
EXTRA_OECONF += '${@base_conditional( "ROOTLESS_X", "1", " --enable-kms-only", "", d )}'

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

SRC_URI[md5sum] = "8b646d257ace8197d6ab4e5ddeb8efb2"
SRC_URI[sha256sum] = "7d5a140f82a72fd1cbc8a664d66c3d4eca47ee240ca4927b8a98d7af6f65d6fc"
