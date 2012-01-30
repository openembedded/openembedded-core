require xorg-lib-common.inc

SUMMARY = "XFontCache: X Font Cache extension library"

DESCRIPTION = "FontCache is an extension that is used by X-TrueType to cache \
information about fonts."

DEPENDS += "libxext fontcacheproto zlib"
LIC_FILES_CHKSUM = "file://COPYING;md5=62a75d5d3da40f1f6eb1cbe3bcc0a5d0"
PR = "r1"
PE = "1"

XORG_PN = "libXfontcache"

SRC_URI[md5sum] = "bbd37768c87f63cf2eb845b2c0f56515"
SRC_URI[sha256sum] = "0d639219549f51fa0e6b4414383f5d13e6c1638e66b3434f4626eb989ffacbce"
