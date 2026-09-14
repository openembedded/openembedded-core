SUMMARY = "Basic desktop integration functions"
HOMEPAGE = "https://www.freedesktop.org/wiki/Software/xdg-utils/"
DESCRIPTION = "The xdg-utils package is a set of simple scripts that provide basic desktop integration functions."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a5367a90934098d6b05af3b746405014"

DEPENDS = "xmlto-native libxslt-native"

SRC_URI = "git://gitlab.freedesktop.org/xdg/xdg-utils.git;protocol=https;branch=master;tag=v${PV} \
           file://0001-Reinstate-xdg-terminal.patch \
           file://0001-Don-t-build-the-in-script-manual.patch \
           file://CVE-2022-4055.patch \
          "

SRCREV = "159fc37075db2decf446f453fe1a796da6921aad"

# Needs brokensep as this doesn't use automake
inherit autotools-brokensep

RRECOMMENDS:${PN} = "dbus-tools file \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'xprop xset', '', d)}"

CVE_STATUS[CVE-2025-52968] = "disputed"
