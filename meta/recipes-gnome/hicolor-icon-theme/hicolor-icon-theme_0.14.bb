SUMMARY = "Default icon theme that all icon themes automatically inherit from"
HOMEPAGE = "http://icon-theme.freedesktop.org/wiki/HicolorTheme"
BUGTRACKER = "https://bugs.freedesktop.org/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=f08a446809913fc9b3c718f0eaea0426"

SRC_URI = "http://icon-theme.freedesktop.org/releases/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "56d2c6c3a9df5a0acd332430e3f3ae3e"
SRC_URI[sha256sum] = "7bed06e6ef316318274bda0fdaf39fce960e4222a0165c968d84acb428902ecd"

inherit allarch autotools

FILES_${PN} += "${datadir}/icons"
