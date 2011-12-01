require xserver-xorg-lite.inc
require xserver-xorg-${PV}.inc

FILESEXTRAPATHS_append := "${THISDIR}/xserver-xorg:${THISDIR}/xserver-xorg-${PV}:"
