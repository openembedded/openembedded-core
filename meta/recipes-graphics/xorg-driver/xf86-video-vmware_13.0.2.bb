require xorg-driver-video.inc

SUMMARY = "X.Org X server -- VMware SVGA display driver"

DESCRIPTION = "vmware is an Xorg driver for VMware virtual video cards."

LIC_FILES_CHKSUM = "file://COPYING;md5=5fcd7d437a959a15fbee8707747c6b53"

DEPENDS += "virtual/libx11 xineramaproto videoproto libpciaccess"

SRC_URI += "file://0001-configure-fix-build-without-xatracker.patch \
            file://0002-add-option-for-vmwgfx.patch"

SRC_URI[md5sum] = "91d1d7d33181766714405ab013d31244"
SRC_URI[sha256sum] = "c8ba3d2cead3620dba2cbf5defb7f1759b2b96f4fe209f4bf6976832b6763c54"

COMPATIBLE_HOST = '(i.86.*-linux|x86_64.*-linux)'

EXTRA_OECONF += "--disable-vmwgfx"
