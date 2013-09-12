require xorg-driver-video.inc

SUMMARY = "X.Org X server -- VMware SVGA display driver"

DESCRIPTION = "vmware is an Xorg driver for VMware virtual video cards."

LIC_FILES_CHKSUM = "file://COPYING;md5=5fcd7d437a959a15fbee8707747c6b53"

DEPENDS += "virtual/libx11 xineramaproto videoproto libpciaccess"

SRC_URI += "file://vmwgfx-option.patch"

SRC_URI[md5sum] = "b08e0195ebf3f88a82129322cb93da08"
SRC_URI[sha256sum] = "802dda415c22412edad6c3df44fe18a06e91d0f8456d9a58bac0d340fdf8fe3d"

COMPATIBLE_HOST = '(i.86.*-linux|x86_64.*-linux)'

EXTRA_OECONF += "--disable-vmwgfx"
