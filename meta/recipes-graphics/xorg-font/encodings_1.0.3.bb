DESCRIPTION = "The XFree86/Xorg encoding files"
require xorg-font-common.inc
LICENSE = "Public Domain"
LIC_FILES_CHKSUM = "file://COPYING;md5=9da93f2daf2d5572faa2bfaf0dbd9e76"
PE = "1"
PR = "${INC_PR}.1"

DEPENDS = "mkfontscale-native font-util-native"

EXTRA_OECONF += "--with-encodingsdir=${datadir}/fonts/X11/encodings"
